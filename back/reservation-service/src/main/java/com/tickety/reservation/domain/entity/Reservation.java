package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "예약 엔티티")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "예약 ID", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "좌석 ID", example = "1")
    private Long seatId;

    @Column(nullable = false)
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "예약 상태", example = "PENDING")
    private ReservationStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Schema(description = "예약 시간")
    private LocalDateTime reservedAt;

    @Column(nullable = false)
    @Schema(description = "예약 만료 시간")
    private LocalDateTime expiredAt;

    public static Reservation create(Long seatId, Long userId, int expirationMinutes) {
        return Reservation.builder()
                .seatId(seatId)
                .userId(userId)
                .status(ReservationStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be confirmed.");
        }
        if (LocalDateTime.now().isAfter(this.expiredAt)) {
            throw new IllegalStateException("Reservation has expired.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed reservations cannot be cancelled directly.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt) && this.status == ReservationStatus.PENDING;
    }
}
