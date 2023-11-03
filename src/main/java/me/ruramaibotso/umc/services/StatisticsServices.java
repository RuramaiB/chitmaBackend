package me.ruramaibotso.umc.services;

import jakarta.persistence.ElementCollection;
import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.Response.StatisticsAmounts;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.*;
import me.ruramaibotso.umc.user.Organisation;
import org.hibernate.mapping.Array;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatisticsServices {
    private final FinancialTargetsRepository financialTargetsRepository;
    private final FinanceDescriptionRepository financeDescriptionRepository;
    private final LocalsRepository localsRepository;
    private final SectionsRepository sectionsRepository;
    private final SectionFinanceRepository sectionFinanceRepository;
    private final OrganisationsRepository organisationsRepository;
    private final OrganisationsFinanceRepository organisationsFinanceRepository;
    private final LocalFinanceRepository localFinanceRepository;

    public Statistics forSectionTargetAgainstFinancialDescription(String name, String financialDescription, String locals){
        Statistics statistics = new Statistics();
        Section section = sectionsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found."));
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialDescription);
        FinancialTargets financialTargets = financialTargetsRepository.findByTargetAndFinanceDescription(section.getName(), financeDescription);
        StatisticsAmounts statisticsAmounts = calculateSectionAmountsByFinancialDescription(locals, section.getName(), financeDescription.getDescription());
        int g = (int) financeDescription.getGrandTarget();
        int allocated = (int) financialTargets.getAmount();
        int paidInUSD = statisticsAmounts.getUSD();
        int paidInRTGS = statisticsAmounts.getRTGS();
        int remainder = allocated - paidInUSD;

        var completed = (paidInUSD * 100)/allocated;
        System.out.println("This is " + completed);
        statistics.setDescription(section.getName());
        statistics.setAllocated(allocated);
        statistics.setPaidInUSD(paidInUSD);
        statistics.setPaidInRTGS(paidInRTGS);
        statistics.setRemaining(remainder);
        statistics.setPercentage(String.valueOf(completed + "%"));
        statistics.setPercentageRemainder(String.valueOf((100-completed) + "%"));
        return statistics;
    }
    public ResponseEntity<Statistics> getStatisticsByOrganisationAndFinanceDescription(Organisation organisation, String financeDescription, String local){
        Statistics statistics = new Statistics();
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        Organisations organisations = organisationsRepository.findByOrganisationAndLocals(organisation, locals)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation not found"));
        FinanceDescription fd = financeDescriptionRepository.findByDescriptionAndLocals(financeDescription, locals)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
        FinancialTargets financialTargets = financialTargetsRepository.findByTargetAndFinanceDescription(String.valueOf(organisations.getOrganisation()), fd);
        StatisticsAmounts statisticsAmounts = calculateOrganisationsAmountsByFinancialDescription(locals.getName(), organisations.getOrganisation(), financeDescription);

        int allocated = (int) financialTargets.getAmount();
        int paidInUSD = statisticsAmounts.getUSD();
        int paidInRTGS = statisticsAmounts.getRTGS();
        int remainder = allocated - paidInUSD;

        var completed = (paidInUSD * 100)/allocated;
        System.out.println("This is " + completed);
        statistics.setDescription(String.valueOf(organisations.getOrganisation()));
        statistics.setAllocated(allocated);
        statistics.setPaidInUSD(paidInUSD);
        statistics.setPaidInRTGS(paidInRTGS);
        statistics.setRemaining(remainder);
        statistics.setPercentage(String.valueOf(completed + "%"));
        statistics.setPercentageRemainder(String.valueOf((100-completed) + "%"));
        return  ResponseEntity.ok(statistics);
    }
    public List<Statistics> getAllSectionsStatistics(String financialDescription, String local){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        List<Section> sections = sectionsRepository.findAllByLocals(locals);
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialDescription);
        List<Statistics> sectionsStatistics = new ArrayList<>();

        for (int i = 0; i < sections.size(); i++) {
            FinancialTargets financialTargets = financialTargetsRepository.findByTargetAndFinanceDescription(sections.get(i).getName(), financeDescription);
            StatisticsAmounts statisticsAmounts = calculateSectionAmountsByFinancialDescription(locals.getName(), sections.get(i).getName(), financeDescription.getDescription());
            Statistics statistics = new Statistics();
            int allocated = (int) financialTargets.getAmount();
            int paidInUSD = statisticsAmounts.getUSD();
            int paidInRTGS = statisticsAmounts.getRTGS();
            int remainder = allocated - paidInUSD;
            var completed = (paidInUSD * 100)/allocated;
            statistics.setDescription(financialTargets.getTarget());
            statistics.setAllocated(allocated);
            statistics.setPaidInUSD(paidInUSD);
            statistics.setPaidInRTGS(paidInRTGS);
            statistics.setRemaining(remainder);
            statistics.setPercentage(String.valueOf(completed + "%"));
            statistics.setPercentageRemainder(String.valueOf((100-completed) + "%"));
            System.out.println(statistics.getDescription() + " " + statistics.getPercentage() + " " + statistics.getPercentageRemainder());
            sectionsStatistics.add(statistics);
        }
        return sectionsStatistics;

    }
    public List<Statistics> getAllOrganisationsStatistics(String financialDescription, String local){

        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        List<Organisations> organisations = organisationsRepository.findAllByLocals(locals);
        FinanceDescription financeDescription = financeDescriptionRepository.findByDescription(financialDescription);
        List<Statistics> orgStatistics = new ArrayList<>();

        for (int i = 0; i < organisations.size(); i++) {
            FinancialTargets financialTargets = financialTargetsRepository.findByTargetAndFinanceDescription(String.valueOf(organisations.get(i).getOrganisation()), financeDescription);
            StatisticsAmounts statisticsAmounts = calculateOrganisationsAmountsByFinancialDescription(locals.getName(), organisations.get(i).getOrganisation(), financeDescription.getDescription());
            Statistics statistics = new Statistics();
            int allocated = (int) financialTargets.getAmount();
            int paidInUSD = statisticsAmounts.getUSD();
            int paidInRTGS = statisticsAmounts.getRTGS();
            int remainder = allocated - paidInUSD;
            var completed = (paidInUSD * 100)/allocated;
            statistics.setDescription(financialTargets.getTarget());
            statistics.setAllocated(allocated);
            statistics.setPaidInUSD(paidInUSD);
            statistics.setPaidInRTGS(paidInRTGS);
            statistics.setRemaining(remainder);
            statistics.setPercentage(String.valueOf(completed + "%"));
            statistics.setPercentageRemainder(String.valueOf((100-completed) + "%"));
            System.out.println(statistics.getDescription() + " " + statistics.getPercentage() + " " + statistics.getPercentageRemainder());
            orgStatistics.add(statistics);
        }
        return orgStatistics;

    }
    public StatisticsAmounts calculateSectionAmountsByFinancialDescription(String local, String section, String financeDescription){

        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        Section sections = sectionsRepository.findByName(section)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found."));
        FinanceDescription fD = financeDescriptionRepository.findByDescriptionAndLocals(financeDescription, locals)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
        List<SectionFinance> sectionFinances = sectionFinanceRepository.findAllByDateOfPaymentBetweenAndSectionAndFinanceDescription(LocalDateTime.from(fD.getKickOff()), LocalDateTime.now(), sections, fD)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
        System.out.println(sectionFinances.size());
        double USD = 0;
        double RTGS = 0;
        StatisticsAmounts statisticsAmounts = new StatisticsAmounts();
        for (SectionFinance scf: sectionFinances) {
            if(Objects.equals(scf.getCurrency(), "USD") || Objects.equals(scf.getCurrency(), "usd")) {
                USD += scf.getAmount();
                statisticsAmounts.setUSD((int) USD);
            } else if (Objects.equals(scf.getCurrency(), "ZWL") || Objects.equals(scf.getCurrency(), "RTGS")) {
                RTGS += scf.getAmount();
                statisticsAmounts.setRTGS((int) RTGS);
            }
        }


        return statisticsAmounts;
    }
    public StatisticsAmounts calculateOrganisationsAmountsByFinancialDescription(String local, Organisation organisation, String financeDescription){

        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        Organisations organisations = organisationsRepository.findByOrganisationAndLocals(organisation, locals)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found."));
        FinanceDescription fD = financeDescriptionRepository.findByDescriptionAndLocals(financeDescription, locals)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
        List<OrganisationFinance> organisationFinances = organisationsFinanceRepository.findAllByDateOfPaymentBetweenAndOrganisationsAndFinanceDescription(LocalDateTime.from(fD.getKickOff()), LocalDateTime.now(), organisations, fD)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));

        double USD = 0;
        double RTGS = 0;
        StatisticsAmounts statisticsAmounts = new StatisticsAmounts();
        for (OrganisationFinance orgFinance: organisationFinances ) {
            if(Objects.equals(orgFinance.getCurrency(), "USD") || Objects.equals(orgFinance.getCurrency(), "usd")) {
                USD += orgFinance.getAmount();
                statisticsAmounts.setUSD((int) USD);
            } else if (Objects.equals(orgFinance.getCurrency(), "ZWL") || Objects.equals(orgFinance.getCurrency(), "RTGS")) {
                RTGS += orgFinance.getAmount();
            statisticsAmounts.setRTGS((int) RTGS);
            }
        }
        return statisticsAmounts;

    }
