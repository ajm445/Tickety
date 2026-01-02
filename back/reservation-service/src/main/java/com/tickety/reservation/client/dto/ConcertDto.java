package com.tickety.reservation.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertDto {

    private UUID id;
    private UUID venueId;
    private VenueDto venue;
    private String title;
    private String artist;
    private String description;
    private OffsetDateTime concertDate;
    private OffsetDateTime bookingStartAt;
    private OffsetDateTime bookingEndAt;
    private String status;
    private String posterUrl;
    private Integer priceMin;
    private Integer priceMax;
    private Boolean bookingOpen;
}
