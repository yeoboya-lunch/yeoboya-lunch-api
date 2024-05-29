package com.yeoboya.lunch.config.pricingPlan.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.config.pricingPlan.constants.PricingPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APIKEY_ID")
    private Long id;

    @Column(name="api_key", unique=true, nullable=false)
    private String apiKey;

    @Column(name="pricing_plan", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private PricingPlan pricingPlan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
