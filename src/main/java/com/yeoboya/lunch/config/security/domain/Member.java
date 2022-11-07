package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.domain.BaseTimeEntity;
import com.yeoboya.lunch.config.constants.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String name;
    private String password;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "member")
//    @Builder.Default
//    private List<Order> orders = new ArrayList<Order>();

    @Enumerated(EnumType.STRING)
    private Authority role;

}
