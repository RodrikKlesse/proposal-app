package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.repository.ProposalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProposalService {

    private ProposalRepository proposalRepository;

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        proposalRepository.save(new Proposal());
        return null;
    }
}
