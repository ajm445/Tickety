package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByConcertId(UUID concertId);

    List<Seat> findByConcertIdAndStatus(UUID concertId, SeatStatus status);

    @Query("SELECT s FROM Seat s JOIN FETCH s.concert WHERE s.concert.id = :concertId")
    List<Seat> findByConcertIdWithConcert(@Param("concertId") UUID concertId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findByIdWithPessimisticLock(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findAllByIdWithPessimisticLock(@Param("ids") List<UUID> ids);

    @Query("SELECT s FROM Seat s WHERE s.status = 'HELD' AND s.heldUntil < :now")
    List<Seat> findExpiredHeldSeats(@Param("now") OffsetDateTime now);

    @Modifying
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE', s.heldBy = null, s.heldUntil = null " +
           "WHERE s.status = 'HELD' AND s.heldUntil < :now")
    int releaseExpiredHeldSeats(@Param("now") OffsetDateTime now);
}
