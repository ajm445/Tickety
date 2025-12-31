package com.tickety.reservation.dto.response;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "예약 응답 DTO")
public class ReservationResponse {

    @Schema(description = "예약 ID", example = "1")
    private Long reservationId;

    @Schema(description = "좌석 ID", example = "1")
    private Long seatId;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "예약 상태", example = "PENDING")
    private ReservationStatus status;

    @Schema(description = "예약 시간")
    private LocalDateTime reservedAt;

    @Schema(description = "예약 만료 시간")
    private LocalDateTime expiredAt;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .seatId(reservation.getSeatId())
                .userId(reservation.getUserId())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .expiredAt(reservation.getExpiredAt())
                .build();
    }
}
