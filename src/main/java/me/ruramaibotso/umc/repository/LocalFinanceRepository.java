package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.LocalFinance;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

public interface LocalFinanceRepository extends JpaRepository<LocalFinance, Integer> {
    Page<LocalFinance> findAllByLocals(Pageable pageable, Locals Local);
    Optional<List<LocalFinance>> findLocalFinanceByMembershipNumberOrderByDateOfPaymentDesc(String membershipNumber);
    Optional<LocalFinance> findLocalFinanceByFinanceID(Integer financeID);
    Page<LocalFinance> findAllByLocalsOrderByDateOfPaymentDesc(Pageable pageable, Locals locals);

    Optional<List<LocalFinance>> findAllByDateOfPaymentBetweenAndLocalsAndFinanceDescription(LocalDateTime from, LocalDateTime now, Locals local, FinanceDescription fD);
}
