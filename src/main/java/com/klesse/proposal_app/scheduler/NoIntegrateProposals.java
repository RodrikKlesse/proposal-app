package com.klesse.proposal_app.scheduler;

import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.repository.ProposalRepository;
import com.klesse.proposal_app.service.NotificationRabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

@Slf4j
@Component
public class NoIntegrateProposals {

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitMQService notificationRabbitMQService;

    private final RedisTemplate<String, String> redisTemplate;

    private final String exchange;

    private static final String LOCK_KEY = "proposal:retry:lock";
    private static final long LOCK_TIMEOUT_SECONDS = 50;

    public NoIntegrateProposals(ProposalRepository proposalRepository,
                                NotificationRabbitMQService notificationRabbitMQService, RedisTemplate<String, String> redisTemplate,
                                @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationRabbitMQService = notificationRabbitMQService;
        this.redisTemplate = redisTemplate;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void getNoIntegrateProposals() {
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(LOCK_KEY, "locked", Duration.ofSeconds(LOCK_TIMEOUT_SECONDS));

        if (Boolean.TRUE.equals(lockAcquired)) {
            try {
                List<Proposal> proposals = proposalRepository.findAllByIntegrateIsFalse();

                if (proposals.isEmpty()) {
                    log.debug("No proposals with integrate=false found");
                    return;
                }

                log.info("Processing {} proposals with integrate=false", proposals.size());

                int successCount = 0;
                int failureCount = 0;

                for (Proposal proposal : proposals) {
                    try {
                        notificationRabbitMQService.notify(proposal, exchange);
                        proposalRepository.updateStatusIntegrate(proposal.getId(), true);

                        successCount++;
                        log.debug("Proposal {} marked as integrated", proposal.getId());
                    } catch (RuntimeException ex) {
                        failureCount++;
                        log.error("Failed to process proposal {}: {}", proposal.getId(), ex.getMessage(), ex);
                    }
                }
                log.info("Processed proposals: {} successful, {} failed", successCount, failureCount);
            } catch (Exception e) {
                log.error("Unexpected error in scheduler", e);
            } finally {
                try {
                    redisTemplate.delete(LOCK_KEY);
                } catch (Exception e) {
                    log.warn("Failed to release lock", e);
                }
            }
        } else {
            log.debug("Lock already acquired by another instance, skipping execution");
        }
    }
}
