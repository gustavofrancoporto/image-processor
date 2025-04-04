package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}