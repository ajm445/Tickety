package com.tickety.reservation.service.impl;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.ReservationStatus;
import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;
import com.tickety.reservation.exception.ReservationNotFoundException;
import com.tickety.reservation.exception.SeatAlreadyReservedException;
import com.tickety.reservation.exception.SeatNotFoundException;
import com.tickety.reservation.repository.ReservationRepository;
import com.tickety.reservation.repository.SeatRepository;
import com.tickety.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private static final int RESERVATION_EXPIRATION_MINUTES = 10;

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public List<ReservationResponse> createReservation(ReservationRequest request, Long userId) {
        List<Seat> seats = seatRepository.findAllByIdWithPessimisticLock(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new SeatNotFoundException("One or more seats not found.");
        }

        List<Reservation> reservations = new ArrayList<>();

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new SeatAlreadyReservedException(seat.getId());
            }

            seat.reserve();

            Reservation reservation = Reservation.create(
                    seat.getId(),
                    userId,
                    RESERVATION_EXPIRATION_MINUTES
            );
            reservations.add(reservationRepository.save(reservation));
        }

        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(ReservationResponse::from)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalStateException("You can only cancel your own reservations.");
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed reservations cannot be cancelled directly.");
        }

        Seat seat = seatRepository.findByIdWithPessimisticLock(reservation.getSeatId())
                .orElseThrow(() -> new SeatNotFoundException(reservation.getSeatId()));

        seat.release();
        reservation.cancel();

        log.info("Reservation {} cancelled by user {}", reservationId, userId);
    }

    @Override
    @Transactional
    public ReservationResponse confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.isExpired()) {
            throw new IllegalStateException("Reservation has expired.");
        }

        Seat seat = seatRepository.findByIdWithPessimisticLock(reservation.getSeatId())
                .orElseThrow(() -> new SeatNotFoundException(reservation.getSeatId()));

        reservation.confirm();
        seat.confirmSale();

        log.info("Reservation {} confirmed", reservationId);

        return ReservationResponse.from(reservation);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 60000) // Run every minute
    public void processExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(
                ReservationStatus.PENDING,
                LocalDateTime.now()
        );

        for (Reservation reservation : expiredReservations) {
            try {
                Seat seat = seatRepository.findByIdWithPessimisticLock(reservation.getSeatId())
                        .orElse(null);

                if (seat != null && seat.getStatus() == SeatStatus.RESERVED) {
                    seat.release();
                }

                reservation.cancel();
                log.info("Expired reservation {} has been cancelled", reservation.getId());
            } catch (Exception e) {
                log.error("Failed to process expired reservation {}: {}", reservation.getId(), e.getMessage());
            }
        }
    }
}
