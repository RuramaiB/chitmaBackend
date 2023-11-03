package me.ruramaibotso.umc.user;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAllByLocals(Pageable pageable, Locals Local);
    Optional<User> findUserByMembershipNumber(String membershipNumber);

    Optional<List<User>> findUsersByOrganisation(Organisation organisation);

    Optional<List<User>> findUsersByMembershipStatus(Membership membershipStatus);

    Optional<List<User>> findUsersBySection(Section section);

    @Query(value = "SELECT MAX(id) FROM User")
    Integer findLastGeneratedId();

    Page<User> findUsersByRoleAndLocals(Pageable pageable, Role role, Locals Local);
    Page<User> findAllByRole(Pageable pageable, Role role);
}
