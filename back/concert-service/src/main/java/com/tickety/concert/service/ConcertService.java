package com.tickety.concert.service;

import com.tickety.concert.dto.request.ConcertCreateRequest;
import com.tickety.concert.dto.response.ConcertResponse;

import java.util.List;
import java.util.UUID;

public interface ConcertService {

    List<ConcertResponse> getAllConcerts();

    List<ConcertResponse> getOpenConcerts();

    List<ConcertResponse> getUpcomingConcerts();

    ConcertResponse getConcertById(UUID concertId);

    List<ConcertResponse> searchConcerts(String keyword);

    ConcertResponse createConcert(ConcertCreateRequest request);

    ConcertResponse updateConcertStatus(UUID concertId, String status);

    void deleteConcert(UUID concertId);
}
