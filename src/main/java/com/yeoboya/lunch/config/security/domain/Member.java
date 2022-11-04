package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.config.constants.Authority;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String name;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();

    @Enumerated(EnumType.STRING)
    private Authority role;

}
