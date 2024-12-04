package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private RabbitTemplate rabbitTemplate;

    public void notify(ProposalResponseDTO proposal, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "", proposal);
    }
}
