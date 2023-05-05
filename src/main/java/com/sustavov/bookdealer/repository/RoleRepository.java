package com.sustavov.bookdealer.repository;

import com.sustavov.bookdealer.model.Role;
import com.sustavov.bookdealer.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
