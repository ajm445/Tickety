package com.tickety.concert.controller;

import com.tickety.concert.dto.request.ConcertCreateRequest;
import com.tickety.concert.dto.response.ApiResponse;
import com.tickety.concert.dto.response.ConcertResponse;
import com.tickety.concert.service.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
@Tag(name = "Concert", description = "공연 API")
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping
    @Operation(summary = "공연 목록 조회", description = "예매 가능한 공연 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<ConcertResponse>>> getConcerts(
            @RequestParam(required = false) String keyword) {
        log.info("Get concerts request, keyword: {}", keyword);

        List<ConcertResponse> concerts;
        if (keyword != null && !keyword.isBlank()) {
            concerts = concertService.searchConcerts(keyword);
        } else {
            concerts = concertService.getOpenConcerts();
        }

        return ResponseEntity.ok(ApiResponse.success(concerts));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 공연 목록 조회", description = "모든 공연 목록을 조회합니다. (관리자)")
    public ResponseEntity<ApiResponse<List<ConcertResponse>>> getAllConcerts() {
        log.info("Get all concerts request");
        List<ConcertResponse> concerts = concertService.getAllConcerts();
        return ResponseEntity.ok(ApiResponse.success(concerts));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "다가오는 공연 조회", description = "현재 시점 이후의 공연 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<ConcertResponse>>> getUpcomingConcerts() {
        log.info("Get upcoming concerts request");
        List<ConcertResponse> concerts = concertService.getUpcomingConcerts();
        return ResponseEntity.ok(ApiResponse.success(concerts));
    }

    @GetMapping("/{concertId}")
    @Operation(summary = "공연 상세 조회", description = "특정 공연의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ConcertResponse>> getConcertById(
            @PathVariable UUID concertId) {
        log.info("Get concert by ID: {}", concertId);
        ConcertResponse concert = concertService.getConcertById(concertId);
        return ResponseEntity.ok(ApiResponse.success(concert));
    }

    @PostMapping
    @Operation(summary = "공연 등록", description = "새로운 공연을 등록합니다. (관리자)")
    public ResponseEntity<ApiResponse<ConcertResponse>> createConcert(
            @Valid @RequestBody ConcertCreateRequest request) {
        log.info("Create concert request: {}", request.getTitle());
        ConcertResponse concert = concertService.createConcert(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(concert, "Concert created successfully"));
    }

    @PatchMapping("/{concertId}/status")
    @Operation(summary = "공연 상태 변경", description = "공연 상태를 변경합니다. (관리자)")
    public ResponseEntity<ApiResponse<ConcertResponse>> updateConcertStatus(
            @PathVariable UUID concertId,
            @RequestParam String status) {
        log.info("Update concert status request: {} -> {}", concertId, status);
        ConcertResponse concert = concertService.updateConcertStatus(concertId, status);
        return ResponseEntity.ok(ApiResponse.success(concert, "Concert status updated"));
    }

    @DeleteMapping("/{concertId}")
    @Operation(summary = "공연 삭제", description = "공연을 삭제합니다. (관리자)")
    public ResponseEntity<ApiResponse<Void>> deleteConcert(
            @PathVariable UUID concertId) {
        log.info("Delete concert request: {}", concertId);
        concertService.deleteConcert(concertId);
        return ResponseEntity.ok(ApiResponse.success(null, "Concert deleted successfully"));
    }
}
