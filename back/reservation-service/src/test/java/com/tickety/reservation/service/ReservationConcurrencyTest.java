package com.tickety.reservation.service;

import com.tickety.reservation.domain.entity.Seat;
import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.repository.ReservationRepository;
import com.tickety.reservation.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Seat testSeat;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        seatRepository.deleteAll();

        testSeat = seatRepository.save(Seat.builder()
                .concertId(1L)
                .seatNumber("A-1")
                .seatGrade(SeatGrade.VIP)
                .price(BigDecimal.valueOf(150000))
                .status(SeatStatus.AVAILABLE)
                .version(0L)
                .build());
    }

    @Test
    @DisplayName("100명의 사용자가 동시에 같은 좌석을 예약할 때 한 명만 성공")
    void concurrentReservation_OnlyOneSucceeds() throws InterruptedException {
        // given
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ReservationRequest request = ReservationRequest.builder()
                .concertId(1L)
                .seatIds(List.of(testSeat.getId()))
                .build();

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            final long userId = i + 1;
            executorService.submit(() -> {
                try {
                    reservationService.createReservation(request, userId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(99);

        // Verify seat status
        Seat updatedSeat = seatRepository.findById(testSeat.getId()).orElseThrow();
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.RESERVED);

        // Verify only one reservation exists
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount).isEqualTo(1);
    }

    @Test
    @DisplayName("다중 좌석 동시 예약 시 데이터 정합성 유지")
    void concurrentReservation_MultipleSeats_DataIntegrity() throws InterruptedException {
        // given
        Seat seat2 = seatRepository.save(Seat.builder()
                .concertId(1L)
                .seatNumber("A-2")
                .seatGrade(SeatGrade.VIP)
                .price(BigDecimal.valueOf(150000))
                .status(SeatStatus.AVAILABLE)
                .version(0L)
                .build());

        int numberOfThreads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        // Half try to reserve seat1, half try to reserve seat2
        for (int i = 0; i < numberOfThreads; i++) {
            final long userId = i + 1;
            final Long seatId = (i % 2 == 0) ? testSeat.getId() : seat2.getId();

            executorService.submit(() -> {
                try {
                    ReservationRequest request = ReservationRequest.builder()
                            .concertId(1L)
                            .seatIds(List.of(seatId))
                            .build();
                    reservationService.createReservation(request, userId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // Expected for concurrent access
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then: Only 2 successful reservations (one per seat)
        assertThat(successCount.get()).isEqualTo(2);

        // Verify both seats are reserved
        Seat updatedSeat1 = seatRepository.findById(testSeat.getId()).orElseThrow();
        Seat updatedSeat2 = seatRepository.findById(seat2.getId()).orElseThrow();
        assertThat(updatedSeat1.getStatus()).isEqualTo(SeatStatus.RESERVED);
        assertThat(updatedSeat2.getStatus()).isEqualTo(SeatStatus.RESERVED);

        // Verify exactly 2 reservations exist
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount).isEqualTo(2);
    }
}