//    public StatisticsAmounts calculateLocalAmountsByFinancialDescription(String local, String financeDescription){
//        Locals locals = localsRepository.findByName(local)
//                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
//        FinanceDescription fD = financeDescriptionRepository.findByDescriptionAndLocals(financeDescription, locals)
//                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
//        List<LocalFinance> localFinances = localFinanceRepository.findAllByDateOfPaymentBetweenAndLocalsAndFinanceDescription(LocalDateTime.from(fD.getKickOff()), LocalDateTime.now(), locals, fD)
//                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
//
//        double USD = 0;
//        double RTGS = 0;
//        StatisticsAmounts statisticsAmounts = new StatisticsAmounts();
//        for (LocalFinance lf: localFinances ) {
//            if(Objects.equals(lf.getCurrency(), "USD") || Objects.equals(lf.getCurrency(), "usd")) {
//                USD += lf.getAmount();
//                statisticsAmounts.setUSD((int) USD);
//            } else if (Objects.equals(lf.getCurrency(), "ZWL") || Objects.equals(lf.getCurrency(), "RTGS")) {
//                RTGS += lf.getAmount();
//                statisticsAmounts.setRTGS((int) RTGS);
//            }
//        }
//        return statisticsAmounts;
//    }
//    public Statistics getStatisticsByLocalFinanceAndFinanceDescription(String local, String financeDescription){
//        Statistics statistics = new Statistics();
//        Locals locals = localsRepository.findByName(local)
//                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
//        FinanceDescription fd = financeDescriptionRepository.findByDescriptionAndLocals(financeDescription, locals)
//                .orElseThrow(() -> new ResourceNotFoundException("Finance Description not found"));
//        FinancialTargets financialTargets = financialTargetsRepository.findByLocalsAndFinanceDescription(locals, fd);
//        StatisticsAmounts statisticsAmounts = calculateLocalAmountsByFinancialDescription(locals.getName(), financeDescription);
//        int allocated = (int) financialTargets.getAmount();
//        int paidInUSD = statisticsAmounts.getUSD();
//        int paidInRTGS = statisticsAmounts.getRTGS();
//        int remainder = allocated - paidInUSD;
//
//        var completed = (paidInUSD * 100)/allocated;
//        System.out.println("This is " + completed);
//        statistics.setDescription(String.valueOf(financialTargets.getLocals()));
//        statistics.setAllocated(allocated);
//        statistics.setPaidInUSD(paidInUSD);
//        statistics.setPaidInRTGS(paidInRTGS);
//        statistics.setRemaining(remainder);
//        statistics.setPercentage(String.valueOf(completed + "%"));
//        statistics.setPercentageRemainder(String.valueOf((100-completed) + "%"));
//        return statistics;
//    }
}
