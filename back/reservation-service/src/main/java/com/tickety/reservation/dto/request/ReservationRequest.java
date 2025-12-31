package com.tickety.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "예약 요청 DTO")
public class ReservationRequest {

    @NotNull(message = "Concert ID is required")
    @Schema(description = "공연 ID", required = true)
    private UUID concertId;

    @NotEmpty(message = "At least one seat must be selected")
    @Schema(description = "선택한 좌석 ID 목록", required = true)
    private List<UUID> seatIds;
}
