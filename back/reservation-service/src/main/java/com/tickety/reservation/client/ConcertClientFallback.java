package com.tickety.reservation.client;

import com.tickety.reservation.client.dto.ApiResponseWrapper;
import com.tickety.reservation.client.dto.ConcertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ConcertClientFallback implements ConcertClient {

    @Override
    public ApiResponseWrapper<ConcertDto> getConcertById(UUID concertId) {
        log.warn("Fallback: Concert service is unavailable. ConcertId: {}", concertId);
        return new ApiResponseWrapper<>(false, null, "Concert service is currently unavailable");
    }
}
