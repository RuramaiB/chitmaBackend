package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.OrganisationsFinanceRepository;
import me.ruramaibotso.umc.repository.OrganisationsRepository;
import me.ruramaibotso.umc.requests.OrganisationsFinanceRequest;
import me.ruramaibotso.umc.services.OrganisationsFinanceServices;
import me.ruramaibotso.umc.user.Organisation;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/organisationsFinance")
public class OrganisationsFinanceController {
    private final OrganisationsFinanceServices organisationsFinanceServices;
    private final OrganisationsFinanceRepository organisationsFinanceRepository;
    private final UserRepository userRepository;
    private final OrganisationsRepository organisationsRepository;
    private final LocalsRepository localsRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;
    @GetMapping("/getAllOrganisationsFinance")
    public Page<OrganisationFinance> getAllOrganisationsFinance(Integer offset){
        return organisationsFinanceServices.getAllOrganisationsFinance(offset);
    }
    @PostMapping("addOrganisationsFinance")
    public OrganisationFinance addOrgFinance(@RequestBody OrganisationsFinanceRequest organisationsFinanceRequest){
        return organisationsFinanceServices.addNewOrgFinance(organisationsFinanceRequest);
    }
    @GetMapping("/getOrganisationFinanceByMembership/{membershipNumber}")
    public List<OrganisationFinance> getOrgFinanceByMembership(@PathVariable String membershipNumber){
        return organisationsFinanceServices.getOrganisationFinanceByMembership(membershipNumber);
    }
    @GetMapping("/getOrganisationsFinanceByFinanceID/{financeID}")
    public OrganisationFinance getOrgFinanceByFinanceID(@PathVariable Integer financeID){
        return  organisationsFinanceServices.getOrganisationFinanceByFinanceID(financeID);
    }
    @GetMapping("/getAllOrganisationsByLocal/{local}/{offset}")
    public Page<OrganisationFinance> getAllOrganisationsByLocal(@PathVariable int offset, @PathVariable String local){
        return organisationsFinanceServices.getOrganisationsFinanceByLocal(offset,local);

    }
    @PutMapping("/updateOrganisationFinanceByFinanceID/{financeID}")
    public OrganisationFinance updateOrganisationFinanceByFinanceID(@PathVariable Integer financeID, @RequestBody OrganisationsFinanceRequest organisationsFinanceRequest){
        OrganisationFinance organisationFinance = organisationsFinanceRepository.findOrganisationFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Organisations finance record not found."));
        User user = userRepository.findUserByMembershipNumber(organisationsFinanceRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Locals local = localsRepository.findByName(organisationsFinanceRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        Organisations organisations = organisationsRepository.findOrganisationByLocalsAndOrganisation(local,organisationsFinanceRequest.getOrganisation())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));
        FinanceDescription fd = financeDescriptionRepository.findByDescription(organisationsFinanceRequest.getDescription());
        organisationFinance.setPhoneNumber(organisationsFinanceRequest.getPhoneNumber());
        organisationFinance.setOrganisations(organisations);
        organisationFinance.setCurrency(organisationsFinanceRequest.getCurrency());
        organisationFinance.setLocals(local);
        organisationFinance.setUser(user);
        organisationFinance.setAmount(organisationsFinanceRequest.getAmount());
        organisationFinance.setFinanceDescription(fd);
        organisationsFinanceRepository.save(organisationFinance);
        return organisationFinance;

    }
    @DeleteMapping("/deleteOrganisationsFinance/{financeID}")
    public String deleteOrgFinance( @PathVariable Integer financeID){
        OrganisationFinance organisationFinance = organisationsFinanceServices.getOrganisationFinanceByFinanceID(financeID);
        organisationsFinanceRepository.delete(organisationFinance);
        return "Organisational finance deleted successfully.";

    }
}
