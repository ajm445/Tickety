package com.tickety.payment.dto.response;

import com.tickety.payment.domain.entity.Payment;
import com.tickety.payment.domain.enums.PaymentMethod;
import com.tickety.payment.domain.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "결제 응답 DTO")
public class PaymentResponse {

    @Schema(description = "결제 ID")
    private UUID id;

    @Schema(description = "예약 ID")
    private UUID reservationId;

    @Schema(description = "사용자 ID")
    private UUID userId;

    @Schema(description = "결제 번호")
    private String paymentNumber;

    @Schema(description = "결제 금액")
    private Integer amount;

    @Schema(description = "결제 수단")
    private PaymentMethod method;

    @Schema(description = "결제 상태")
    private PaymentStatus status;

    @Schema(description = "PG 거래 ID")
    private String pgTransactionId;

    @Schema(description = "PG 제공자")
    private String pgProvider;

    @Schema(description = "결제 완료 시간")
    private OffsetDateTime paidAt;

    @Schema(description = "결제 실패 시간")
    private OffsetDateTime failedAt;

    @Schema(description = "실패 사유")
    private String failureReason;

    @Schema(description = "환불 금액")
    private Integer refundedAmount;

    @Schema(description = "환불 시간")
    private OffsetDateTime refundedAt;

    @Schema(description = "환불 사유")
    private String refundReason;

    @Schema(description = "생성 시간")
    private OffsetDateTime createdAt;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .reservationId(payment.getReservationId())
                .userId(payment.getUserId())
                .paymentNumber(payment.getPaymentNumber())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .pgTransactionId(payment.getPgTransactionId())
                .pgProvider(payment.getPgProvider())
                .paidAt(payment.getPaidAt())
                .failedAt(payment.getFailedAt())
                .failureReason(payment.getFailureReason())
                .refundedAmount(payment.getRefundedAmount())
                .refundedAt(payment.getRefundedAt())
                .refundReason(payment.getRefundReason())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
