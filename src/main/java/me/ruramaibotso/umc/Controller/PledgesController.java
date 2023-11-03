package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Pledges;
import me.ruramaibotso.umc.repository.PledgesRepository;
import me.ruramaibotso.umc.requests.PledgesRequest;
import me.ruramaibotso.umc.services.PledgesServices;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pledges")
@RequiredArgsConstructor
@CrossOrigin
public class PledgesController {

    private final PledgesServices pledgesServices;
    private final PledgesRepository pledgesRepository;
    private final UserRepository userRepository;

    @GetMapping("/getAllPledgesByLocal/{local}/{offset}")
    public Page<Pledges> getAllPledgesByLocal(@PathVariable String local, @PathVariable Integer offset){
        return pledgesServices.getAllPledgesByLocal(local,offset);
    }
    @PostMapping("/addNewPledge")
    public Pledges addNewPledge(@RequestBody PledgesRequest pledgesRequest){
        return pledgesServices.addPledge(pledgesRequest);
    }
    @GetMapping("/getPledgeByFinanceID/{financeID}")
    public Pledges getPledgeByFinanceID(@PathVariable Integer financeID){
        return pledgesRepository.findById(financeID).orElseThrow(() -> new ResourceNotFoundException("Pledge not found"));
    }
    @PutMapping("/updatePledgeByFinanceID/{financeID}")
    public Pledges updatePledgeByFinanceID(@RequestBody PledgesRequest pledgesRequest ,@PathVariable Integer financeID){
        Pledges pledges = pledgesRepository.findById(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Pledge not found"));
        pledges.setDescription(pledgesRequest.getDescription());
        pledges.setAmount(pledgesRequest.getAmount());
        pledges.setPaymentMethod(pledgesRequest.getPaymentMethod());
        pledges.setDateOfPayment(LocalDateTime.now());
        pledges.setPhoneNumber(pledgesRequest.getPhoneNumber());
        pledges.setCurrency(pledgesRequest.getCurrency());
        if(pledgesRequest.getMembershipNumber().contains("null") || pledgesRequest.getMembershipNumber() == null){
            pledges.setUser(null);
            pledges.setMembershipNumber(null);


        }
        else{
            User user = userRepository.findUserByMembershipNumber(pledgesRequest.getMembershipNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("User not found"));
            pledges.setMembershipNumber(pledgesRequest.getMembershipNumber());
            pledges.setUser(user);

        }
        return pledgesRepository.save(pledges);

    }

    @DeleteMapping("/deletePledgeByFinanceID/{financeID}")
    public String deletePledgeByFinanceID(@PathVariable Integer financeID){
        Pledges pledges = pledgesRepository.findById(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Pledge was not found"));
        pledgesRepository.delete(pledges);
        return "Pledge deleted successfully";
    }
}
