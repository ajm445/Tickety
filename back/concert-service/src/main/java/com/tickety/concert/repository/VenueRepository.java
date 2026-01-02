package com.tickety.concert.repository;

import com.tickety.concert.domain.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueRepository extends JpaRepository<Venue, UUID> {

    List<Venue> findByCity(String city);

    List<Venue> findByNameContainingIgnoreCase(String name);
}
