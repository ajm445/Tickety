package com.tickety.reservation.dto.response;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "예약 응답 DTO")
public class ReservationResponse {

    @Schema(description = "예약 ID")
    private UUID id;

    @Schema(description = "예약 번호", example = "TKT20251231-123456")
    private String reservationNumber;

    @Schema(description = "사용자 ID")
    private UUID userId;

    @Schema(description = "공연 ID")
    private UUID concertId;

    @Schema(description = "공연 제목")
    private String concertTitle;

    @Schema(description = "예약 상태", example = "PENDING")
    private ReservationStatus status;

    @Schema(description = "총 결제 금액", example = "300000")
    private Integer totalAmount;

    @Schema(description = "예약 좌석 목록")
    private List<ReservationSeatResponse> seats;

    @Schema(description = "예약 생성 시간")
    private OffsetDateTime createdAt;

    @Schema(description = "예약 만료 시간")
    private OffsetDateTime expiresAt;

    @Schema(description = "예약 확정 시간")
    private OffsetDateTime confirmedAt;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .userId(reservation.getUserId())
                .concertId(reservation.getConcert().getId())
                .concertTitle(reservation.getConcert().getTitle())
                .status(reservation.getStatus())
                .totalAmount(reservation.getTotalAmount())
                .seats(reservation.getReservationSeats().stream()
                        .map(ReservationSeatResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(reservation.getCreatedAt())
                .expiresAt(reservation.getExpiresAt())
                .confirmedAt(reservation.getConfirmedAt())
                .build();
    }

    public static ReservationResponse simpleFrom(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .userId(reservation.getUserId())
                .concertId(reservation.getConcert().getId())
                .concertTitle(reservation.getConcert().getTitle())
                .status(reservation.getStatus())
                .totalAmount(reservation.getTotalAmount())
                .createdAt(reservation.getCreatedAt())
                .expiresAt(reservation.getExpiresAt())
                .confirmedAt(reservation.getConfirmedAt())
                .build();
    }
}
