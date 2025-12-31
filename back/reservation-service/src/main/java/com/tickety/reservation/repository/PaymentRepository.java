package com.tickety.reservation.repository;

import com.tickety.reservation.domain.entity.Payment;
import com.tickety.reservation.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByUserId(UUID userId);

    List<Payment> findByUserIdAndStatus(UUID userId, PaymentStatus status);

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    Optional<Payment> findByReservationId(UUID reservationId);

    @Query("SELECT p FROM Payment p JOIN FETCH p.reservation WHERE p.userId = :userId")
    List<Payment> findByUserIdWithReservation(@Param("userId") UUID userId);

    Optional<Payment> findByPgTransactionId(String pgTransactionId);
}
