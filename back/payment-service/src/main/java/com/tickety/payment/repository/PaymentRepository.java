package com.tickety.payment.repository;

import com.tickety.payment.domain.entity.Payment;
import com.tickety.payment.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    Optional<Payment> findByReservationId(UUID reservationId);

    List<Payment> findByUserId(UUID userId);

    List<Payment> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Payment> findByStatus(PaymentStatus status);

    boolean existsByReservationIdAndStatusIn(UUID reservationId, List<PaymentStatus> statuses);
}
