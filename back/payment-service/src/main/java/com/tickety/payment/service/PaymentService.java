package com.tickety.payment.service;

import com.tickety.payment.dto.request.PaymentRequest;
import com.tickety.payment.dto.request.RefundRequest;
import com.tickety.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request, UUID userId);

    PaymentResponse processPayment(UUID paymentId);

    PaymentResponse getPaymentById(UUID paymentId);

    PaymentResponse getPaymentByNumber(String paymentNumber);

    PaymentResponse getPaymentByReservationId(UUID reservationId);

    List<PaymentResponse> getPaymentsByUserId(UUID userId);

    PaymentResponse refundPayment(UUID paymentId, RefundRequest request);

    PaymentResponse cancelPayment(UUID paymentId);
}
