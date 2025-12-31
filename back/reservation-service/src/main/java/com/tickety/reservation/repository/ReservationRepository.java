package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    Optional<Reservation> findBySeatIdAndStatus(Long seatId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.status = :status AND r.expiredAt < :now")
    List<Reservation> findExpiredReservations(
            @Param("status") ReservationStatus status,
            @Param("now") LocalDateTime now
    );

    boolean existsBySeatIdAndStatusIn(Long seatId, List<ReservationStatus> statuses);
}
