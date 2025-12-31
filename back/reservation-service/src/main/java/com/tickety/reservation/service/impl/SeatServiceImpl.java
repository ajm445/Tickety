package com.tickety.reservation.service.impl;

import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.response.SeatResponse;
import com.tickety.reservation.exception.SeatNotFoundException;
import com.tickety.reservation.repository.SeatRepository;
import com.tickety.reservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<SeatResponse> getSeatsByConcertId(Long concertId) {
        return seatRepository.findByConcertId(concertId).stream()
                .map(SeatResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatResponse> getAvailableSeatsByConcertId(Long concertId) {
        return seatRepository.findByConcertIdAndStatus(concertId, SeatStatus.AVAILABLE).stream()
                .map(SeatResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public SeatResponse getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .map(SeatResponse::from)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
    }
}
