package com.yeoboya.lunch.config.security.domain;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    private String bankName;

    private String accountNumber;

    //fixme member @OneToOne

}
