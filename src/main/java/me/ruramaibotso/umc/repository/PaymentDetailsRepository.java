package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.PaymentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {
    Page<PaymentDetails> findAllByLocals(Pageable pageable, Locals locals);
    PaymentDetails findByLocalsAndCurrencyAndPaymentMethod(Locals locals, String currency, String paymentMethod);

}
