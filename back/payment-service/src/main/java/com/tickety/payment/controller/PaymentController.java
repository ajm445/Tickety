package com.tickety.payment.controller;

import com.tickety.payment.dto.request.PaymentRequest;
import com.tickety.payment.dto.request.RefundRequest;
import com.tickety.payment.dto.response.ApiResponse;
import com.tickety.payment.dto.response.PaymentResponse;
import com.tickety.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "결제 생성", description = "예약에 대한 결제를 생성합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("Create payment request for reservation: {} by user: {}", request.getReservationId(), userId);
        PaymentResponse payment = paymentService.createPayment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(payment, "결제가 생성되었습니다."));
    }

    @PostMapping("/{paymentId}/process")
    @Operation(summary = "결제 처리", description = "생성된 결제를 처리합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Parameter(description = "결제 ID", required = true)
            @PathVariable UUID paymentId) {
        log.info("Process payment request: {}", paymentId);
        PaymentResponse payment = paymentService.processPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(payment, "결제가 처리되었습니다."));
    }

    @GetMapping
    @Operation(summary = "내 결제 목록 조회", description = "현재 사용자의 모든 결제 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments(
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("Get payments for user: {}", userId);
        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "결제 상세 조회", description = "특정 결제의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(
            @Parameter(description = "결제 ID", required = true)
            @PathVariable UUID paymentId) {
        log.info("Get payment by ID: {}", paymentId);
        PaymentResponse payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    @GetMapping("/number/{paymentNumber}")
    @Operation(summary = "결제 번호로 조회", description = "결제 번호로 결제 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByNumber(
            @Parameter(description = "결제 번호", required = true)
            @PathVariable String paymentNumber) {
        log.info("Get payment by number: {}", paymentNumber);
        PaymentResponse payment = paymentService.getPaymentByNumber(paymentNumber);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "예약 ID로 결제 조회", description = "예약 ID로 결제 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByReservationId(
            @Parameter(description = "예약 ID", required = true)
            @PathVariable UUID reservationId) {
        log.info("Get payment by reservation ID: {}", reservationId);
        PaymentResponse payment = paymentService.getPaymentByReservationId(reservationId);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "환불 요청", description = "완료된 결제에 대해 환불을 요청합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @Parameter(description = "결제 ID", required = true)
            @PathVariable UUID paymentId,
            @Valid @RequestBody RefundRequest request) {
        log.info("Refund payment request: {} amount: {}", paymentId, request.getAmount());
        PaymentResponse payment = paymentService.refundPayment(paymentId, request);
        return ResponseEntity.ok(ApiResponse.success(payment, "환불이 처리되었습니다."));
    }

    @DeleteMapping("/{paymentId}")
    @Operation(summary = "결제 취소", description = "대기 중이거나 처리 중인 결제를 취소합니다.")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @Parameter(description = "결제 ID", required = true)
            @PathVariable UUID paymentId) {
        log.info("Cancel payment request: {}", paymentId);
        PaymentResponse payment = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(payment, "결제가 취소되었습니다."));
    }
}
