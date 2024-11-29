package com.klesse.proposal_app.controller;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/proposal")
@AllArgsConstructor
public class ProposalController {

    private ProposalService proposalService;

    @PostMapping("/create")
    public ResponseEntity<ProposalResponseDTO> createProposal(@RequestBody ProposalRequestDTO request) {
        ProposalResponseDTO response = proposalService.createProposal(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProposalResponseDTO>> getAllProposals() {
        return ResponseEntity.ok(proposalService.getAllProposals());
    }
}
