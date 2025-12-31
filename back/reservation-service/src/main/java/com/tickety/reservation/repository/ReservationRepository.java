package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByUserId(UUID userId);

    List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.concert WHERE r.userId = :userId")
    List<Reservation> findByUserIdWithConcert(@Param("userId") UUID userId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.concert JOIN FETCH r.reservationSeats WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") UUID id);

    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Query("SELECT r FROM Reservation r WHERE r.status = :status AND r.expiresAt < :now")
    List<Reservation> findExpiredReservations(
            @Param("status") ReservationStatus status,
            @Param("now") OffsetDateTime now
    );

    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'EXPIRED' WHERE r.status = 'PENDING' AND r.expiresAt < :now")
    int expireReservations(@Param("now") OffsetDateTime now);

    @Query("SELECT r FROM Reservation r WHERE r.concert.id = :concertId AND r.status IN :statuses")
    List<Reservation> findByConcertIdAndStatusIn(
            @Param("concertId") UUID concertId,
            @Param("statuses") List<ReservationStatus> statuses
    );
}
