package com.klesse.proposal_app.listener;

import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.repository.ProposalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {

    @Autowired
    private ProposalRepository proposalRepository;

    @RabbitListener(queues = "${rabbitmq.queue.completed.proposal}")
    public void completedProposal(Proposal proposal) {
        proposalRepository.save(proposal);
    }
}
