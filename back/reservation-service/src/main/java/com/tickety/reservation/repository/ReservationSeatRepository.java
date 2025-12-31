package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, UUID> {

    List<ReservationSeat> findByReservationId(UUID reservationId);

    @Query("SELECT rs FROM ReservationSeat rs JOIN FETCH rs.seat WHERE rs.reservation.id = :reservationId")
    List<ReservationSeat> findByReservationIdWithSeat(@Param("reservationId") UUID reservationId);

    boolean existsBySeatId(UUID seatId);
}
