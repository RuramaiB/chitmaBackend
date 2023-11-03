package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.LocalFinance;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalFinanceRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.requests.LocalFinanceRequest;
import me.ruramaibotso.umc.services.LocalFinanceService;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/localFinance")
public class LocalFinanceController {
    private final LocalFinanceService localFinanceService;
    private final LocalFinanceRepository localFinanceRepository;
    private final LocalsRepository localsRepository;
    private final UserRepository userRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;

    @GetMapping("/getAllLocalsFinances")
    public Page<LocalFinance> getAllLocalsFinances(Integer offset){
        return localFinanceService.getAllCircuitFinancesPaged(offset);
    }
    @GetMapping("/getLocalFinanceByLocal/{local}/{offset}")
    public Page<LocalFinance> getLocalFinanceByLocal(@PathVariable int offset, @PathVariable String local){
        return localFinanceService.getAllLocalFinanceByLocal(offset, local);
    }
    @GetMapping("/getLocalFinanceID/{financeID}")
    public LocalFinance getLocalFinanceByID(@PathVariable Integer financeID){
        return localFinanceRepository.findLocalFinanceByFinanceID(financeID)
                .orElseThrow( () ->  new ResourceNotFoundException("Local finance not found"));
    }
    @GetMapping("/getLocalFinanceByMembership/{MembershipNumber}")
    public List<LocalFinance> getLocalFinanceByMembership(@PathVariable String membershipNumber){
        return localFinanceService.getLocFinanceByMembership(membershipNumber);
    }
    @PostMapping("addNewLocalFinanceRecord")
    public LocalFinance addNewLocalFinance(@RequestBody LocalFinanceRequest localFinanceRequest){
        return localFinanceService.addFinanceByLocal(localFinanceRequest);
    }

    @PutMapping("updateLocalFinanceRecordByFinanceID/{financeID}")
    public LocalFinance updateLocalFinanceRecordByFinanceID(@PathVariable Integer financeID, @RequestBody LocalFinanceRequest localFinanceRequest){
        LocalFinance localFinance = localFinanceRepository.findLocalFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Local Finance Record not Found"));
        User user = userRepository.findUserByMembershipNumber(localFinanceRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Locals locals = localsRepository.findByName(localFinanceRequest.getLocals())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(localFinanceRequest.getDescription());
        localFinance.setPhoneNumber(localFinanceRequest.getPhoneNumber());
        localFinance.setFinanceDescription(fd);
        localFinance.setAmount(localFinanceRequest.getAmount());
        localFinance.setMembershipNumber(localFinanceRequest.getMembershipNumber());
        localFinance.setPaymentMethod(localFinanceRequest.getPaymentMethod());
        localFinance.setLocals(locals);
        localFinance.setUser(user);
        localFinance.setCurrency(localFinanceRequest.getCurrency());
        localFinanceRepository.save(localFinance);
        return localFinance;
    }
    @DeleteMapping("/deleteLocalFinanceByMembership")
    public ResponseEntity<LocalFinance> deleteLocalFinanceByMembership(String membershipNumber) {
        List<LocalFinance> localFinance = localFinanceService.getLocFinanceByMembership(membershipNumber);
        localFinanceRepository.deleteAll(localFinance);
        return (ResponseEntity<LocalFinance>) ResponseEntity.noContent();
    }
    @DeleteMapping("/deleteLocalFinanceByFinanceID/{financeID}")
    public String  deleteLocalFinanceByFinanceID(@PathVariable Integer financeID){
        LocalFinance localFinance = localFinanceRepository.findLocalFinanceByFinanceID(financeID)
                        .orElseThrow(() -> new ResourceNotFoundException("Local finance not found"));
        localFinanceRepository.delete(localFinance);
        return "Local Finance record deleted successfully.";
    }
}
