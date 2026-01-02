package com.tickety.reservation.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDto {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private Integer totalSeats;
}
