package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.mapper.ProposalMapper;
import com.klesse.proposal_app.repository.ProposalRepository;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitMQService notificationRabbitMQService;

    private final String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationRabbitMQService notificationRabbitMQService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationRabbitMQService = notificationRabbitMQService;
        this.exchange = exchange;
    }

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToProposal(request);
        proposalRepository.save(proposal);

        int priority = proposal.getUsers().getWage() > 10000 ? 10 : 5;
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        };

        notifierRabbitMQ(proposal, messagePostProcessor);

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }

    private void notifierRabbitMQ(Proposal proposal, MessagePostProcessor messagePostProcessor) {
        try {
            notificationRabbitMQService.notify(proposal, exchange, messagePostProcessor);
        } catch (RuntimeException ex) {
            proposalRepository.updateStatusIntegrate(proposal.getId(), false);
        }
    }

    public List<ProposalResponseDTO> getAllProposals() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
