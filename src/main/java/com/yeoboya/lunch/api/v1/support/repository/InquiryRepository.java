package com.yeoboya.lunch.api.v1.support.repository;

import com.yeoboya.lunch.api.v1.support.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
