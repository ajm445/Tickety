package com.tickety.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "환불 요청 DTO")
public class RefundRequest {

    @NotNull(message = "환불 금액은 필수입니다.")
    @Min(value = 1, message = "환불 금액은 1원 이상이어야 합니다.")
    @Schema(description = "환불 금액")
    private Integer amount;

    @NotBlank(message = "환불 사유는 필수입니다.")
    @Schema(description = "환불 사유")
    private String reason;
}
