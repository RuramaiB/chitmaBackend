package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Organisations;
import me.ruramaibotso.umc.user.Organisation;
import me.ruramaibotso.umc.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganisationsRepository extends JpaRepository<Organisations, Integer> {
    Optional<Organisations> findOrganisationByLocalsAndOrganisation(Locals local, Organisation organisation);
    List<Organisations> findAllByLocals(Locals Local);


    Optional<Organisations> findByOrganisationAndLocals(Organisation organisation, Locals locals);
}
