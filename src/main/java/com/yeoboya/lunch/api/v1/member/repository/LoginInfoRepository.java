package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.member.domain.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
}
