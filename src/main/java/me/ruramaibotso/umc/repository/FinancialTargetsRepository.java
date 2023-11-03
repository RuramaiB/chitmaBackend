package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.requests.FinanceDescriptionRequest;
import me.ruramaibotso.umc.requests.FinancialTargetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialTargetsRepository extends JpaRepository<FinancialTargets, Integer> {
    Page<FinancialTargets> findAllByLocals(Locals locals, Pageable pageable);
    Page<FinancialTargets> findAllByLocalsAndFinanceDescription(Locals locals, Pageable pageable, FinanceDescription financeDescription);
    FinancialTargets findByTargetAndFinanceDescription(String target, FinanceDescription financeDescription);
    FinancialTargets findByLocalsAndFinanceDescription(Locals locals, FinanceDescription financeDescription);

    List<FinancialTargets> findAllByLocalsAndLevel(Locals locals, Level level);

}
