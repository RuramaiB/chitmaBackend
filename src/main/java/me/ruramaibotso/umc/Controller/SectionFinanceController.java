package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.SectionFinance;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionFinanceRepository;
import me.ruramaibotso.umc.requests.SectionFinanceRequest;
import me.ruramaibotso.umc.services.SectionFinanceServices;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/sectionFinance")
public class SectionFinanceController {
    private final SectionFinanceServices sectionFinanceServices;
    private final SectionFinanceRepository sectionFinanceRepository;
    private final LocalsRepository localsRepository;
    private final UserRepository userRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;

    @GetMapping("/getAllFinancesForAllSections")
    public Page<SectionFinance> getAllFinancesForAllSections(Integer offset){
        return sectionFinanceServices.getAllSectionFinances(offset);
    }

    @PostMapping("addSectionFinance")
    public SectionFinance addSectionFinance(@RequestBody SectionFinanceRequest sectionFinanceRequest){
        return sectionFinanceServices.addSecFinance(sectionFinanceRequest);
    }
    @GetMapping("/getSectionFinanceBySection")
    public List<SectionFinance> getFinanceBySection(String sectionName){
        return sectionFinanceServices.getSecFinanceBySection(sectionName);
    }
    @GetMapping("/getSectionFinanceByIndividual/{membershipNumber}")
    public List<SectionFinance> getSectionFinanceByIndividual(@PathVariable String membershipNumber){
        return sectionFinanceRepository.findSectionRecordByMembershipNumberOrderByDateOfPaymentDesc(membershipNumber);
    }
    @GetMapping("/getSectionFinanceByLocal/{local}/{offset}")
    public Page<SectionFinance> getSectionFinanceByLocal(@PathVariable int offset,@PathVariable String local){
        return sectionFinanceServices.getAllSectionFinanceByLocal(offset, local);
    }
    @GetMapping("/getSectionFinanceByFinanceID/{financeID}")
    public SectionFinance getSectionFinanceByFinanceID(@PathVariable Integer financeID){
        return sectionFinanceServices.getSectionFinanceByFinanceID(financeID);
    }

    @DeleteMapping("/deleteFinanceBySection")
    public ResponseEntity<SectionFinance> deleteFinanceBySection(String sectionName){
        List<SectionFinance> sectionFinance = sectionFinanceRepository.findBySectionName(sectionName);
        sectionFinanceRepository.deleteAll( sectionFinance);
        return (ResponseEntity<SectionFinance>) ResponseEntity.noContent();
    }
    @DeleteMapping("/deleteFinanceByMembership")
    public ResponseEntity<SectionFinance> deleteFinanceByMembership(String membershipNumber){
        List<SectionFinance> sectionFinance = sectionFinanceServices.getSecFinRecordByMembershipNumber(membershipNumber);
        sectionFinanceRepository.deleteAll(sectionFinance);
        return (ResponseEntity<SectionFinance>) ResponseEntity.noContent();

    }
    @DeleteMapping("/deleteSectionFinanceByFinanceID/{financeID}")
    public String deleteSectionFinanceByFinanceID(@PathVariable Integer financeID){
        SectionFinance sectionFinance = sectionFinanceRepository.findSectionFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Section finance not found"));
        sectionFinanceRepository.delete(sectionFinance);
        return "Section Finance Record with ID: "+ financeID + " has been deleted successfully";
    }
    @PutMapping("/updateSectionFinanceByFinanceID/{financeID}")
    public SectionFinance updateSectionFinanceByFinanceID(@PathVariable Integer financeID,@RequestBody SectionFinanceRequest sectionFinanceRequest){
        SectionFinance sectionFinance = sectionFinanceRepository.findSectionFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Section finance record not found"));
        User user = userRepository.findUserByMembershipNumber(sectionFinanceRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Locals local = localsRepository.findByName(sectionFinanceRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(sectionFinanceRequest.getDescription());
        sectionFinance.setPhoneNumber(sectionFinanceRequest.getPhoneNumber());
        sectionFinance.setMembershipNumber(sectionFinanceRequest.getMembershipNumber());
        sectionFinance.setCurrency(sectionFinanceRequest.getCurrency());
        sectionFinance.setLocals(local);
        sectionFinance.setUser(user);
        sectionFinance.setFinanceDescription(fd);
        sectionFinance.setAmount(sectionFinanceRequest.getAmount());
        sectionFinance.setPaymentMethod(sectionFinanceRequest.getPaymentMethod());
        sectionFinanceRepository.save(sectionFinance);
        return sectionFinance;
    }
}
