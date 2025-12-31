package com.tickety.reservation.dto.response;

import com.tickety.reservation.domain.entity.ReservationSeat;
import com.tickety.reservation.domain.enums.SeatGrade;
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
@Schema(description = "예약 좌석 응답 DTO")
public class ReservationSeatResponse {

    @Schema(description = "예약-좌석 ID")
    private UUID id;

    @Schema(description = "좌석 ID")
    private UUID seatId;

    @Schema(description = "전체 좌석 번호", example = "VIP-A-1")
    private String fullSeatNumber;

    @Schema(description = "좌석 등급", example = "VIP")
    private SeatGrade grade;

    @Schema(description = "좌석 가격", example = "150000")
    private Integer price;

    public static ReservationSeatResponse from(ReservationSeat reservationSeat) {
        return ReservationSeatResponse.builder()
                .id(reservationSeat.getId())
                .seatId(reservationSeat.getSeat().getId())
                .fullSeatNumber(reservationSeat.getSeat().getFullSeatNumber())
                .grade(reservationSeat.getSeat().getGrade())
                .price(reservationSeat.getPrice())
                .build();
    }
}
