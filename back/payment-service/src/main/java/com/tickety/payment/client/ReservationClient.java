package com.tickety.payment.client;

import com.tickety.payment.client.dto.ApiResponseWrapper;
import com.tickety.payment.client.dto.ReservationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(
        name = "reservation-service",
        fallback = ReservationClientFallback.class
)
public interface ReservationClient {

    @GetMapping("/api/reservations/{reservationId}")
    ApiResponseWrapper<ReservationDto> getReservationById(@PathVariable("reservationId") UUID reservationId);

    @PostMapping("/api/reservations/{reservationId}/confirm")
    ApiResponseWrapper<ReservationDto> confirmReservation(@PathVariable("reservationId") UUID reservationId);
}
