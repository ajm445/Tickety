package com.tickety.concert.dto.response;

import com.tickety.concert.domain.entity.Venue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공연장 응답 DTO")
public class VenueResponse {

    @Schema(description = "공연장 ID")
    private UUID id;

    @Schema(description = "공연장 이름")
    private String name;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "도시")
    private String city;

    @Schema(description = "총 좌석 수")
    private Integer totalSeats;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    public static VenueResponse from(Venue venue) {
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .totalSeats(venue.getTotalSeats())
                .description(venue.getDescription())
                .imageUrl(venue.getImageUrl())
                .build();
    }
}
