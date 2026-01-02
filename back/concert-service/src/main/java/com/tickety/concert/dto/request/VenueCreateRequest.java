package com.tickety.concert.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공연장 생성 요청 DTO")
public class VenueCreateRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "공연장 이름", example = "올림픽공원 체조경기장", required = true)
    private String name;

    @NotBlank(message = "Address is required")
    @Schema(description = "주소", example = "서울특별시 송파구 올림픽로 424", required = true)
    private String address;

    @NotBlank(message = "City is required")
    @Schema(description = "도시", example = "서울", required = true)
    private String city;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    @Schema(description = "총 좌석 수", example = "15000", required = true)
    private Integer totalSeats;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "이미지 URL")
    private String imageUrl;
}
