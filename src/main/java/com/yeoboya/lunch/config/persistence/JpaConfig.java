package com.yeoboya.lunch.config.persistence;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class JpaConfig {

    // set createBy, LastModifiedBy
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    // 사용하지 말자
    @Bean
    Hibernate5Module hibernate5Module(){
        return new Hibernate5Module();
    }

}
