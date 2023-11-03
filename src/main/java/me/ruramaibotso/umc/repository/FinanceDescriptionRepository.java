package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.Locals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinanceDescriptionRepository extends JpaRepository<FinanceDescription, Integer> {

    List<FinanceDescription> findAllByLocals(Locals locals);

    FinanceDescription findByDescription(String financialDescription);
    Optional<FinanceDescription> findByDescriptionAndLocals(String financeDescription, Locals locals);

    List<FinanceDescription> findAllByLocalsAndGrandTargetGreaterThan(Locals locals, Double grandTarget);
}
