package com.klesse.proposal_app.scheduler;

import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.repository.ProposalRepository;
import com.klesse.proposal_app.service.NotificationRabbitMQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NoIntegrateProposals {

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitMQService notificationRabbitMQService;

    private final String exchange;

    private final Logger log = LoggerFactory.getLogger(NoIntegrateProposals.class);

    public NoIntegrateProposals(ProposalRepository proposalRepository,
                                NotificationRabbitMQService notificationRabbitMQService,
                                @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationRabbitMQService = notificationRabbitMQService;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void getNoIntegrateProposals() {
        proposalRepository.findAllByIntegrateIsFalse().forEach(proposal -> {
            try {
                notificationRabbitMQService.notify(proposal, exchange);
                proposalRepository.updateStatusIntegrate(proposal.getId(), true);
            } catch (RuntimeException ex) {
                log.error(ex.getMessage());
            }
        });
    }
}
