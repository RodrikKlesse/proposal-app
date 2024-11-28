package com.klesse.proposal_app.entity;

import jakarta.persistence.*;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double loanAmount;
    private int paymentTerm;
    private Boolean status;
    private boolean integrate;
    private String description;
    @OneToOne
    @JoinColumn(name = "userId")
    private Users users;

}
