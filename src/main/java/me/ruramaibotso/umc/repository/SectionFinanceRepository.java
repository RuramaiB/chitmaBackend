package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SectionFinanceRepository extends JpaRepository<SectionFinance, Integer> {
    List<SectionFinance> findSectionRecordByMembershipNumberOrderByDateOfPaymentDesc(String membershipNumber);
    List<SectionFinance> findBySectionName(String sectionName);
    Page<SectionFinance> findAllByLocalsOrderByDateOfPaymentDesc(Pageable pageable, Locals Local);
    Optional<List<SectionFinance>> findSectionRecordByLocals(Locals local);
//    Optional<List<SectionFinance>> findTopByDateOfPaymentBetweenAndSectionAndFinanceDescription(LocalDateTime startingDate, LocalDateTime endingDate, Section section, FinanceDescription financeDescription);
    Optional<List<SectionFinance>> findAllByDateOfPaymentBetweenAndSectionAndFinanceDescription(LocalDateTime startingDate, LocalDateTime endingDate, Section section, FinanceDescription financeDescription);

    Optional<SectionFinance> findSectionFinanceByFinanceID(Integer financeID);
}
