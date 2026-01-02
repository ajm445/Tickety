package com.tickety.payment.client;

import com.tickety.payment.client.dto.ApiResponseWrapper;
import com.tickety.payment.client.dto.ReservationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ReservationClientFallback implements ReservationClient {

    @Override
    public ApiResponseWrapper<ReservationDto> getReservationById(UUID reservationId) {
        log.warn("Fallback: Reservation service is unavailable. ReservationId: {}", reservationId);
        return new ApiResponseWrapper<>(false, null, "Reservation service is currently unavailable");
    }

    @Override
    public ApiResponseWrapper<ReservationDto> confirmReservation(UUID reservationId) {
        log.warn("Fallback: Reservation service is unavailable. Cannot confirm reservation: {}", reservationId);
        return new ApiResponseWrapper<>(false, null, "Reservation service is currently unavailable");
    }
}
