package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.requests.FinanceDescriptionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceDescriptionServices {
    private final FinanceDescriptionRepository financeDescriptionRepository;
    private final LocalsRepository localsRepository;

    public FinanceDescription addFinanceDescription(FinanceDescriptionRequest financeDescriptionRequest){
        Locals locals = localsRepository.findByName(financeDescriptionRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        FinanceDescription financeDescription = new FinanceDescription();
        financeDescription.setID(financeDescription.getID());
        financeDescription.setDescription(financeDescriptionRequest.getDescription());
        financeDescription.setGrandTarget(financeDescriptionRequest.getTarget());
        financeDescription.setKickOff(financeDescriptionRequest.getKickOffDate());
        financeDescription.setLocals(locals);
        return financeDescriptionRepository.save(financeDescription);
    }

    public List<FinanceDescription> getFinanceDescriptionByLocal(String local){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
       return financeDescriptionRepository.findAllByLocals(locals);
    }
    public List<FinanceDescription> getFinancialDescriptionWithoutTargetsByLocal(String local){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return financeDescriptionRepository.findAllByLocalsAndGrandTargetGreaterThan(locals, (double) 0);
    }

}
