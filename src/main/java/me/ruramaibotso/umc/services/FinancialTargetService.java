package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.*;
import me.ruramaibotso.umc.requests.FinancialTargetRequest;
import me.ruramaibotso.umc.user.Organisation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FinancialTargetService {
    private final FinancialTargetsRepository financialTargetsRepository;
    private final LocalsRepository localsRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;
    private final SectionsRepository sectionsRepository;
    private final OrganisationsRepository organisationsRepository;

    public Page<FinancialTargets> getAllFinancialTargetByLocal(String local, Integer offset){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return financialTargetsRepository.findAllByLocals(locals, PageRequest.of(offset, 10));
    }
    public Page<FinancialTargets> getAllFinancialTargetsByLocalAndFinancialDescription(String local, Integer offset, String financialDescription){
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialDescription);
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        return financialTargetsRepository.findAllByLocalsAndFinanceDescription(locals, PageRequest.of(offset, 10), financeDescription);
    }
    public FinancialTargets distributeTargetsToSections(FinancialTargetRequest financialTargetRequest) {
        // Find all sections


        // Find finance description by description
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialTargetRequest.getFinancialDescription());

        // Find local by name or throw an exception if not found
        Locals local = localsRepository.findByName(financialTargetRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        List<Section> sections = sectionsRepository.findAllByLocals(local);
        // Calculate equal target for each section
        double totalTarget = financeDescription.getGrandTarget();
        double equalTarget = totalTarget / sections.size();
        System.out.println("This is the section size: " + sections.size());

        // Initialize variables for tracking remaining target and a list of financial targets
        double remainingTarget = totalTarget;
        List<FinancialTargets> financialTargetsList = new ArrayList<>();

        for (Section s : sections) {
            // Calculate the target for the current section
            double sectionTarget = Math.min(remainingTarget, equalTarget);

            // Create a new FinancialTargets object for the section
            FinancialTargets financialTargets = new FinancialTargets();
            financialTargets.setLevel(Level.Section);
            financialTargets.setLocals(local);
            financialTargets.setTarget(s.getName());
            financialTargets.setAmount(equalTarget);
            financialTargets.setFinanceDescription(financeDescription);

            // Add the financial target to the list
            financialTargetsList.add(financialTargets);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget <= 0) {
                break;
            }
        }

        // Save all the financial targets in the list
        financialTargetsRepository.saveAll(financialTargetsList);

        return financialTargetsList.get(financialTargetsList.size() - 1); // Return the last financial target created
    }

    public FinancialTargets distributeTargetsToOrganisations(FinancialTargetRequest financialTargetRequest) {
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialTargetRequest.getFinancialDescription());
        Locals local = localsRepository.findByName(financialTargetRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        List<Organisations> organisations = organisationsRepository.findAllByLocals(local);

        // Calculate equal target for each section
        double totalTarget = financeDescription.getGrandTarget();
        double equalTarget = totalTarget / organisations.size();
        double remainingTarget = totalTarget;
        List<FinancialTargets> financialTargetsList = new ArrayList<>();

        for (Organisations org : organisations) {
            // Calculate the target for the current section
            double sectionTarget = Math.min(remainingTarget, equalTarget);

            // Create a new FinancialTargets object for the section
            FinancialTargets financialTargets = new FinancialTargets();
            financialTargets.setLevel(Level.Organisation);
            financialTargets.setLocals(local);
            financialTargets.setTarget(String.valueOf(org.getOrganisation()));
            financialTargets.setAmount(equalTarget);
            financialTargets.setFinanceDescription(financeDescription);

            // Add the financial target to the list
            financialTargetsList.add(financialTargets);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget <= 0) {
                break;
            }
        }

        // Save all the financial targets in the list
        financialTargetsRepository.saveAll(financialTargetsList);

        return financialTargetsList.get(financialTargetsList.size() - 1); // Return the last financial target created
    }

    public FinancialTargets distributeTargetsToOrganisationsAndSections(FinancialTargetRequest financialTargetRequest) {
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialTargetRequest.getFinancialDescription());
        Locals local = localsRepository.findByName(financialTargetRequest.getLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        List<Organisations> organisations = organisationsRepository.findAllByLocals(local);
        List<Section> sections = sectionsRepository.findAllByLocals(local);

        // Calculate equal target for each section
        double totalTarget = financeDescription.getGrandTarget();
        double equalTarget = totalTarget / (organisations.size() + sections.size());
        double remainingTarget = totalTarget;
        List<FinancialTargets> financialTargetsList = new ArrayList<>();

        for (Organisations org : organisations) {
            // Calculate the target for the current section
            double sectionTarget = Math.min(remainingTarget, equalTarget);

            // Create a new FinancialTargets object for the section
            FinancialTargets financialTargets = new FinancialTargets();
            financialTargets.setLevel(Level.All);
            financialTargets.setLocals(local);
            financialTargets.setTarget(String.valueOf(org.getOrganisation()));
            financialTargets.setAmount(equalTarget);
            financialTargets.setFinanceDescription(financeDescription);

            // Add the financial target to the list
            financialTargetsList.add(financialTargets);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget <= 0) {
                break;
            }
        }
        for (Section s : sections) {
            // Calculate the target for the current section
            double sectionTarget = Math.min(remainingTarget, equalTarget);

            // Create a new FinancialTargets object for the section
            FinancialTargets financialTargets = new FinancialTargets();
            financialTargets.setLevel(Level.All);
            financialTargets.setLocals(local);
            financialTargets.setTarget(s.getName());
            financialTargets.setAmount(equalTarget);
            financialTargets.setFinanceDescription(financeDescription);

            // Add the financial target to the list
            financialTargetsList.add(financialTargets);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget <= 0) {
                break;
            }
        }

        // Save all the financial targets in the list
        financialTargetsRepository.saveAll(financialTargetsList);

        return financialTargetsList.get(financialTargetsList.size() - 1); // Return the last financial target created
    }

    public List<FinancialTargets> getFinancialTargetsBySectionLevel(String local){
        Locals locals = localsRepository.findByName(local).orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return financialTargetsRepository.findAllByLocalsAndLevel(locals, Level.Section);
    }

    public List<FinancialTargets> getFinancialTargetsAtOrganisationLevel(String local){
        Locals locals = localsRepository.findByName(local).orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return financialTargetsRepository.findAllByLocalsAndLevel(locals, Level.Organisation);
    }
    public List<FinancialTargets> getFinancialTargetsForBoth(String local){
        Locals locals = localsRepository.findByName(local).orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return financialTargetsRepository.findAllByLocalsAndLevel(locals, Level.All);
    }

    public FinancialTargets updateOrganisationFinancialTarget(int id, double amountUpdate ){
        var orgFT = financialTargetsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Financial Target not found"));
//        System.out.println(orgFT.getLocals().getName());
        Locals local = localsRepository.findByName(String.valueOf(orgFT.getLocals().getName()))
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        System.out.println(local);
        List<Organisations> organisations = organisationsRepository.findAllByLocals(local);
//        List<FinancialTargets> financialTargetsLists = financialTargetsRepository.findAll();
        List<FinancialTargets> financialTargetsLists = financialTargetsRepository.findAllByLocalsAndLevel(local, Level.Organisation);
        // Calculate equal target for each section
        double totalTarget = orgFT.getFinanceDescription().getGrandTarget() - amountUpdate;
        System.out.println(totalTarget);
        double equalTarget = totalTarget / (organisations.size() -1);
        System.out.println(equalTarget);
        double remainingTarget = totalTarget;
        List<FinancialTargets> financialTargetsList = new ArrayList<>();

        for (FinancialTargets ftg :financialTargetsLists) {
            // Calculate the target for the current section
            double sectionTarget = Math.min(remainingTarget, equalTarget);
            System.out.println(sectionTarget);
            ftg.setAmount(sectionTarget);
            ftg.setLevel(orgFT.getLevel());
            ftg.setFinanceDescription(orgFT.getFinanceDescription());
            ftg.setLevel(Level.Organisation);
            // Add the financial target to the list
            financialTargetsList.add(ftg);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget == 0) {
                break;
            }
        }
        orgFT.setAmount(amountUpdate);
        financialTargetsRepository.saveAll(financialTargetsList);
        financialTargetsRepository.save(orgFT);

        return orgFT; // Return the last financial target created

    }
    public FinancialTargets updateSectionFinancialTarget(int id, double amountUpdate ){
        var section = financialTargetsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Financial Target not found"));
        Locals local = localsRepository.findByName(String.valueOf(section.getLocals().getName()))
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        List<Section> sections = sectionsRepository.findAllByLocals(local);
        List<FinancialTargets> financialTargetsLists = financialTargetsRepository.findAllByLocalsAndLevel(local, Level.Section);
        double totalTarget = section.getFinanceDescription().getGrandTarget() - amountUpdate;
        System.out.println(totalTarget);
        double equalTarget = totalTarget / (sections.size() -1);
        System.out.println(equalTarget);
        double remainingTarget = totalTarget;
        List<FinancialTargets> financialTargetsList = new ArrayList<>();

        for (FinancialTargets ftg :financialTargetsLists) {
            double sectionTarget = Math.min(remainingTarget, equalTarget);
            System.out.println(sectionTarget);
            ftg.setAmount(sectionTarget);
            ftg.setLevel(section.getLevel());
            ftg.setFinanceDescription(section.getFinanceDescription());
            ftg.setLevel(Level.Section);
            // Add the financial target to the list
            financialTargetsList.add(ftg);

            // Deduct the allocated target from the remaining target
            remainingTarget -= sectionTarget;

            // If remaining target is zero, break the loop
            if (remainingTarget == 0) {
                break;
            }
        }
        section.setAmount(amountUpdate);
        financialTargetsRepository.saveAll(financialTargetsList);
        financialTargetsRepository.save(section);

        return section; // Return the last financial target created

    }



}
