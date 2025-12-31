package com.tickety.reservation.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation_seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "예약-좌석 연결 엔티티")
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "예약-좌석 ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    @Schema(description = "예약")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    @Schema(description = "좌석")
    private Seat seat;

    @Column(nullable = false)
    @Schema(description = "좌석 가격 (예약 시점 가격)", example = "150000")
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public static ReservationSeat create(Reservation reservation, Seat seat) {
        return ReservationSeat.builder()
                .reservation(reservation)
                .seat(seat)
                .price(seat.getPrice())
                .build();
    }
}
