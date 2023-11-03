package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.Response.StatisticsAmounts;
import me.ruramaibotso.umc.model.Statistics;
import me.ruramaibotso.umc.services.StatisticsServices;
import me.ruramaibotso.umc.user.Organisation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/statistics")
@RestController
public class StatisticsController {
    private final StatisticsServices statisticsServices;

    @GetMapping("/getSectionStatsBy/{name}/{financialDescription}/{local}")
    public Statistics getSectionStatsByNameAndFinancialDescription(@PathVariable String name, @PathVariable String financialDescription, @PathVariable String local){
        return statisticsServices.forSectionTargetAgainstFinancialDescription(name, financialDescription, local);
    }
    @GetMapping("/getStatisticsByOrganisationAndFinanceDescription/{organisation}/{financeDescription}/{local}")
    public ResponseEntity<Statistics> getStatisticsByOrganisationAndFinanceDescription(@PathVariable Organisation organisation, @PathVariable String financeDescription, @PathVariable String local){
        return statisticsServices.getStatisticsByOrganisationAndFinanceDescription(organisation, financeDescription, local);
    }
    @GetMapping("/getSectionsStatistics/{financialDescription}/{local}")
    public List<Statistics> getSectionsStatistics(@PathVariable String local, @PathVariable String financialDescription){
        return statisticsServices.getAllSectionsStatistics(financialDescription,local);
    }
    @GetMapping("/getAllOrganisationsStatistics/{financialDescription}/{local}")
    public List<Statistics> getAllOrganisationsStatistics(@PathVariable String local, @PathVariable String financialDescription){
        return statisticsServices.getAllOrganisationsStatistics(financialDescription, local);
    }
//    @GetMapping("/getLocalFinanceStatsBy/{local}/{financeDescription}")
//    public Statistics getLocalFinanceStats(@PathVariable String local, @PathVariable String financeDescription){
//        return statisticsServices.getStatisticsByLocalFinanceAndFinanceDescription(local, financeDescription);
//    }

}
