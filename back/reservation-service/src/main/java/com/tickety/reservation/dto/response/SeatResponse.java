package com.tickety.reservation.dto.response;

import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
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
@Schema(description = "좌석 응답 DTO")
public class SeatResponse {

    @Schema(description = "좌석 ID")
    private UUID id;

    @Schema(description = "공연 ID")
    private UUID concertId;

    @Schema(description = "섹션", example = "VIP")
    private String section;

    @Schema(description = "열 번호", example = "A")
    private String rowNumber;

    @Schema(description = "좌석 번호", example = "1")
    private Integer seatNumber;

    @Schema(description = "전체 좌석 번호", example = "VIP-A-1")
    private String fullSeatNumber;

    @Schema(description = "좌석 등급", example = "VIP")
    private SeatGrade grade;

    @Schema(description = "좌석 가격", example = "150000")
    private Integer price;

    @Schema(description = "좌석 상태", example = "AVAILABLE")
    private SeatStatus status;

    public static SeatResponse from(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .concertId(seat.getConcert().getId())
                .section(seat.getSection())
                .rowNumber(seat.getRowNumber())
                .seatNumber(seat.getSeatNumber())
                .fullSeatNumber(seat.getFullSeatNumber())
                .grade(seat.getGrade())
                .price(seat.getPrice())
                .status(seat.getStatus())
                .build();
    }
}
