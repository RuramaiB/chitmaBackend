package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.OrganisationFinance;
import me.ruramaibotso.umc.model.Organisations;
import me.ruramaibotso.umc.repository.FinanceDescriptionRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.OrganisationsFinanceRepository;
import me.ruramaibotso.umc.repository.OrganisationsRepository;
import me.ruramaibotso.umc.requests.OrganisationsFinanceRequest;
import me.ruramaibotso.umc.user.User;
import me.ruramaibotso.umc.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationsFinanceServices {
    private final OrganisationsFinanceRepository organisationsFinanceRepository;
    private final OrganisationsRepository organisationsRepository;
    private final LocalsRepository localsRepository;
    private final UserRepository userRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;
    public Page<OrganisationFinance> getAllOrganisationsFinance(Integer offset){
        return organisationsFinanceRepository.findAll(PageRequest.of(offset,10));
    }
    public OrganisationFinance addNewOrgFinance(OrganisationsFinanceRequest organisationsFinanceRequest){
        OrganisationFinance organisationFinance = new OrganisationFinance();
        Locals local = localsRepository.findByName(organisationsFinanceRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        Organisations organisations = organisationsRepository.findOrganisationByLocalsAndOrganisation(local,organisationsFinanceRequest.getOrganisation())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));
        User user = userRepository.findUserByMembershipNumber(organisationsFinanceRequest.getMembershipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        FinanceDescription fd = financeDescriptionRepository.findByDescriptionAndLocals(organisationsFinanceRequest.getDescription(), local)
                .orElseThrow(() -> new ResourceNotFoundException("Finance description not found."));
        organisationFinance.setFinanceID(organisationFinance.getFinanceID());
        organisationFinance.setAmount(organisationsFinanceRequest.getAmount());
        organisationFinance.setDateOfPayment(LocalDateTime.now());
        organisationFinance.setPaymentMethod(organisationsFinanceRequest.getPaymentMethod());
        organisationFinance.setCurrency(organisationsFinanceRequest.getCurrency());
        organisationFinance.setPhoneNumber(organisationsFinanceRequest.getPhoneNumber());
        organisationFinance.setOrganisations(organisations);
        organisationFinance.setLocals(local);
        organisationFinance.setUser(user);
        organisationFinance.setFinanceDescription(fd);
        organisationFinance.setMembershipNumber(user.getMembershipNumber());
        return organisationsFinanceRepository.save(organisationFinance);

    }
    public List<OrganisationFinance> getOrganisationFinanceByMembership(String membership){
        return organisationsFinanceRepository.findByMembershipNumberOrderByDateOfPaymentDesc(membership)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Records not found"));
    }
    public OrganisationFinance getOrganisationFinanceByFinanceID(Integer financeID){
        return organisationsFinanceRepository.findOrganisationFinanceByFinanceID(financeID)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Records not found"));
    }

    public Page<OrganisationFinance> getOrganisationsFinanceByLocal(int offset, String local) {
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
       return organisationsFinanceRepository.findAllByLocalsOrderByDateOfPaymentDesc(PageRequest.of(offset,10), locals);

    }
}
