package com.pfe.project.repository;

import java.util.Optional;

import com.pfe.project.models.ERole;
import com.pfe.project.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
