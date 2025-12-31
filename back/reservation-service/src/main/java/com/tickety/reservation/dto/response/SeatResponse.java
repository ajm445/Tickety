package com.tickety.reservation.dto.response;

import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "좌석 응답 DTO")
public class SeatResponse {

    @Schema(description = "좌석 ID", example = "1")
    private Long seatId;

    @Schema(description = "공연 ID", example = "1")
    private Long concertId;

    @Schema(description = "좌석 번호", example = "A-1")
    private String seatNumber;

    @Schema(description = "좌석 등급", example = "VIP")
    private SeatGrade seatGrade;

    @Schema(description = "좌석 가격", example = "150000.00")
    private BigDecimal price;

    @Schema(description = "좌석 상태", example = "AVAILABLE")
    private SeatStatus status;

    public static SeatResponse from(Seat seat) {
        return SeatResponse.builder()
                .seatId(seat.getId())
                .concertId(seat.getConcertId())
                .seatNumber(seat.getSeatNumber())
                .seatGrade(seat.getSeatGrade())
                .price(seat.getPrice())
                .status(seat.getStatus())
                .build();
    }
}
