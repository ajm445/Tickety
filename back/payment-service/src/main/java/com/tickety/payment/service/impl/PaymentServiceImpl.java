package com.tickety.payment.service.impl;

import com.tickety.payment.client.ReservationClient;
import com.tickety.payment.client.dto.ApiResponseWrapper;
import com.tickety.payment.client.dto.ReservationDto;
import com.tickety.payment.domain.entity.Payment;
import com.tickety.payment.domain.enums.PaymentStatus;
import com.tickety.payment.dto.request.PaymentRequest;
import com.tickety.payment.dto.request.RefundRequest;
import com.tickety.payment.dto.response.PaymentResponse;
import com.tickety.payment.exception.PaymentAlreadyExistsException;
import com.tickety.payment.exception.PaymentNotFoundException;
import com.tickety.payment.repository.PaymentRepository;
import com.tickety.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationClient reservationClient;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request, UUID userId) {
        // Check if payment already exists for this reservation
        boolean exists = paymentRepository.existsByReservationIdAndStatusIn(
                request.getReservationId(),
                List.of(PaymentStatus.PENDING, PaymentStatus.PROCESSING, PaymentStatus.COMPLETED)
        );
        if (exists) {
            throw new PaymentAlreadyExistsException(request.getReservationId());
        }

        // Get reservation info from reservation service
        ApiResponseWrapper<ReservationDto> response = reservationClient.getReservationById(request.getReservationId());

        if (!response.isSuccess() || response.getData() == null) {
            throw new IllegalStateException("Failed to get reservation info: " + response.getMessage());
        }

        ReservationDto reservation = response.getData();

        // Validate reservation status
        if (!"PENDING".equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation is not in PENDING status.");
        }

        // Validate reservation ownership
        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalStateException("You can only pay for your own reservations.");
        }

        // Create payment
        Payment payment = Payment.create(
                request.getReservationId(),
                userId,
                reservation.getTotalAmount(),
                request.getMethod()
        );
        payment.setPaymentNumber(generatePaymentNumber());

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment {} created for reservation {} by user {}",
                savedPayment.getPaymentNumber(), request.getReservationId(), userId);

        return PaymentResponse.from(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // Start processing
        payment.startProcessing();

        // Simulate PG processing (in real scenario, this would call actual PG API)
        try {
            // Simulate PG response
            String pgTransactionId = "PG" + System.currentTimeMillis();
            String pgProvider = getProviderName(payment.getMethod());

            // Complete payment
            payment.complete(pgTransactionId, pgProvider);

            // Confirm reservation via Feign
            ApiResponseWrapper<ReservationDto> response = reservationClient.confirmReservation(payment.getReservationId());
            if (!response.isSuccess()) {
                log.warn("Failed to confirm reservation {}: {}", payment.getReservationId(), response.getMessage());
                // In a real scenario, we might need to handle this with a saga pattern
            }

            log.info("Payment {} completed successfully. PG Transaction: {}",
                    payment.getPaymentNumber(), pgTransactionId);

        } catch (Exception e) {
            payment.fail(e.getMessage());
            log.error("Payment {} failed: {}", payment.getPaymentNumber(), e.getMessage());
        }

        return PaymentResponse.from(payment);
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public PaymentResponse getPaymentByNumber(String paymentNumber) {
        return paymentRepository.findByPaymentNumber(paymentNumber)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new PaymentNotFoundException(paymentNumber));
    }

    @Override
    public PaymentResponse getPaymentByReservationId(UUID reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found for reservation: " + reservationId));
    }

    @Override
    public List<PaymentResponse> getPaymentsByUserId(UUID userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(UUID paymentId, RefundRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (!payment.isRefundable()) {
            throw new IllegalStateException("Payment cannot be refunded in current status: " + payment.getStatus());
        }

        payment.refund(request.getAmount(), request.getReason());

        log.info("Payment {} refunded. Amount: {}, Reason: {}",
                payment.getPaymentNumber(), request.getAmount(), request.getReason());

        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (payment.getStatus() != PaymentStatus.PENDING && payment.getStatus() != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Only pending or processing payments can be cancelled.");
        }

        payment.fail("Cancelled by user");

        log.info("Payment {} cancelled", payment.getPaymentNumber());

        return PaymentResponse.from(payment);
    }

    private String generatePaymentNumber() {
        String dateStr = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", new Random().nextInt(1000000));
        return "PAY" + dateStr + "-" + randomStr;
    }

    private String getProviderName(com.tickety.payment.domain.enums.PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD, DEBIT_CARD -> "NICE_PAY";
            case BANK_TRANSFER -> "TOSS_PAYMENTS";
            case KAKAO_PAY -> "KAKAO";
            case NAVER_PAY -> "NAVER";
            case TOSS_PAY -> "TOSS";
        };
    }
}
