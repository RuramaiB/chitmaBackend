package me.ruramaibotso.umc.Controller;


import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.model.PayNowDetails;
import me.ruramaibotso.umc.repository.PaymentRepository;
import me.ruramaibotso.umc.requests.PayNowRequest;
import me.ruramaibotso.umc.services.PayNowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paynow")
@RequiredArgsConstructor
public class PayNowController {
    private final PaymentRepository paymentRepository;
    private final PayNowService payNowService;

    @PostMapping("/makePayment")
    public String makePayment(@RequestBody PayNowRequest payNowRequest){
       return payNowService.makePayment(payNowRequest);
    }
    @PostMapping("/makeLocalFinance")
    public String makeLocalFinance(@RequestBody PayNowRequest payNowRequest){
        return payNowService.makeLocalFinance(payNowRequest);
    }
    @PostMapping("/makeSectionFinance")
    public String makeSectionFinance(@RequestBody PayNowRequest payNowRequest){
        return payNowService.makeSectionFinance(payNowRequest);
    }
    @PostMapping("/makeOrganisationFinance")
    public String makeOrganisationFinance(@RequestBody PayNowRequest payNowRequest){
        return payNowService.makeOrganisationFinance(payNowRequest);
    }
    @PostMapping("/makePledge")
    public String makePledge(@RequestBody PayNowRequest payNowRequest){
        return payNowService.makePledgeFinance(payNowRequest);
    }
    @GetMapping("/getAllPayments")
    public List<PayNowDetails> getAllPayments(){
        return paymentRepository.findAll();
    }
}
