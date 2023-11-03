package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Pledges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PledgesRepository extends JpaRepository<Pledges, Integer> {
    Page<Pledges> findAllByLocalsOrderByDateOfPaymentDesc(Pageable pageable, Locals locals);
}
