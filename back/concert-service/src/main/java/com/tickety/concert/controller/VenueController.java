package com.tickety.concert.controller;

import com.tickety.concert.dto.request.VenueCreateRequest;
import com.tickety.concert.dto.response.ApiResponse;
import com.tickety.concert.dto.response.VenueResponse;
import com.tickety.concert.service.VenueService;
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
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venue", description = "공연장 API")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    @Operation(summary = "공연장 목록 조회", description = "모든 공연장 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getAllVenues() {
        log.info("Get all venues request");
        List<VenueResponse> venues = venueService.getAllVenues();
        return ResponseEntity.ok(ApiResponse.success(venues));
    }

    @GetMapping("/{venueId}")
    @Operation(summary = "공연장 상세 조회", description = "특정 공연장의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<VenueResponse>> getVenueById(
            @PathVariable UUID venueId) {
        log.info("Get venue by ID: {}", venueId);
        VenueResponse venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok(ApiResponse.success(venue));
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "도시별 공연장 조회", description = "특정 도시의 공연장 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getVenuesByCity(
            @PathVariable String city) {
        log.info("Get venues by city: {}", city);
        List<VenueResponse> venues = venueService.getVenuesByCity(city);
        return ResponseEntity.ok(ApiResponse.success(venues));
    }

    @PostMapping
    @Operation(summary = "공연장 등록", description = "새로운 공연장을 등록합니다. (관리자)")
    public ResponseEntity<ApiResponse<VenueResponse>> createVenue(
            @Valid @RequestBody VenueCreateRequest request) {
        log.info("Create venue request: {}", request.getName());
        VenueResponse venue = venueService.createVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(venue, "Venue created successfully"));
    }

    @PutMapping("/{venueId}")
    @Operation(summary = "공연장 수정", description = "공연장 정보를 수정합니다. (관리자)")
    public ResponseEntity<ApiResponse<VenueResponse>> updateVenue(
            @PathVariable UUID venueId,
            @Valid @RequestBody VenueCreateRequest request) {
        log.info("Update venue request: {}", venueId);
        VenueResponse venue = venueService.updateVenue(venueId, request);
        return ResponseEntity.ok(ApiResponse.success(venue, "Venue updated successfully"));
    }

    @DeleteMapping("/{venueId}")
    @Operation(summary = "공연장 삭제", description = "공연장을 삭제합니다. (관리자)")
    public ResponseEntity<ApiResponse<Void>> deleteVenue(
            @PathVariable UUID venueId) {
        log.info("Delete venue request: {}", venueId);
        venueService.deleteVenue(venueId);
        return ResponseEntity.ok(ApiResponse.success(null, "Venue deleted successfully"));
    }
}
