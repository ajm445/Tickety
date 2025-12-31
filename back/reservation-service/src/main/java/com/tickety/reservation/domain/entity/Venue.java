package com.tickety.reservation.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "venues")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "공연장 엔티티")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    @Schema(description = "공연장 ID")
    private UUID id;

    @Column(nullable = false, length = 255)
    @Schema(description = "공연장 이름", example = "올림픽공원 체조경기장")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "주소", example = "서울특별시 송파구 올림픽로 424")
    private String address;

    @Column(nullable = false, length = 100)
    @Schema(description = "도시", example = "서울")
    private String city;

    @Column(name = "total_seats", nullable = false)
    @Schema(description = "총 좌석 수", example = "15000")
    private Integer totalSeats;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "설명")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    @Schema(description = "이미지 URL")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
