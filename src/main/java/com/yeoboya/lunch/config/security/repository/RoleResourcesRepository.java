package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.RoleResources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleResourcesRepository extends JpaRepository<RoleResources, Long> {
}