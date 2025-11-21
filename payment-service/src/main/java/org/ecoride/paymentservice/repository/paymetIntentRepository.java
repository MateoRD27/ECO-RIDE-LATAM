package org.ecoride.paymentservice.repository;

import org.ecoride.paymentservice.model.entity.PaymentIntent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface paymetIntentRepository extends JpaRepository<PaymentIntent, UUID> {

    //Encontrar el intento de pago de una reserva específica
    Optional<PaymentIntent> findByPaymentIntentId(UUID paymentIntentId);

}
