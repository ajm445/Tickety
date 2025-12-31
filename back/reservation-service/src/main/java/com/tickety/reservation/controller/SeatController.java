package com.tickety.reservation.controller;

import com.tickety.reservation.dto.response.ApiResponse;
import com.tickety.reservation.dto.response.SeatResponse;
import com.tickety.reservation.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations/seats")
@RequiredArgsConstructor
@Tag(name = "Seat", description = "좌석 관리 API")
public class SeatController {

    private final SeatService seatService;

    @Operation(summary = "공연별 전체 좌석 조회", description = "특정 공연의 모든 좌석 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatsByConcertId(
            @Parameter(description = "공연 ID", required = true) @PathVariable Long concertId) {
        List<SeatResponse> seats = seatService.getSeatsByConcertId(concertId);
        return ResponseEntity.ok(ApiResponse.success(seats));
    }

    @Operation(summary = "공연별 예약 가능 좌석 조회", description = "특정 공연의 예약 가능한 좌석만 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/concert/{concertId}/available")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getAvailableSeatsByConcertId(
            @Parameter(description = "공연 ID", required = true) @PathVariable Long concertId) {
        List<SeatResponse> seats = seatService.getAvailableSeatsByConcertId(concertId);
        return ResponseEntity.ok(ApiResponse.success(seats));
    }

    @Operation(summary = "좌석 상세 조회", description = "특정 좌석의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "좌석을 찾을 수 없음")
    })
    @GetMapping("/{seatId}")
    public ResponseEntity<ApiResponse<SeatResponse>> getSeatById(
            @Parameter(description = "좌석 ID", required = true) @PathVariable Long seatId) {
        SeatResponse seat = seatService.getSeatById(seatId);
        return ResponseEntity.ok(ApiResponse.success(seat));
    }
}
