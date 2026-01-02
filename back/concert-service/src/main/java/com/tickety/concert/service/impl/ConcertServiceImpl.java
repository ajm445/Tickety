package com.tickety.concert.service.impl;

import com.tickety.concert.domain.entity.Concert;
import com.tickety.concert.domain.entity.Venue;
import com.tickety.concert.domain.enums.ConcertStatus;
import com.tickety.concert.dto.request.ConcertCreateRequest;
import com.tickety.concert.dto.response.ConcertResponse;
import com.tickety.concert.exception.ConcertNotFoundException;
import com.tickety.concert.exception.VenueNotFoundException;
import com.tickety.concert.repository.ConcertRepository;
import com.tickety.concert.repository.VenueRepository;
import com.tickety.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;

    @Override
    public List<ConcertResponse> getAllConcerts() {
        return concertRepository.findAllWithVenue().stream()
                .map(ConcertResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConcertResponse> getOpenConcerts() {
        List<ConcertStatus> openStatuses = Arrays.asList(
                ConcertStatus.SCHEDULED,
                ConcertStatus.OPEN
        );
        return concertRepository.findByStatusInWithVenue(openStatuses).stream()
                .map(ConcertResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConcertResponse> getUpcomingConcerts() {
        return concertRepository.findUpcomingConcerts(OffsetDateTime.now()).stream()
                .map(ConcertResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    @Override
    public ConcertResponse getConcertById(UUID concertId) {
        Concert concert = concertRepository.findByIdWithVenue(concertId)
                .orElseThrow(() -> new ConcertNotFoundException(concertId));
        return ConcertResponse.from(concert);
    }

    @Override
    public List<ConcertResponse> searchConcerts(String keyword) {
        return concertRepository.searchByKeyword(keyword).stream()
                .map(ConcertResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConcertResponse createConcert(ConcertCreateRequest request) {
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException(request.getVenueId()));

        Concert concert = Concert.builder()
                .venue(venue)
                .title(request.getTitle())
                .artist(request.getArtist())
                .description(request.getDescription())
                .concertDate(request.getConcertDate())
                .bookingStartAt(request.getBookingStartAt())
                .bookingEndAt(request.getBookingEndAt())
                .posterUrl(request.getPosterUrl())
                .priceMin(request.getPriceMin())
                .priceMax(request.getPriceMax())
                .status(ConcertStatus.SCHEDULED)
                .build();

        Concert savedConcert = concertRepository.save(concert);
        log.info("Concert created: {}", savedConcert.getTitle());

        return ConcertResponse.from(savedConcert);
    }

    @Override
    @Transactional
    public ConcertResponse updateConcertStatus(UUID concertId, String status) {
        Concert concert = concertRepository.findByIdWithVenue(concertId)
                .orElseThrow(() -> new ConcertNotFoundException(concertId));

        ConcertStatus newStatus = ConcertStatus.valueOf(status.toUpperCase());

        switch (newStatus) {
            case OPEN -> concert.openBooking();
            case SOLD_OUT -> concert.markSoldOut();
            case CANCELLED -> concert.cancel();
            case COMPLETED -> concert.complete();
            default -> throw new IllegalStateException("Cannot change status to: " + status);
        }

        log.info("Concert {} status changed to {}", concertId, newStatus);

        return ConcertResponse.from(concert);
    }

    @Override
    @Transactional
    public void deleteConcert(UUID concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertNotFoundException(concertId));

        concertRepository.delete(concert);
        log.info("Concert deleted: {}", concertId);
    }
}
