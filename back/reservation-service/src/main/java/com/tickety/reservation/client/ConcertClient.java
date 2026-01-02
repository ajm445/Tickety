package com.tickety.reservation.client;

import com.tickety.reservation.client.dto.ApiResponseWrapper;
import com.tickety.reservation.client.dto.ConcertDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "concert-service",
        fallback = ConcertClientFallback.class
)
public interface ConcertClient {

    @GetMapping("/api/concerts/{concertId}")
    ApiResponseWrapper<ConcertDto> getConcertById(@PathVariable("concertId") UUID concertId);
}
