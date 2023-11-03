package me.ruramaibotso.umc.services;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Pledges;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.PledgesRepository;
import me.ruramaibotso.umc.requests.PledgesRequest;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PledgesServices {
    private final PledgesRepository pledgesRepository;
    private final LocalsRepository localsRepository;
    private final UserRepository userRepository;

    public Pledges addPledge(PledgesRequest pledgesRequest){
        Locals locals = localsRepository.findByName(pledgesRequest.getLocals())
                .orElseThrow(()-> new ResourceNotFoundException("Local not found"));

        Pledges pledges = new Pledges();
        pledges.setFinanceID(pledges.getFinanceID());
        pledges.setDescription(pledgesRequest.getDescription());
        pledges.setAmount(pledgesRequest.getAmount());
        pledges.setPaymentMethod(pledgesRequest.getPaymentMethod());
        pledges.setDateOfPayment(LocalDateTime.now());
        pledges.setPhoneNumber(pledgesRequest.getPhoneNumber());
        pledges.setCurrency(pledgesRequest.getCurrency());
        pledges.setLocals(locals);
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

      return  pledgesRepository.save(pledges);
    }
    public Page<Pledges> getAllPledgesByLocal(String local, int offset){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return pledgesRepository.findAllByLocalsOrderByDateOfPaymentDesc(PageRequest.of(offset, 10), locals);
    }
}
