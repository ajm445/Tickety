package com.tickety.concert.service.impl;

import com.tickety.concert.domain.entity.Venue;
import com.tickety.concert.dto.request.VenueCreateRequest;
import com.tickety.concert.dto.response.VenueResponse;
import com.tickety.concert.exception.VenueNotFoundException;
import com.tickety.concert.repository.VenueRepository;
import com.tickety.concert.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(VenueResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public VenueResponse getVenueById(UUID venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
        return VenueResponse.from(venue);
    }

    @Override
    public List<VenueResponse> getVenuesByCity(String city) {
        return venueRepository.findByCity(city).stream()
                .map(VenueResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VenueResponse createVenue(VenueCreateRequest request) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .totalSeats(request.getTotalSeats())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        Venue savedVenue = venueRepository.save(venue);
        log.info("Venue created: {}", savedVenue.getName());

        return VenueResponse.from(savedVenue);
    }

    @Override
    @Transactional
    public VenueResponse updateVenue(UUID venueId, VenueCreateRequest request) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        venue.update(
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getTotalSeats(),
                request.getDescription(),
                request.getImageUrl()
        );

        log.info("Venue updated: {}", venue.getName());

        return VenueResponse.from(venue);
    }

    @Override
    @Transactional
    public void deleteVenue(UUID venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        venueRepository.delete(venue);
        log.info("Venue deleted: {}", venueId);
    }
}
