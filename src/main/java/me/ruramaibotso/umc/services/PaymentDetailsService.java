package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.PayNowDetails;
import me.ruramaibotso.umc.model.PaymentDetails;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.PaymentDetailsRepository;
import me.ruramaibotso.umc.requests.PaymentDetailsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.PublicKey;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final LocalsRepository localsRepository;

    public Page<PaymentDetails> getAllPaymentDetailsByLocals(String local, int offset){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return paymentDetailsRepository.findAllByLocals( PageRequest.of(offset, 10), locals);
    }
    public Page<PaymentDetails> getAllPaymentDetails(int offset){
        return paymentDetailsRepository.findAll(PageRequest.of(offset, 10));
    }

    public PaymentDetails addNewPaymentDetails(PaymentDetailsRequest paymentDetailsRequest){
        Locals locals = localsRepository.findByName(paymentDetailsRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(paymentDetails.getId());
        paymentDetails.setPaymentKey(paymentDetailsRequest.getPaymentKey());
        paymentDetails.setPaymentMethod(paymentDetailsRequest.getPaymentMethod());
        paymentDetails.setLocals(locals);
        paymentDetails.setCurrency(paymentDetailsRequest.getCurrency());
        paymentDetails.setPaymentId(paymentDetailsRequest.getPaymentId());
        return paymentDetailsRepository.save(paymentDetails);
    }
    public PaymentDetails updatePaymentDetails(PaymentDetailsRequest paymentDetailsRequest, int id){
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Details not found"));
        Locals locals = localsRepository.findByName(paymentDetailsRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        paymentDetails.setPaymentId(paymentDetailsRequest.getPaymentId());
        paymentDetails.setPaymentMethod(paymentDetailsRequest.getPaymentMethod());
        paymentDetails.setPaymentKey(paymentDetailsRequest.getPaymentKey());
        paymentDetails.setCurrency(paymentDetailsRequest.getCurrency());
        paymentDetails.setLocals(locals);
       return paymentDetailsRepository.save(paymentDetails);
    }
    public PaymentDetails findByLocalsAndCurrencyAndPaymentMethod(String local, String currency, String paymentMethod){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return paymentDetailsRepository.findByLocalsAndCurrencyAndPaymentMethod(locals, currency, paymentMethod);
    }
}
