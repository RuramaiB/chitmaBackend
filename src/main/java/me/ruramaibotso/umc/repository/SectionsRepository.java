package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.model.SectionFinance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionsRepository extends JpaRepository<Section, Integer> {
    Optional<Section> findByName(String name);
    Optional<Section> findByLocalsAndName(Locals locals, String name);
    Page<Section> findAllByLocals(Pageable pageable, Locals Local);
    List<Section> findAllByLocals(Locals locals);
}
