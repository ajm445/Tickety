package com.tickety.payment.exception;

import java.util.UUID;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException(UUID reservationId) {
        super("Payment already exists for reservation: " + reservationId);
    }
}
