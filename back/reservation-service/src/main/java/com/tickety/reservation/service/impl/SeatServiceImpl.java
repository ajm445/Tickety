package com.tickety.reservation.service.impl;

import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.response.SeatResponse;
import com.tickety.reservation.exception.SeatNotFoundException;
import com.tickety.reservation.repository.SeatRepository;
import com.tickety.reservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<SeatResponse> getSeatsByConcertId(UUID concertId) {
        return seatRepository.findByConcertIdWithConcert(concertId).stream()
                .map(SeatResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatResponse> getAvailableSeatsByConcertId(UUID concertId) {
        return seatRepository.findByConcertIdAndStatus(concertId, SeatStatus.AVAILABLE).stream()
                .map(SeatResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public SeatResponse getSeatById(UUID seatId) {
        return seatRepository.findById(seatId)
                .map(SeatResponse::from)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
    }

    @Override
    @Transactional
    public void holdSeat(UUID seatId, UUID userId, int holdMinutes) {
        Seat seat = seatRepository.findByIdWithPessimisticLock(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
        seat.hold(userId, holdMinutes);
    }

    @Override
    @Transactional
    public void releaseSeat(UUID seatId) {
        Seat seat = seatRepository.findByIdWithPessimisticLock(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
        seat.release();
    }

    @Override
    @Transactional
    public int releaseExpiredHeldSeats() {
        return seatRepository.releaseExpiredHeldSeats(OffsetDateTime.now());
    }
}
