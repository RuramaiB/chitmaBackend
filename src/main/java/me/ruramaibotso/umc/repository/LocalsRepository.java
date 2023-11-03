package me.ruramaibotso.umc.repository;

import me.ruramaibotso.umc.model.Locals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalsRepository extends JpaRepository<Locals, Integer> {
    Optional<Locals> findByName(String name);

}
