package com.klesse.proposal_app.controller;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proposal")
@AllArgsConstructor
public class ProposalController {

    private ProposalService proposalService;

    @PostMapping("/create")
    public ResponseEntity<ProposalResponseDTO> createProposal(@RequestBody ProposalRequestDTO request) {
        ProposalResponseDTO response = proposalService.createProposal(request);
        return ResponseEntity.ok(response);
    }
}
