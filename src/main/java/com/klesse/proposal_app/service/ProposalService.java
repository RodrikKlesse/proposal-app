package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.mapper.ProposalMapper;
import com.klesse.proposal_app.repository.ProposalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProposalService {

    private ProposalRepository proposalRepository;

    private NotificationService notificationService;

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToProposal(request);
        proposalRepository.save(proposal);

        ProposalResponseDTO response = ProposalMapper.INSTANCE.convertEntityToDto(proposal);

        notificationService.notify(response,"pending-proposal.ex");

        return response;
    }

    public List<ProposalResponseDTO> getAllProposals() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
