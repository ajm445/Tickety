package com.tickety.reservation.service;

import com.tickety.reservation.dto.response.SeatResponse;

import java.util.List;
import java.util.UUID;

public interface SeatService {

    List<SeatResponse> getSeatsByConcertId(UUID concertId);

    List<SeatResponse> getAvailableSeatsByConcertId(UUID concertId);

    SeatResponse getSeatById(UUID seatId);

    void holdSeat(UUID seatId, UUID userId, int holdMinutes);

    void releaseSeat(UUID seatId);

    int releaseExpiredHeldSeats();
}
