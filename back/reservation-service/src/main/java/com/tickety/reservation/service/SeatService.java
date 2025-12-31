package com.tickety.reservation.service;

import com.tickety.reservation.dto.response.SeatResponse;

import java.util.List;

public interface SeatService {

    List<SeatResponse> getSeatsByConcertId(Long concertId);

    List<SeatResponse> getAvailableSeatsByConcertId(Long concertId);

    SeatResponse getSeatById(Long seatId);
}
