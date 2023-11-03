package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.*;
import me.ruramaibotso.umc.requests.PayNowRequest;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zw.co.paynow.constants.MobileMoneyMethod;
import zw.co.paynow.core.Payment;
import zw.co.paynow.core.Paynow;
import zw.co.paynow.responses.MobileInitResponse;
import zw.co.paynow.responses.StatusResponse;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PayNowService {

    private final UserRepository userRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;
    private final PaymentRepository paymentRepository;
    private final LocalFinanceRepository localFinanceRepository;
    private final LocalsRepository localsRepository;
    private final SectionFinanceRepository sectionFinanceRepository;
    private final OrganisationsFinanceRepository organisationsFinanceRepository;
    private final OrganisationsRepository organisationsRepository;
    private final PledgesRepository pledgesRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Value("${Andrews_ID}")
    private String payNowID;
    @Value("${Andrews_KEY}")
    private String payNowKey;
    public String makePayment(PayNowRequest payNowRequest) {
        User user = userRepository.findUserByMembershipNumber(payNowRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(payNowRequest.getFinancialDescription());
        PayNowDetails payNowDetails = new PayNowDetails();
        payNowDetails.setPaymentStatus(PaymentStatus.Waiting);
        payNowDetails.setAmount(payNowRequest.getAmount());
        payNowDetails.setMembershipNumber(payNowRequest.getMembershipNumber());
        payNowDetails.setMethod(payNowRequest.getMethod());
        payNowDetails.setEmail(payNowRequest.getEmail());
        payNowDetails.setPhoneNumber(payNowRequest.getPhoneNumber());
        payNowDetails.setDateOfPayment(LocalDate.now());
        payNowDetails.setTimeOfPayment(LocalTime.now());
        payNowDetails.setUser(user);
        payNowDetails.setFinanceDescription(fd);
        PayNowDetails paymentDetails1 = paymentRepository.save(payNowDetails);

        Paynow paynow = new Paynow(payNowID, payNowKey);
        Payment payment = paynow.createPayment(fd.getDescription(), paymentDetails1.getEmail());
        payment.add(paymentDetails1.getMembershipNumber(), paymentDetails1.getAmount());
        MobileInitResponse response = paynow.sendMobile(payment, paymentDetails1.getPhoneNumber(), MobileMoneyMethod.valueOf(paymentDetails1.getMethod().toUpperCase()));
        if (response.success()) {
            // Get the instructions to show to the user
            String instructions  = response.instructions();
            // Get the poll URL of the transaction
            String pollUrl = response.pollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if (status.paid()) {
                return pollUrl;
            }
//            else {
//                    PaymentDetails paymentRecord = paymentRepository.findPaymentByStudentNumber(paymentDetails1.getStudentNumber());
//                            .orElseThrow(() -> new ResourceNotFoundException(("Payment was not found.")));
//                    paymentRepository.delete(paymentRecord);
//                }

            return instructions;
        } else {
            throw new RuntimeException("Payment failed");
        }

    }

    public String makeLocalFinance(PayNowRequest payNowRequest) {
        User user = userRepository.findUserByMembershipNumber(payNowRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(payNowRequest.getFinancialDescription());
        Locals locals = localsRepository.findByName(payNowRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        PaymentDetails paymentDetails = paymentDetailsRepository.findByLocalsAndCurrencyAndPaymentMethod(locals, payNowRequest.getCurrency(), payNowRequest.getMethod());
        LocalFinance localFinance = new LocalFinance();
        localFinance.setLocals(locals);
        localFinance.setFinanceDescription(fd);
        localFinance.setFinanceID(localFinance.getFinanceID());
        localFinance.setUser(user);
        localFinance.setDateOfPayment(LocalDateTime.now());
        localFinance.setCurrency(payNowRequest.getCurrency());
        localFinance.setMembershipNumber(payNowRequest.getMembershipNumber());
        localFinance.setAmount(payNowRequest.getAmount());
        localFinance.setPhoneNumber(payNowRequest.getPhoneNumber());
        localFinance.setPaymentMethod(payNowRequest.getMethod());
        LocalFinance pay = localFinanceRepository.save(localFinance);

//        Paynow paynow = new Paynow(payNowID, payNowKey);
        Paynow paynow = new Paynow(paymentDetails.getPaymentId(), paymentDetails.getPaymentKey());
        System.out.println(paymentDetails.getPaymentId());
        System.out.println(paymentDetails.getPaymentKey());
        Payment payment = paynow.createPayment(fd.getDescription(),payNowRequest.getEmail());
        payment.add(pay.getMembershipNumber(), pay.getAmount());
        MobileInitResponse response = paynow.sendMobile(payment, pay.getPhoneNumber(), MobileMoneyMethod.valueOf(pay.getPaymentMethod().toUpperCase()));
        if (response.success()) {
            // Get the instructions to show to the user
            String instructions  = response.instructions();
            // Get the poll URL of the transaction
            String pollUrl = response.pollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if (status.paid()) {
                return pollUrl;
            }
            else {
                    LocalFinance paymentRecord = localFinanceRepository.findLocalFinanceByFinanceID(localFinance.getFinanceID())
                            .orElseThrow(() -> new ResourceNotFoundException(("Payment was not found.")));
                    localFinanceRepository.delete(paymentRecord);
                }

            return instructions;
        } else {
            throw new RuntimeException("Payment failed");
        }

    }
    public String makeSectionFinance(PayNowRequest payNowRequest) {
        User user = userRepository.findUserByMembershipNumber(payNowRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(payNowRequest.getFinancialDescription());
        Locals locals = localsRepository.findByName(payNowRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        PaymentDetails paymentDetails = paymentDetailsRepository.findByLocalsAndCurrencyAndPaymentMethod(locals, payNowRequest.getCurrency(), payNowRequest.getMethod());
        SectionFinance sectionFinance = new SectionFinance();
        sectionFinance.setSection(user.getSection());
        sectionFinance.setUser(user);
        sectionFinance.setCurrency(payNowRequest.getCurrency());
        sectionFinance.setFinanceID(sectionFinance.getFinanceID());
        sectionFinance.setFinanceDescription(fd);
        sectionFinance.setAmount(payNowRequest.getAmount());
        sectionFinance.setPaymentMethod(payNowRequest.getMethod());
        sectionFinance.setMembershipNumber(user.getMembershipNumber());
        sectionFinance.setDateOfPayment(LocalDateTime.now());
        sectionFinance.setLocals(locals);
        sectionFinance.setPhoneNumber(payNowRequest.getPhoneNumber());
        SectionFinance secPay = sectionFinanceRepository.save(sectionFinance);
//        Paynow paynow = new Paynow(payNowID, payNowKey);
        Paynow paynow = new Paynow(paymentDetails.getPaymentId(), paymentDetails.getPaymentKey());
        Payment payment = paynow.createPayment(fd.getDescription(),payNowRequest.getEmail());
        payment.add(secPay.getMembershipNumber(), secPay.getAmount());
        MobileInitResponse response = paynow.sendMobile(payment, secPay.getPhoneNumber(), MobileMoneyMethod.valueOf(secPay.getPaymentMethod().toUpperCase()));
        if (response.success()) {
            // Get the instructions to show to the user
            String instructions  = response.instructions();
            // Get the poll URL of the transaction
            String pollUrl = response.pollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if (status.paid()) {
                return pollUrl;
            }
            else {
                LocalFinance paymentRecord = localFinanceRepository.findLocalFinanceByFinanceID(sectionFinance.getFinanceID())
                        .orElseThrow(() -> new ResourceNotFoundException(("Payment was not found.")));
                localFinanceRepository.delete(paymentRecord);
            }

            return instructions;
        } else {
            throw new RuntimeException("Payment failed");
        }

    }
    public String makeOrganisationFinance(PayNowRequest payNowRequest) {
        User user = userRepository.findUserByMembershipNumber(payNowRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(payNowRequest.getFinancialDescription());
        Locals locals = localsRepository.findByName(payNowRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        Organisations organisations = organisationsRepository.findByOrganisationAndLocals(user.getOrganisation(), locals)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        PaymentDetails paymentDetails = paymentDetailsRepository.findByLocalsAndCurrencyAndPaymentMethod(locals, payNowRequest.getCurrency(), payNowRequest.getMethod());
        OrganisationFinance organisationFinance = new OrganisationFinance();
        organisationFinance.setFinanceID(organisationFinance.getFinanceID());
        organisationFinance.setUser(user);
        organisationFinance.setCurrency(payNowRequest.getCurrency());
        organisationFinance.setOrganisations(organisations);
        organisationFinance.setFinanceDescription(fd);
        organisationFinance.setAmount(payNowRequest.getAmount());
        organisationFinance.setPaymentMethod(payNowRequest.getMethod());
        organisationFinance.setMembershipNumber(user.getMembershipNumber());
        organisationFinance.setDateOfPayment(LocalDateTime.now());
        organisationFinance.setLocals(locals);
        organisationFinance.setPhoneNumber(payNowRequest.getPhoneNumber());
        OrganisationFinance org = organisationsFinanceRepository.save(organisationFinance);

        Paynow paynow = new Paynow(paymentDetails.getPaymentId(), paymentDetails.getPaymentKey());
        Payment payment = paynow.createPayment(fd.getDescription(),payNowRequest.getEmail());
        payment.add(org.getMembershipNumber(), org.getAmount());
        MobileInitResponse response = paynow.sendMobile(payment, org.getPhoneNumber(), MobileMoneyMethod.valueOf(org.getPaymentMethod().toUpperCase()));
        if (response.success()) {
            // Get the instructions to show to the user
            String instructions  = response.instructions();
            // Get the poll URL of the transaction
            String pollUrl = response.pollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if (status.paid()) {
                return pollUrl;
            }
            else {
                LocalFinance paymentRecord = localFinanceRepository.findLocalFinanceByFinanceID(org.getFinanceID())
                        .orElseThrow(() -> new ResourceNotFoundException(("Payment was not found.")));
                localFinanceRepository.delete(paymentRecord);
            }

            return instructions;
        } else {
            throw new RuntimeException("Payment failed");
        }

    }
    public String makePledgeFinance(PayNowRequest payNowRequest) {
        User user = userRepository.findUserByMembershipNumber(payNowRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(payNowRequest.getFinancialDescription());
        Locals locals = localsRepository.findByName(payNowRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        PaymentDetails paymentDetails = paymentDetailsRepository.findByLocalsAndCurrencyAndPaymentMethod(locals, payNowRequest.getCurrency(), payNowRequest.getMethod());
        Pledges pledges = new Pledges();
        pledges.setFinanceID(pledges.getFinanceID());
        pledges.setUser(user);
        pledges.setCurrency(payNowRequest.getCurrency());
        pledges.setDescription(payNowRequest.getFinancialDescription());
        pledges.setAmount(payNowRequest.getAmount());
        pledges.setPaymentMethod(payNowRequest.getMethod());
        pledges.setMembershipNumber(user.getMembershipNumber());
        pledges.setDateOfPayment(LocalDateTime.now());
        pledges.setLocals(locals);
        pledges.setPhoneNumber(payNowRequest.getPhoneNumber());
        Pledges pg = pledgesRepository.save(pledges);

        Paynow paynow = new Paynow(paymentDetails.getPaymentId(), paymentDetails.getPaymentKey());
        Payment payment = paynow.createPayment(fd.getDescription(),payNowRequest.getEmail());
        payment.add(pg.getMembershipNumber(), pg.getAmount());
        MobileInitResponse response = paynow.sendMobile(payment, pg.getPhoneNumber(), MobileMoneyMethod.valueOf(pg.getPaymentMethod().toUpperCase()));
        if (response.success()) {
            // Get the instructions to show to the user
            String instructions  = response.instructions();
            // Get the poll URL of the transaction
            String pollUrl = response.pollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if (status.paid()) {
                return pollUrl;
            }
            else {
                LocalFinance paymentRecord = localFinanceRepository.findLocalFinanceByFinanceID(pg.getFinanceID())
                        .orElseThrow(() -> new ResourceNotFoundException(("Payment was not found.")));
                localFinanceRepository.delete(paymentRecord);
            }

            return instructions;
        } else {
            throw new RuntimeException("Payment failed");
        }

    }


}
