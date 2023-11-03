package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.requests.FinanceDescriptionRequest;
import me.ruramaibotso.umc.services.FinanceDescriptionServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/financeDescription/")
public class FinanceDescriptionController {
    private final FinanceDescriptionServices financeDescriptionServices;
    private final FinanceDescriptionRepository financeDescriptionRepository;

    @GetMapping("/getAllFinanceDescriptions/{local}")
    public List<FinanceDescription> getAllFinanceDescriptions(@PathVariable String local){
        return financeDescriptionServices.getFinanceDescriptionByLocal(local);
    }
    @GetMapping("/getFinancialDescriptionWithoutTargetsByLocal/{local}")
    public List<FinanceDescription> getFinancialDescriptionWithoutTargetsByLocal(@PathVariable String local){
        return financeDescriptionServices.getFinancialDescriptionWithoutTargetsByLocal(local);
    }
    @GetMapping("/getFinanceDescriptionBy/{Id}")
    public Optional<FinanceDescription> getFinanceDescriptionById(@PathVariable int Id){
        return financeDescriptionRepository.findById(Id);
    }
    @PostMapping("/addNewFinanceDescriptions")
    public FinanceDescription addNewFinanceDescriptions(@RequestBody FinanceDescriptionRequest financeDescriptionRequest){
        return financeDescriptionServices.addFinanceDescription(financeDescriptionRequest);
    }
    @DeleteMapping("/deleteFinanceDescriptionByID/{ID}")
    public String deleteFinanceDescriptionByID(@PathVariable Integer ID){
        FinanceDescription financeDescription = financeDescriptionRepository.findById(ID)
                .orElseThrow(()-> new ResourceNotFoundException("Finance Description not found"));
        financeDescriptionRepository.delete(financeDescription);
        return "Finance Description deleted successfully";
    }
}
