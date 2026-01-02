package com.tickety.payment.domain.entity;

import com.tickety.payment.domain.enums.PaymentMethod;
import com.tickety.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID reservationId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String paymentNumber;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    // PG 거래 정보
    private String pgTransactionId;
    private String pgProvider;

    // 결제 완료 정보
    private OffsetDateTime paidAt;

    // 결제 실패 정보
    private OffsetDateTime failedAt;
    private String failureReason;

    // 환불 정보
    @Builder.Default
    private Integer refundedAmount = 0;
    private OffsetDateTime refundedAt;
    private String refundReason;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    public static Payment create(UUID reservationId, UUID userId, Integer amount, PaymentMethod method) {
        return Payment.builder()
                .reservationId(reservationId)
                .userId(userId)
                .amount(amount)
                .method(method)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void startProcessing() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be processed.");
        }
        this.status = PaymentStatus.PROCESSING;
    }

    public void complete(String pgTransactionId, String pgProvider) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Only processing payments can be completed.");
        }
        this.status = PaymentStatus.COMPLETED;
        this.pgTransactionId = pgTransactionId;
        this.pgProvider = pgProvider;
        this.paidAt = OffsetDateTime.now();
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.failedAt = OffsetDateTime.now();
    }

    public void refund(Integer refundAmount, String reason) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded.");
        }

        int totalRefunded = this.refundedAmount + refundAmount;
        if (totalRefunded > this.amount) {
            throw new IllegalStateException("Refund amount exceeds payment amount.");
        }

        this.refundedAmount = totalRefunded;
        this.refundReason = reason;
        this.refundedAt = OffsetDateTime.now();

        if (totalRefunded == this.amount) {
            this.status = PaymentStatus.REFUNDED;
        } else {
            this.status = PaymentStatus.PARTIAL_REFUNDED;
        }
    }

    public boolean isRefundable() {
        return this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.PARTIAL_REFUNDED;
    }
}
