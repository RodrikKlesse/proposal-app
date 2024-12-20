package com.klesse.proposal_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalRequestDTO {

    private String name;
    private String lastName;
    private String ssn;
    private String phoneNumber;
    private Double wage;
    private Double loanAmount;
    private int paymentTerm;
}
