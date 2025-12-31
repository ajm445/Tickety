package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.ConcertStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "concerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "공연 엔티티")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "공연 ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @Schema(description = "공연장")
    private Venue venue;

    @Column(nullable = false, length = 255)
    @Schema(description = "공연 제목", example = "2025 아이유 콘서트")
    private String title;

    @Column(nullable = false, length = 255)
    @Schema(description = "아티스트", example = "아이유")
    private String artist;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "설명")
    private String description;

    @Column(name = "concert_date", nullable = false)
    @Schema(description = "공연 일시")
    private OffsetDateTime concertDate;

    @Column(name = "booking_start_at", nullable = false)
    @Schema(description = "예매 시작 일시")
    private OffsetDateTime bookingStartAt;

    @Column(name = "booking_end_at", nullable = false)
    @Schema(description = "예매 종료 일시")
    private OffsetDateTime bookingEndAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "공연 상태", example = "SCHEDULED")
    @Builder.Default
    private ConcertStatus status = ConcertStatus.SCHEDULED;

    @Column(name = "poster_url", columnDefinition = "TEXT")
    @Schema(description = "포스터 URL")
    private String posterUrl;

    @Column(name = "price_min", nullable = false)
    @Schema(description = "최소 가격", example = "50000")
    private Integer priceMin;

    @Column(name = "price_max", nullable = false)
    @Schema(description = "최대 가격", example = "150000")
    private Integer priceMax;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public boolean isBookingOpen() {
        OffsetDateTime now = OffsetDateTime.now();
        return status == ConcertStatus.OPEN &&
               now.isAfter(bookingStartAt) &&
               now.isBefore(bookingEndAt);
    }

    public void openBooking() {
        if (this.status != ConcertStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled concerts can be opened for booking.");
        }
        this.status = ConcertStatus.OPEN;
    }

    public void markSoldOut() {
        this.status = ConcertStatus.SOLD_OUT;
    }

    public void cancel() {
        this.status = ConcertStatus.CANCELLED;
    }

    public void complete() {
        this.status = ConcertStatus.COMPLETED;
    }
}
