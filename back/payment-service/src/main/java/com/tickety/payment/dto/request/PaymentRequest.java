package com.tickety.payment.dto.request;

import com.tickety.payment.domain.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "결제 요청 DTO")
public class PaymentRequest {

    @NotNull(message = "예약 ID는 필수입니다.")
    @Schema(description = "예약 ID")
    private UUID reservationId;

    @NotNull(message = "결제 수단은 필수입니다.")
    @Schema(description = "결제 수단")
    private PaymentMethod method;
}
