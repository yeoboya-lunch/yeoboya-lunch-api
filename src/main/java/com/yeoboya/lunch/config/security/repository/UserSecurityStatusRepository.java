package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSecurityStatusRepository extends JpaRepository<UserSecurityStatus, Long> {


}
