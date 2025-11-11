package com.klesse.proposal_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProposalResponseDTO {

    private Long id;
    private String name;
    private String lastName;
    private String ssn;
    private String phoneNumber;
    private Double wage;
    private String loanAmount;
    private int paymentTerm;
    private Boolean status;
    private String description;
}
