package com.tickety.reservation.service;

import com.tickety.reservation.domain.entity.Reservation;
import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.ReservationStatus;
import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;
import com.tickety.reservation.exception.SeatAlreadyReservedException;
import com.tickety.reservation.repository.ReservationRepository;
import com.tickety.reservation.repository.SeatRepository;
import com.tickety.reservation.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Seat availableSeat;
    private Seat reservedSeat;
    private Reservation pendingReservation;

    @BeforeEach
    void setUp() {
        availableSeat = Seat.builder()
                .id(1L)
                .concertId(1L)
                .seatNumber("A-1")
                .seatGrade(SeatGrade.VIP)
                .price(BigDecimal.valueOf(150000))
                .status(SeatStatus.AVAILABLE)
                .version(0L)
                .build();

        reservedSeat = Seat.builder()
                .id(2L)
                .concertId(1L)
                .seatNumber("A-2")
                .seatGrade(SeatGrade.VIP)
                .price(BigDecimal.valueOf(150000))
                .status(SeatStatus.RESERVED)
                .version(0L)
                .build();

        pendingReservation = Reservation.builder()
                .id(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    @Test
    @DisplayName("예약 가능한 좌석 예약 성공")
    void createReservation_Success() {
        // given
        ReservationRequest request = ReservationRequest.builder()
                .concertId(1L)
                .seatIds(List.of(1L))
                .build();

        given(seatRepository.findAllByIdWithPessimisticLock(List.of(1L)))
                .willReturn(List.of(availableSeat));
        given(reservationRepository.save(any(Reservation.class)))
                .willReturn(pendingReservation);

        // when
        List<ReservationResponse> responses = reservationService.createReservation(request, 1L);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getStatus()).isEqualTo(ReservationStatus.PENDING);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("이미 예약된 좌석 예약 시 실패")
    void createReservation_AlreadyReserved_ThrowsException() {
        // given
        ReservationRequest request = ReservationRequest.builder()
                .concertId(1L)
                .seatIds(List.of(2L))
                .build();

        given(seatRepository.findAllByIdWithPessimisticLock(List.of(2L)))
                .willReturn(List.of(reservedSeat));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request, 1L))
                .isInstanceOf(SeatAlreadyReservedException.class);
    }

    @Test
    @DisplayName("사용자 예약 목록 조회 성공")
    void getReservationsByUserId_Success() {
        // given
        given(reservationRepository.findByUserId(1L))
                .willReturn(List.of(pendingReservation));

        // when
        List<ReservationResponse> responses = reservationService.getReservationsByUserId(1L);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 취소 성공")
    void cancelReservation_Success() {
        // given
        given(reservationRepository.findById(1L))
                .willReturn(Optional.of(pendingReservation));
        given(seatRepository.findByIdWithPessimisticLock(1L))
                .willReturn(Optional.of(reservedSeat));

        // when
        reservationService.cancelReservation(1L, 1L);

        // then
        verify(reservationRepository, times(1)).findById(1L);
        verify(seatRepository, times(1)).findByIdWithPessimisticLock(1L);
    }

    @Test
    @DisplayName("다른 사용자의 예약 취소 시 실패")
    void cancelReservation_DifferentUser_ThrowsException() {
        // given
        given(reservationRepository.findById(1L))
                .willReturn(Optional.of(pendingReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelReservation(1L, 999L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You can only cancel your own reservations.");
    }

    @Test
    @DisplayName("예약 확정 성공")
    void confirmReservation_Success() {
        // given
        given(reservationRepository.findById(1L))
                .willReturn(Optional.of(pendingReservation));
        given(seatRepository.findByIdWithPessimisticLock(1L))
                .willReturn(Optional.of(reservedSeat));

        // when
        ReservationResponse response = reservationService.confirmReservation(1L);

        // then
        assertThat(response).isNotNull();
        verify(reservationRepository, times(1)).findById(1L);
    }
}
