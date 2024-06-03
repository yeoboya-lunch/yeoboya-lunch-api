package com.yeoboya.lunch.api.v1.support.repository;

import com.yeoboya.lunch.api.v1.support.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
