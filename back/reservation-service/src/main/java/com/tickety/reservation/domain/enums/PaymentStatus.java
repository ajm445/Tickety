package com.tickety.reservation.domain.enums;

public enum PaymentStatus {
    PENDING,          // 결제 대기
    PROCESSING,       // 결제 처리 중
    COMPLETED,        // 결제 완료
    FAILED,           // 결제 실패
    REFUNDED,         // 전액 환불
    PARTIAL_REFUNDED  // 부분 환불
}
