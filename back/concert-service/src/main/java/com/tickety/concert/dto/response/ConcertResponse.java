package com.tickety.concert.dto.response;

import com.tickety.concert.domain.entity.Concert;
import com.tickety.concert.domain.enums.ConcertStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "공연 응답 DTO")
public class ConcertResponse {

    @Schema(description = "공연 ID")
    private UUID id;

    @Schema(description = "공연장 ID")
    private UUID venueId;

    @Schema(description = "공연장 정보")
    private VenueResponse venue;

    @Schema(description = "공연 제목")
    private String title;

    @Schema(description = "아티스트")
    private String artist;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "공연 일시")
    private OffsetDateTime concertDate;

    @Schema(description = "예매 시작 일시")
    private OffsetDateTime bookingStartAt;

    @Schema(description = "예매 종료 일시")
    private OffsetDateTime bookingEndAt;

    @Schema(description = "공연 상태")
    private ConcertStatus status;

    @Schema(description = "포스터 URL")
    private String posterUrl;

    @Schema(description = "최소 가격")
    private Integer priceMin;

    @Schema(description = "최대 가격")
    private Integer priceMax;

    @Schema(description = "예매 가능 여부")
    private Boolean bookingOpen;

    public static ConcertResponse from(Concert concert) {
        return ConcertResponse.builder()
                .id(concert.getId())
                .venueId(concert.getVenue().getId())
                .venue(VenueResponse.from(concert.getVenue()))
                .title(concert.getTitle())
                .artist(concert.getArtist())
                .description(concert.getDescription())
                .concertDate(concert.getConcertDate())
                .bookingStartAt(concert.getBookingStartAt())
                .bookingEndAt(concert.getBookingEndAt())
                .status(concert.getStatus())
                .posterUrl(concert.getPosterUrl())
                .priceMin(concert.getPriceMin())
                .priceMax(concert.getPriceMax())
                .bookingOpen(concert.isBookingOpen())
                .build();
    }

    public static ConcertResponse simpleFrom(Concert concert) {
        return ConcertResponse.builder()
                .id(concert.getId())
                .venueId(concert.getVenue().getId())
                .venue(VenueResponse.from(concert.getVenue()))
                .title(concert.getTitle())
                .artist(concert.getArtist())
                .concertDate(concert.getConcertDate())
                .status(concert.getStatus())
                .posterUrl(concert.getPosterUrl())
                .priceMin(concert.getPriceMin())
                .priceMax(concert.getPriceMax())
                .bookingOpen(concert.isBookingOpen())
                .build();
    }
}
