package com.tickety.reservation.domain.entity;

import com.tickety.reservation.domain.enums.PaymentMethod;
import com.tickety.reservation.domain.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "결제 엔티티")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "결제 ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    @Schema(description = "예약")
    private Reservation reservation;

    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    @Schema(description = "사용자 ID")
    private UUID userId;

    @Column(name = "payment_number", nullable = false, unique = true, length = 30)
    @Schema(description = "결제 번호", example = "PAY20251231120000-1234")
    private String paymentNumber;

    @Column(nullable = false)
    @Schema(description = "결제 금액", example = "150000")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "결제 방법", example = "CREDIT_CARD")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "결제 상태", example = "PENDING")
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "pg_transaction_id", length = 100)
    @Schema(description = "PG사 거래 ID")
    private String pgTransactionId;

    @Column(name = "pg_provider", length = 50)
    @Schema(description = "PG사 제공자", example = "TOSS")
    private String pgProvider;

    @Column(name = "paid_at")
    @Schema(description = "결제 완료 시간")
    private OffsetDateTime paidAt;

    @Column(name = "failed_at")
    @Schema(description = "결제 실패 시간")
    private OffsetDateTime failedAt;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    @Schema(description = "결제 실패 사유")
    private String failureReason;

    @Column(name = "refunded_amount")
    @Builder.Default
    @Schema(description = "환불 금액", example = "0")
    private Integer refundedAmount = 0;

    @Column(name = "refunded_at")
    @Schema(description = "환불 시간")
    private OffsetDateTime refundedAt;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    @Schema(description = "환불 사유")
    private String refundReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Schema(description = "추가 메타데이터")
    private Map<String, Object> metadata;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public void complete(String pgTransactionId) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Cannot complete payment in current status: " + this.status);
        }
        this.status = PaymentStatus.COMPLETED;
        this.pgTransactionId = pgTransactionId;
        this.paidAt = OffsetDateTime.now();
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.failedAt = OffsetDateTime.now();
    }

    public void refund(int refundAmount, String reason) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded.");
        }
        if (refundAmount > this.amount - this.refundedAmount) {
            throw new IllegalStateException("Refund amount exceeds remaining amount.");
        }

        this.refundedAmount += refundAmount;
        this.refundReason = reason;
        this.refundedAt = OffsetDateTime.now();

        if (this.refundedAmount.equals(this.amount)) {
            this.status = PaymentStatus.REFUNDED;
        } else {
            this.status = PaymentStatus.PARTIAL_REFUNDED;
        }
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
}
