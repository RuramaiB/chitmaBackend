package me.ruramaibotso.umc.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.PayNowDetails;
import me.ruramaibotso.umc.model.PaymentDetails;
import me.ruramaibotso.umc.repository.PaymentDetailsRepository;
import me.ruramaibotso.umc.requests.PaymentDetailsRequest;
import me.ruramaibotso.umc.services.PaymentDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentDetails")
@RequiredArgsConstructor
public class PaymentDetailsController {
    private final PaymentDetailsService paymentDetailsService;
    private final PaymentDetailsRepository paymentDetailsRepository;

    @GetMapping("/getAllPaymentDetails/{offset}")
    public Page<PaymentDetails> getAllPaymentDetails(@PathVariable int offset){
     return paymentDetailsService.getAllPaymentDetails(offset);
    }
    @GetMapping("/findBy{local}And{currency}And{paymentMethod}")
    public PaymentDetails findByLocalCurrencyAndPaymentMethod(@PathVariable String local, @PathVariable String currency, @PathVariable String paymentMethod){
        return paymentDetailsService.findByLocalsAndCurrencyAndPaymentMethod(local, currency, paymentMethod);
    }
    @GetMapping("/getAllPaymentDetailsBy/{local}/{offset}")
    public Page<PaymentDetails> getAllPaymentDetailsByLocal(@PathVariable String local, @PathVariable int offset){
        return paymentDetailsService.getAllPaymentDetailsByLocals(local, offset);
    }
    @PostMapping("/addNewPaymentDetails")
    public PaymentDetails addNewPaymentDetails(@RequestBody PaymentDetailsRequest paymentDetailsRequest){
        return paymentDetailsService.addNewPaymentDetails(paymentDetailsRequest);
    }
    @PutMapping("/updatePaymentDetails/{id}")
    public PaymentDetails updatePaymentDetails(@RequestBody PaymentDetailsRequest paymentDetailsRequest, @PathVariable int id){
        return paymentDetailsService.updatePaymentDetails(paymentDetailsRequest, id);
    }
    @DeleteMapping("/deletePaymentDetails/{id}")
    public String deletePaymentDetails(@PathVariable int id){
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Details not found"));
        paymentDetailsRepository.delete(paymentDetails);
        return "Payment details deleted successfullt";
    }

}
