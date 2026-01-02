package com.tickety.concert.config;

import com.tickety.concert.domain.entity.Concert;
import com.tickety.concert.domain.entity.Venue;
import com.tickety.concert.domain.enums.ConcertStatus;
import com.tickety.concert.repository.ConcertRepository;
import com.tickety.concert.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;

    @Override
    public void run(String... args) {
        if (venueRepository.count() > 0) {
            log.info("Sample data already exists. Skipping initialization.");
            return;
        }

        log.info("Initializing sample data...");

        // Create Venues
        Venue venue1 = venueRepository.save(Venue.builder()
                .name("올림픽공원 체조경기장")
                .address("서울특별시 송파구 올림픽로 424")
                .city("서울")
                .totalSeats(15000)
                .description("올림픽공원 내 위치한 대규모 공연장")
                .build());

        Venue venue2 = venueRepository.save(Venue.builder()
                .name("고척스카이돔")
                .address("서울특별시 구로구 경인로 430")
                .city("서울")
                .totalSeats(25000)
                .description("국내 최초 돔 구장")
                .build());

        Venue venue3 = venueRepository.save(Venue.builder()
                .name("KSPO DOME")
                .address("서울특별시 송파구 올림픽로 25")
                .city("서울")
                .totalSeats(15000)
                .description("핸드볼 경기장으로도 불리는 다목적 공연장")
                .build());

        Venue venue4 = venueRepository.save(Venue.builder()
                .name("부산 사직실내체육관")
                .address("부산광역시 동래구 사직로 45")
                .city("부산")
                .totalSeats(8000)
                .description("부산 지역 대표 공연장")
                .build());

        // Create Concerts
        OffsetDateTime now = OffsetDateTime.now();

        concertRepository.save(Concert.builder()
                .venue(venue1)
                .title("2025 아이유 콘서트 'The Winning'")
                .artist("아이유 (IU)")
                .description("아이유의 2025년 전국 투어 콘서트")
                .concertDate(now.plusDays(30))
                .bookingStartAt(now.minusDays(7))
                .bookingEndAt(now.plusDays(29))
                .status(ConcertStatus.OPEN)
                .posterUrl("https://via.placeholder.com/400x600/667eea/ffffff?text=IU+Concert")
                .priceMin(99000)
                .priceMax(176000)
                .build());

        concertRepository.save(Concert.builder()
                .venue(venue2)
                .title("BTS WORLD TOUR 'YET TO COME'")
                .artist("BTS")
                .description("방탄소년단 월드 투어 서울 공연")
                .concertDate(now.plusDays(45))
                .bookingStartAt(now.plusDays(7))
                .bookingEndAt(now.plusDays(44))
                .status(ConcertStatus.SCHEDULED)
                .posterUrl("https://via.placeholder.com/400x600/764ba2/ffffff?text=BTS+Tour")
                .priceMin(132000)
                .priceMax(220000)
                .build());

        concertRepository.save(Concert.builder()
                .venue(venue3)
                .title("2025 뉴이스트 팬미팅")
                .artist("뉴이스트")
                .description("뉴이스트 10주년 기념 팬미팅")
                .concertDate(now.plusDays(20))
                .bookingStartAt(now.minusDays(14))
                .bookingEndAt(now.plusDays(19))
                .status(ConcertStatus.OPEN)
                .posterUrl("https://via.placeholder.com/400x600/f093fb/ffffff?text=NUEST+Fanmeeting")
                .priceMin(77000)
                .priceMax(110000)
                .build());

        concertRepository.save(Concert.builder()
                .venue(venue1)
                .title("BLACKPINK WORLD TOUR [BORN PINK]")
                .artist("BLACKPINK")
                .description("블랙핑크 월드 투어 앙코르 공연")
                .concertDate(now.plusDays(60))
                .bookingStartAt(now.plusDays(14))
                .bookingEndAt(now.plusDays(59))
                .status(ConcertStatus.SCHEDULED)
                .posterUrl("https://via.placeholder.com/400x600/f5576c/ffffff?text=BLACKPINK+Tour")
                .priceMin(154000)
                .priceMax(264000)
                .build());

        concertRepository.save(Concert.builder()
                .venue(venue4)
                .title("임영웅 전국 투어 콘서트")
                .artist("임영웅")
                .description("임영웅 2025 전국 투어 - 부산")
                .concertDate(now.plusDays(25))
                .bookingStartAt(now.minusDays(3))
                .bookingEndAt(now.plusDays(24))
                .status(ConcertStatus.OPEN)
                .posterUrl("https://via.placeholder.com/400x600/43e97b/ffffff?text=Youngwoong+Tour")
                .priceMin(88000)
                .priceMax(143000)
                .build());

        log.info("Sample data initialized: {} venues, {} concerts",
                venueRepository.count(), concertRepository.count());
    }
}
