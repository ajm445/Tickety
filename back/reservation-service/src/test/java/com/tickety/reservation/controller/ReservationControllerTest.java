package com.tickety.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickety.reservation.domain.enums.ReservationStatus;
import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;
import com.tickety.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("POST /api/reservations - 예약 생성 성공")
    void createReservation_Success() throws Exception {
        // given
        ReservationRequest request = ReservationRequest.builder()
                .concertId(1L)
                .seatIds(List.of(1L, 2L))
                .build();

        ReservationResponse response = ReservationResponse.builder()
                .reservationId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        given(reservationService.createReservation(any(ReservationRequest.class), eq(1L)))
                .willReturn(List.of(response));

        // when & then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].reservationId").value(1))
                .andExpect(jsonPath("$.data[0].status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/reservations - 내 예약 목록 조회")
    void getMyReservations_Success() throws Exception {
        // given
        ReservationResponse response = ReservationResponse.builder()
                .reservationId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        given(reservationService.getReservationsByUserId(1L))
                .willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/reservations")
                        .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].userId").value(1));
    }

    @Test
    @DisplayName("GET /api/reservations/{id} - 예약 상세 조회")
    void getReservationById_Success() throws Exception {
        // given
        ReservationResponse response = ReservationResponse.builder()
                .reservationId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        given(reservationService.getReservationById(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reservationId").value(1));
    }

    @Test
    @DisplayName("DELETE /api/reservations/{id} - 예약 취소")
    void cancelReservation_Success() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/reservations/1")
                        .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("예약이 취소되었습니다."));
    }

    @Test
    @DisplayName("POST /api/reservations/{id}/confirm - 예약 확정")
    void confirmReservation_Success() throws Exception {
        // given
        ReservationResponse response = ReservationResponse.builder()
                .reservationId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        given(reservationService.confirmReservation(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/reservations/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("POST /api/reservations - 유효성 검사 실패")
    void createReservation_ValidationFail() throws Exception {
        // given
        ReservationRequest request = ReservationRequest.builder()
                .concertId(null)
                .seatIds(List.of())
                .build();

        // when & then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
