package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByRole(Authority role);

    @Override
    void delete(Role role);
}
