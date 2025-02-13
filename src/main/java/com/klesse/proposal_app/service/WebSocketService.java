package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifier(ProposalResponseDTO proposal) {
        messagingTemplate.convertAndSend("/proposals", proposal);
    }
}
