package com.klesse.proposal_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private Users users;

}
