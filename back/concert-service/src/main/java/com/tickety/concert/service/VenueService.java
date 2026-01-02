package com.tickety.concert.service;

import com.tickety.concert.dto.request.VenueCreateRequest;
import com.tickety.concert.dto.response.VenueResponse;

import java.util.List;
import java.util.UUID;

public interface VenueService {

    List<VenueResponse> getAllVenues();

    VenueResponse getVenueById(UUID venueId);

    List<VenueResponse> getVenuesByCity(String city);

    VenueResponse createVenue(VenueCreateRequest request);

    VenueResponse updateVenue(UUID venueId, VenueCreateRequest request);

    void deleteVenue(UUID venueId);
}
