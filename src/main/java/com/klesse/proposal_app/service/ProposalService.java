package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.mapper.ProposalMapper;
import com.klesse.proposal_app.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private ProposalRepository proposalRepository;

    private NotificationService notificationService;

    private String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationService notificationService,
                           @Value("${rabbitMQ.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
        this.exchange = exchange;
    }

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToProposal(request);
        proposalRepository.save(proposal);

        notifierRabbitMQ(proposal);

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }

    private void notifierRabbitMQ(Proposal proposal) {
        try {
            notificationService.notify(proposal, exchange);
        } catch (RuntimeException ex) {
            proposal.setIntegrate(false);
            proposalRepository.save(proposal);
        }
    }

    public List<ProposalResponseDTO> getAllProposals() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
