package com.tickety.concert.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Schema(description = "공연 생성 요청 DTO")
public class ConcertCreateRequest {

    @NotNull(message = "Venue ID is required")
    @Schema(description = "공연장 ID", required = true)
    private UUID venueId;

    @NotBlank(message = "Title is required")
    @Schema(description = "공연 제목", example = "2025 아이유 콘서트", required = true)
    private String title;

    @NotBlank(message = "Artist is required")
    @Schema(description = "아티스트", example = "아이유", required = true)
    private String artist;

    @Schema(description = "설명")
    private String description;

    @NotNull(message = "Concert date is required")
    @Schema(description = "공연 일시", required = true)
    private OffsetDateTime concertDate;

    @NotNull(message = "Booking start date is required")
    @Schema(description = "예매 시작 일시", required = true)
    private OffsetDateTime bookingStartAt;

    @NotNull(message = "Booking end date is required")
    @Schema(description = "예매 종료 일시", required = true)
    private OffsetDateTime bookingEndAt;

    @Schema(description = "포스터 URL")
    private String posterUrl;

    @NotNull(message = "Minimum price is required")
    @Positive(message = "Minimum price must be positive")
    @Schema(description = "최소 가격", example = "50000", required = true)
    private Integer priceMin;

    @NotNull(message = "Maximum price is required")
    @Positive(message = "Maximum price must be positive")
    @Schema(description = "최대 가격", example = "150000", required = true)
    private Integer priceMax;
}
