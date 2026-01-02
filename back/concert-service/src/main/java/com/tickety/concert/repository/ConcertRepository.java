package com.tickety.concert.repository;

import com.tickety.concert.domain.entity.Concert;
import com.tickety.concert.domain.enums.ConcertStatus;
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

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue")
    List<Concert> findAllWithVenue();

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.status = :status")
    List<Concert> findByStatusWithVenue(@Param("status") ConcertStatus status);

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.status IN :statuses ORDER BY c.concertDate ASC")
    List<Concert> findByStatusInWithVenue(@Param("statuses") List<ConcertStatus> statuses);

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.concertDate >= :date ORDER BY c.concertDate ASC")
    List<Concert> findUpcomingConcerts(@Param("date") OffsetDateTime date);

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY c.concertDate ASC")
    List<Concert> searchByKeyword(@Param("keyword") String keyword);

    List<Concert> findByVenueId(UUID venueId);

    List<Concert> findByArtistContainingIgnoreCase(String artist);
}
