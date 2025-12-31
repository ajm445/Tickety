package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "예약 엔티티")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "예약 ID")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    @Schema(description = "사용자 ID")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    @Schema(description = "공연")
    private Concert concert;

    @Column(name = "reservation_number", nullable = false, unique = true, length = 20)
    @Schema(description = "예약 번호", example = "TKT20251231-123456")
    private String reservationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "예약 상태", example = "PENDING")
    @Builder.Default
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "total_amount", nullable = false)
    @Schema(description = "총 결제 금액", example = "300000")
    private Integer totalAmount;

    @Column(name = "expires_at")
    @Schema(description = "예약 만료 시간")
    private OffsetDateTime expiresAt;

    @Column(name = "confirmed_at")
    @Schema(description = "예약 확정 시간")
    private OffsetDateTime confirmedAt;

    @Column(name = "cancelled_at")
    @Schema(description = "예약 취소 시간")
    private OffsetDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    @Schema(description = "취소 사유")
    private String cancellationReason;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationSeat> reservationSeats = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public static Reservation create(UUID userId, Concert concert, int expirationMinutes) {
        return Reservation.builder()
                .userId(userId)
                .concert(concert)
                .status(ReservationStatus.PENDING)
                .totalAmount(0)
                .expiresAt(OffsetDateTime.now().plusMinutes(expirationMinutes))
                .build();
    }

    public void addSeat(ReservationSeat reservationSeat) {
        this.reservationSeats.add(reservationSeat);
        this.totalAmount += reservationSeat.getPrice();
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be confirmed.");
        }
        if (OffsetDateTime.now().isAfter(this.expiresAt)) {
            throw new IllegalStateException("Reservation has expired.");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.confirmedAt = OffsetDateTime.now();
    }

    public void cancel(String reason) {
        if (this.status == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed reservations cannot be cancelled directly.");
        }
        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = OffsetDateTime.now();
        this.cancellationReason = reason;
    }

    public void expire() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.EXPIRED;
        }
    }

    public boolean isExpired() {
        return this.status == ReservationStatus.PENDING &&
               this.expiresAt != null &&
               OffsetDateTime.now().isAfter(this.expiresAt);
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}
