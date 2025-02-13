package com.klesse.proposal_app.listener;

import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.mapper.ProposalMapper;
import com.klesse.proposal_app.repository.ProposalRepository;
import com.klesse.proposal_app.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private WebSocketService webSocketService;

    @RabbitListener(queues = "${rabbitmq.queue.completed.proposal}")
    public void completedProposal(Proposal proposal) {
        updateProposal(proposal);

        webSocketService.notifier(ProposalMapper.INSTANCE.convertEntityToDto(proposal));
    }

    private void updateProposal(Proposal proposal) {
        proposalRepository.updateProposal(proposal.getId(), proposal.getStatus(), proposal.getDescription());
    }
}
