package com.tickety.reservation.controller;

import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ApiResponse;
import com.tickety.reservation.dto.response.ReservationResponse;
import com.tickety.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "예약 관리 API")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 생성", description = "선택한 좌석들에 대한 예약을 생성합니다. 예약은 10분간 유효합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "좌석이 이미 예약됨")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationRequest request,
            @Parameter(description = "사용자 ID (헤더에서 추출)", required = true)
            @RequestHeader(value = "X-User-Id") UUID userId) {
        ReservationResponse reservation = reservationService.createReservation(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reservation, "예약이 생성되었습니다."));
    }

    @Operation(summary = "내 예약 목록 조회", description = "현재 사용자의 모든 예약 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getMyReservations(
            @Parameter(description = "사용자 ID (헤더에서 추출)", required = true)
            @RequestHeader(value = "X-User-Id") UUID userId) {
        List<ReservationResponse> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @Operation(summary = "예약 상세 조회", description = "특정 예약의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(
            @Parameter(description = "예약 ID", required = true) @PathVariable UUID reservationId) {
        ReservationResponse reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(ApiResponse.success(reservation));
    }

    @Operation(summary = "예약 번호로 조회", description = "예약 번호로 예약 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    @GetMapping("/number/{reservationNumber}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationByNumber(
            @Parameter(description = "예약 번호", required = true) @PathVariable String reservationNumber) {
        ReservationResponse reservation = reservationService.getReservationByNumber(reservationNumber);
        return ResponseEntity.ok(ApiResponse.success(reservation));
    }

    @Operation(summary = "예약 취소", description = "대기 중인 예약을 취소합니다. 확정된 예약은 취소할 수 없습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "취소 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "취소할 수 없는 상태"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(
            @Parameter(description = "예약 ID", required = true) @PathVariable UUID reservationId,
            @Parameter(description = "사용자 ID (헤더에서 추출)", required = true)
            @RequestHeader(value = "X-User-Id") UUID userId) {
        reservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "예약이 취소되었습니다."));
    }

    @Operation(summary = "예약 확정", description = "결제 완료 후 예약을 확정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "확정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "확정할 수 없는 상태"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<ApiResponse<ReservationResponse>> confirmReservation(
            @Parameter(description = "예약 ID", required = true) @PathVariable UUID reservationId) {
        ReservationResponse reservation = reservationService.confirmReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success(reservation, "예약이 확정되었습니다."));
    }
}
