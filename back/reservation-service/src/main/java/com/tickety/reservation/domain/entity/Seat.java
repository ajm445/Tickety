package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "좌석 엔티티")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "좌석 ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    @Schema(description = "공연")
    private Concert concert;

    @Column(nullable = false, length = 50)
    @Schema(description = "섹션", example = "VIP")
    private String section;

    @Column(name = "row_number", nullable = false, length = 10)
    @Schema(description = "열 번호", example = "A")
    private String rowNumber;

    @Column(name = "seat_number", nullable = false)
    @Schema(description = "좌석 번호", example = "1")
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "좌석 등급", example = "VIP")
    private SeatGrade grade;

    @Column(nullable = false)
    @Schema(description = "좌석 가격", example = "150000")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "좌석 상태", example = "AVAILABLE")
    @Builder.Default
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "held_until")
    @Schema(description = "임시 점유 만료 시간")
    private OffsetDateTime heldUntil;

    @Column(name = "held_by", columnDefinition = "uuid")
    @Schema(description = "임시 점유한 사용자 ID")
    private UUID heldBy;

    @Version
    @Column(nullable = false)
    @Builder.Default
    private Integer version = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public String getFullSeatNumber() {
        return section + "-" + rowNumber + "-" + seatNumber;
    }

    public void hold(UUID userId, int holdMinutes) {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("This seat is not available for holding.");
        }
        this.status = SeatStatus.HELD;
        this.heldBy = userId;
        this.heldUntil = OffsetDateTime.now().plusMinutes(holdMinutes);
    }

    public void reserve() {
        if (this.status != SeatStatus.AVAILABLE && this.status != SeatStatus.HELD) {
            throw new IllegalStateException("This seat is not available for reservation.");
        }
        this.status = SeatStatus.RESERVED;
        this.heldBy = null;
        this.heldUntil = null;
    }

    public void confirmSale() {
        if (this.status != SeatStatus.RESERVED) {
            throw new IllegalStateException("This seat is not reserved.");
        }
        this.status = SeatStatus.SOLD;
    }

    public void release() {
        if (this.status == SeatStatus.SOLD) {
            throw new IllegalStateException("Sold seats cannot be released.");
        }
        this.status = SeatStatus.AVAILABLE;
        this.heldBy = null;
        this.heldUntil = null;
    }

    public boolean isHoldExpired() {
        return this.status == SeatStatus.HELD &&
               this.heldUntil != null &&
               OffsetDateTime.now().isAfter(this.heldUntil);
    }
}
