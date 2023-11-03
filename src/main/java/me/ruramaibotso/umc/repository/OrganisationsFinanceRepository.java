package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrganisationsFinanceRepository extends JpaRepository<OrganisationFinance, Integer> {
    Optional<List<OrganisationFinance>> findByMembershipNumberOrderByDateOfPaymentDesc(String membership);

    Optional<List<OrganisationFinance>> findByOrganisationsLocals(Locals local);
    Page<OrganisationFinance> findAllByLocalsOrderByDateOfPaymentDesc(Pageable pageable, Locals Local);

    Optional<OrganisationFinance> findOrganisationFinanceByFinanceID(Integer financeID);
    Optional<List<OrganisationFinance>> findAllByDateOfPaymentBetweenAndOrganisationsAndFinanceDescription(LocalDateTime from, LocalDateTime now, Organisations organisations, FinanceDescription fD);
}
