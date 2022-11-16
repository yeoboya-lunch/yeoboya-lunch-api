package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long>{

    Roles findByRole(Authority role);

}
