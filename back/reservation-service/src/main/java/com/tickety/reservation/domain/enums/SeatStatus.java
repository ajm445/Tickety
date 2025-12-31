package com.tickety.reservation.domain.enums;

public enum SeatStatus {
    AVAILABLE,   // 예약 가능
    RESERVED,    // 예약됨 (결제 대기)
    SOLD         // 판매 완료
}
