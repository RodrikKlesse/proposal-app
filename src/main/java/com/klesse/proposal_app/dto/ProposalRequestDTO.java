package com.klesse.proposal_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    @NotBlank(message = "LastName is required")
    @Size(min = 3, max = 100, message = "LastName must be between 3 and 100 characters")
    private String lastName;
    @NotBlank(message = "SSN is required")
    @Pattern(regexp = "\\d{11}", message = "SSN must contain exactly 11 digits")
    private String ssn;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    @NotNull(message = "Wage is required")
    @DecimalMin(value = "0.0", message = "Wage cannot be negative")
    private Double wage;
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "100.0", message = "Loan amount must be at least 100.0")
    @DecimalMax(value = "1000000.0", message = "Loan amount cannot exceed 1,000,000.0")
    private Double loanAmount;
    @NotNull(message = "Payment term is required")
    @Min(value = 1, message = "Payment term must be at least 1 month")
    @Max(value = 360, message = "Payment term cannot exceed 360 months")
    private int paymentTerm;
}
