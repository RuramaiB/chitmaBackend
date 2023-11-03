package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.model.FinancialTargets;
import me.ruramaibotso.umc.repository.FinancialTargetsRepository;
import me.ruramaibotso.umc.requests.FinancialTargetRequest;
import me.ruramaibotso.umc.services.FinancialTargetService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financialTargets")
@RequiredArgsConstructor
public class FinancialTargetController {
    private final FinancialTargetService financialTargetService;
    private final FinancialTargetsRepository financialTargetsRepository;

    @GetMapping("/getAllFinancialTargetsByLocal/{local}/{offset}")
    public Page<FinancialTargets> getAllFinancialTargetByLocal(@PathVariable String local, @PathVariable Integer offset){
        return financialTargetService.getAllFinancialTargetByLocal(local, offset);
    }
    @PostMapping("distributeFinancialTargetForSections")
    public FinancialTargets addNewFinancialTargetForSections(@RequestBody FinancialTargetRequest financialTargetRequest){
        return financialTargetService.distributeTargetsToSections(financialTargetRequest);
    }
    @PostMapping("distributeToOrganisationsAndSections")
    public FinancialTargets distributeToBoth(@RequestBody FinancialTargetRequest financialTargetRequest){
        return financialTargetService.distributeTargetsToOrganisationsAndSections(financialTargetRequest);
    }
    @PostMapping("distributeFinancialTargetForOrganisations")
    public FinancialTargets addNewFinancialTargetForOrganisations(@RequestBody FinancialTargetRequest financialTargetRequest){
        return financialTargetService.distributeTargetsToOrganisations(financialTargetRequest);
    }
    @GetMapping("/getAllSectionsFinancialTargets/{local}")
    public List<FinancialTargets> getAllSectionsFinancialTargets(@PathVariable String local){
        return financialTargetService.getFinancialTargetsBySectionLevel(local);
    }
    @GetMapping("/getAllOrganisationsFinancialTargets/{local}")
    public List<FinancialTargets> getAllOrganisationsFinancialTargets(@PathVariable String local){
        return financialTargetService.getFinancialTargetsAtOrganisationLevel(local);
    }
    @GetMapping("/getAllFinancialTargetsForBoth/{local}")
    public List<FinancialTargets> getAllFinancialTargetsForBoth(@PathVariable String local){
        return financialTargetService.getFinancialTargetsForBoth(local);
    }
    @GetMapping("/getFinancialTargetBy{local}And{financialDescription}/{offset}")

    public Page<FinancialTargets> getAllByLocalAndFinancialDescription(@PathVariable String local, @PathVariable String financialDescription, @PathVariable Integer offset){
        return financialTargetService.getAllFinancialTargetsByLocalAndFinancialDescription(local, offset, financialDescription);
    }
    @PutMapping("/updateOrganisationFinancialTarget/{id}/{amountUpdate}")
    public FinancialTargets updateOrganisationFinancialTarget(@PathVariable int id, @PathVariable double amountUpdate){
        return financialTargetService.updateOrganisationFinancialTarget(id, amountUpdate);
    }

    @PutMapping("/updateSectionFinancialTarget/{id}/{amountUpdate}")
    public FinancialTargets updateSectionFinancialTarget(@PathVariable int id, @PathVariable double amountUpdate){
        return financialTargetService.updateSectionFinancialTarget(id, amountUpdate);
    }

}
