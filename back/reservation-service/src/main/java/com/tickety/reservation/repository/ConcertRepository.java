package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.Concert;
import com.tickety.reservation.domain.enums.ConcertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, UUID> {

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.id = :id")
    Optional<Concert> findByIdWithVenue(@Param("id") UUID id);

    List<Concert> findByStatus(ConcertStatus status);

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.status = :status")
    List<Concert> findByStatusWithVenue(@Param("status") ConcertStatus status);

    @Query("SELECT c FROM Concert c WHERE c.status = 'OPEN' AND c.bookingStartAt <= :now AND c.bookingEndAt >= :now")
    List<Concert> findOpenConcerts(@Param("now") OffsetDateTime now);

    Page<Concert> findByStatusIn(List<ConcertStatus> statuses, Pageable pageable);

    List<Concert> findByArtistContainingIgnoreCase(String artist);

    List<Concert> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT c FROM Concert c WHERE c.concertDate BETWEEN :startDate AND :endDate")
    List<Concert> findByConcertDateBetween(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    List<Concert> findByVenueId(UUID venueId);
}
