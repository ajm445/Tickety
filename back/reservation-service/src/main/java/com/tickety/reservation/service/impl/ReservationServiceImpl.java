package com.tickety.reservation.service.impl;

import com.tickety.reservation.domain.entity.Concert;
import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.entity.ReservationSeat;
import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.ReservationStatus;
import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;
import com.tickety.reservation.exception.ConcertNotFoundException;
import com.tickety.reservation.exception.ReservationNotFoundException;
import com.tickety.reservation.exception.SeatAlreadyReservedException;
import com.tickety.reservation.exception.SeatNotFoundException;
import com.tickety.reservation.repository.ConcertRepository;
import com.tickety.reservation.repository.ReservationRepository;
import com.tickety.reservation.repository.ReservationSeatRepository;
import com.tickety.reservation.repository.SeatRepository;
import com.tickety.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private static final int RESERVATION_EXPIRATION_MINUTES = 10;

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, UUID userId) {
        Concert concert = concertRepository.findById(request.getConcertId())
                .orElseThrow(() -> new ConcertNotFoundException(request.getConcertId()));

        List<Seat> seats = seatRepository.findAllByIdWithPessimisticLock(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new SeatNotFoundException("One or more seats not found.");
        }

        // Validate all seats are available
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE && seat.getStatus() != SeatStatus.HELD) {
                throw new SeatAlreadyReservedException(seat.getId());
            }
            // If held, verify it's held by the same user
            if (seat.getStatus() == SeatStatus.HELD && !seat.getHeldBy().equals(userId)) {
                throw new SeatAlreadyReservedException(seat.getId());
            }
        }

        // Create reservation
        Reservation reservation = Reservation.create(userId, concert, RESERVATION_EXPIRATION_MINUTES);
        reservation.setReservationNumber(generateReservationNumber());

        // Calculate total and reserve seats
        int totalAmount = 0;
        for (Seat seat : seats) {
            seat.reserve();
            totalAmount += seat.getPrice();
        }
        reservation.setTotalAmount(totalAmount);

        Reservation savedReservation = reservationRepository.save(reservation);

        // Create reservation seats
        for (Seat seat : seats) {
            ReservationSeat reservationSeat = ReservationSeat.create(savedReservation, seat);
            reservationSeatRepository.save(reservationSeat);
            savedReservation.addSeat(reservationSeat);
        }

        log.info("Reservation {} created for user {} with {} seats",
                savedReservation.getReservationNumber(), userId, seats.size());

        return ReservationResponse.from(savedReservation);
    }

    @Override
    public List<ReservationResponse> getReservationsByUserId(UUID userId) {
        return reservationRepository.findByUserIdWithConcert(userId).stream()
                .map(ReservationResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse getReservationById(UUID reservationId) {
        return reservationRepository.findByIdWithDetails(reservationId)
                .map(ReservationResponse::from)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    @Override
    public ReservationResponse getReservationByNumber(String reservationNumber) {
        return reservationRepository.findByReservationNumber(reservationNumber)
                .map(ReservationResponse::from)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: " + reservationNumber));
    }

    @Override
    @Transactional
    public void cancelReservation(UUID reservationId, UUID userId) {
        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalStateException("You can only cancel your own reservations.");
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed reservations cannot be cancelled directly.");
        }

        // Release all seats
        List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservationIdWithSeat(reservationId);
        for (ReservationSeat rs : reservationSeats) {
            Seat seat = seatRepository.findByIdWithPessimisticLock(rs.getSeat().getId())
                    .orElse(null);
            if (seat != null) {
                seat.release();
            }
        }

        reservation.cancel("Cancelled by user");

        log.info("Reservation {} cancelled by user {}", reservationId, userId);
    }

    @Override
    @Transactional
    public ReservationResponse confirmReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.isExpired()) {
            throw new IllegalStateException("Reservation has expired.");
        }

        // Confirm all seat sales
        List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservationIdWithSeat(reservationId);
        for (ReservationSeat rs : reservationSeats) {
            Seat seat = seatRepository.findByIdWithPessimisticLock(rs.getSeat().getId())
                    .orElseThrow(() -> new SeatNotFoundException(rs.getSeat().getId()));
            seat.confirmSale();
        }

        reservation.confirm();

        log.info("Reservation {} confirmed", reservationId);

        return ReservationResponse.from(reservation);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 60000) // Run every minute
    public void processExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(
                ReservationStatus.PENDING,
                OffsetDateTime.now()
        );

        for (Reservation reservation : expiredReservations) {
            try {
                List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservationIdWithSeat(reservation.getId());

                for (ReservationSeat rs : reservationSeats) {
                    Seat seat = seatRepository.findByIdWithPessimisticLock(rs.getSeat().getId())
                            .orElse(null);
                    if (seat != null && seat.getStatus() == SeatStatus.RESERVED) {
                        seat.release();
                    }
                }

                reservation.expire();
                log.info("Expired reservation {} has been processed", reservation.getId());
            } catch (Exception e) {
                log.error("Failed to process expired reservation {}: {}", reservation.getId(), e.getMessage());
            }
        }
    }

    private String generateReservationNumber() {
        String dateStr = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", new Random().nextInt(1000000));
        return "TKT" + dateStr + "-" + randomStr;
    }
}
