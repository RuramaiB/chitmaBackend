package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionFinanceRepository;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.requests.SectionFinanceRequest;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SectionFinanceServices {
    private final SectionFinanceRepository sectionFinanceRepository;
    private final SectionsRepository sectionsRepository;
    private final LocalsRepository localsRepository;
    private final UserRepository userRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;

    public Page<SectionFinance> getAllSectionFinances(Integer offset){
        return sectionFinanceRepository.findAll(PageRequest.of(offset,10));
    }
    public SectionFinance addSecFinance(SectionFinanceRequest sectionFinanceRequest){
        SectionFinance sectionFinance = new SectionFinance();
        Locals local = localsRepository.findByName(sectionFinanceRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        Section section = sectionsRepository.findByLocalsAndName(local,sectionFinanceRequest.getSection())
                .orElseThrow(()-> new ResourceNotFoundException("Section not found"));
        User user = userRepository.findUserByMembershipNumber(sectionFinanceRequest.getMembershipNumber())
                .orElseThrow(()-> new ResourceNotFoundException("Section not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescriptionAndLocals(sectionFinanceRequest.getDescription(), local)
                        .orElseThrow(()-> new ResourceNotFoundException("Finance Description not found"));
        sectionFinance.setFinanceDescription(fd);
        sectionFinance.setFinanceID(sectionFinance.getFinanceID());
        sectionFinance.setAmount(sectionFinanceRequest.getAmount());
        sectionFinance.setPaymentMethod(sectionFinanceRequest.getPaymentMethod());
        sectionFinance.setDateOfPayment(LocalDateTime.now());
        sectionFinance.setCurrency(sectionFinanceRequest.getCurrency());
        sectionFinance.setMembershipNumber(sectionFinanceRequest.getMembershipNumber());
        sectionFinance.setPhoneNumber(sectionFinanceRequest.getPhoneNumber());
        sectionFinance.setLocals(local);
        sectionFinance.setSection(section);
        sectionFinance.setUser(user);
        return sectionFinanceRepository.save(sectionFinance);
    }

    public List<SectionFinance> getSecFinRecordByMembershipNumber(String membershipNumber){
        return sectionFinanceRepository.findSectionRecordByMembershipNumberOrderByDateOfPaymentDesc(membershipNumber);
    }
    public SectionFinance getSectionFinanceByFinanceID(Integer financeID){
        return sectionFinanceRepository.findSectionFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Section finance not found"));
    }

    public List<SectionFinance> getSecFinanceBySection(@PathVariable String sectionName){
        return sectionFinanceRepository.findBySectionName(sectionName);

    }

    public Page<SectionFinance> getAllSectionFinanceByLocal(int offset, String local) {
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return sectionFinanceRepository.findAllByLocalsOrderByDateOfPaymentDesc(PageRequest.of(offset,10), locals);
    }
}
