package com.yeoboya.lunch.api.v1.file.repository;

import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileFileRepository extends JpaRepository<MemberProfileFile, Long> {

}
