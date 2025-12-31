package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.SeatGrade;
import com.tickety.reservation.domain.enums.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "좌석 엔티티")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "좌석 ID", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "공연 ID", example = "1")
    private Long concertId;

    @Column(nullable = false, length = 10)
    @Schema(description = "좌석 번호", example = "A-1")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "좌석 등급", example = "VIP")
    private SeatGrade seatGrade;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "좌석 가격", example = "150000.00")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "좌석 상태", example = "AVAILABLE")
    private SeatStatus status;

    @Version
    private Long version;

    public void reserve() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("This seat is not available for reservation.");
        }
        this.status = SeatStatus.RESERVED;
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
    }
}
