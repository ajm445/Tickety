package com.tickety.payment.client.dto;

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
public class ReservationDto {

    private UUID id;
    private String reservationNumber;
    private UUID userId;
    private UUID concertId;
    private String concertTitle;
    private String status;  // PENDING, CONFIRMED, CANCELLED, EXPIRED
    private Integer totalAmount;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private OffsetDateTime confirmedAt;
}
