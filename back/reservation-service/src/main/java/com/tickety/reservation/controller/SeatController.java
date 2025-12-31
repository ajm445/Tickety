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
import java.util.UUID;

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
            @Parameter(description = "공연 ID", required = true) @PathVariable UUID concertId) {
        List<SeatResponse> seats = seatService.getSeatsByConcertId(concertId);
        return ResponseEntity.ok(ApiResponse.success(seats));
    }

    @Operation(summary = "공연별 예약 가능 좌석 조회", description = "특정 공연의 예약 가능한 좌석만 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/concert/{concertId}/available")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getAvailableSeatsByConcertId(
            @Parameter(description = "공연 ID", required = true) @PathVariable UUID concertId) {
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
            @Parameter(description = "좌석 ID", required = true) @PathVariable UUID seatId) {
        SeatResponse seat = seatService.getSeatById(seatId);
        return ResponseEntity.ok(ApiResponse.success(seat));
    }

    @Operation(summary = "좌석 임시 점유", description = "좌석을 일정 시간 동안 임시 점유합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "점유 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "좌석이 이미 점유됨")
    })
    @PostMapping("/{seatId}/hold")
    public ResponseEntity<ApiResponse<Void>> holdSeat(
            @Parameter(description = "좌석 ID", required = true) @PathVariable UUID seatId,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader(value = "X-User-Id") UUID userId,
            @Parameter(description = "점유 시간(분)", example = "5")
            @RequestParam(defaultValue = "5") int holdMinutes) {
        seatService.holdSeat(seatId, userId, holdMinutes);
        return ResponseEntity.ok(ApiResponse.success(null, "좌석이 임시 점유되었습니다."));
    }

    @Operation(summary = "좌석 점유 해제", description = "임시 점유된 좌석을 해제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "해제 성공")
    })
    @DeleteMapping("/{seatId}/hold")
    public ResponseEntity<ApiResponse<Void>> releaseSeat(
            @Parameter(description = "좌석 ID", required = true) @PathVariable UUID seatId) {
        seatService.releaseSeat(seatId);
        return ResponseEntity.ok(ApiResponse.success(null, "좌석 점유가 해제되었습니다."));
    }
}
