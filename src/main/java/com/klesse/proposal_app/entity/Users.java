package com.klesse.proposal_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String ssn;
    private String phoneNumber;
    private Double wage;

    @OneToOne(mappedBy = "users")
    @JsonBackReference
    @ToString.Exclude
    private Proposal proposal;
}
