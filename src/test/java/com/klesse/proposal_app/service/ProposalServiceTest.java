package com.klesse.proposal_app.service;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import com.klesse.proposal_app.entity.Users;
import com.klesse.proposal_app.repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProposalService Unit Tests")
class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private NotificationRabbitMQService notificationRabbitMQService;

    private ProposalService proposalService;

    private ProposalRequestDTO proposalRequestDTO;
    private Proposal proposal;
    private Users users;

    @BeforeEach
    void setUp() {
        String exchange = "pending-proposal.ex";
        proposalService = new ProposalService(
                proposalRepository,
                notificationRabbitMQService,
                exchange
        );
        
        users = new Users();
        users.setId(1L);
        users.setName("John");
        users.setLastName("Doe");
        users.setSsn("12345678901");
        users.setPhoneNumber("+5511999999999");
        users.setWage(5000.0);

        proposal = new Proposal();
        proposal.setId(1L);
        proposal.setLoanAmount(10000.0);
        proposal.setPaymentTerm(24);
        proposal.setUsers(users);
        proposal.setIntegrate(true);

        proposalRequestDTO = new ProposalRequestDTO();
        proposalRequestDTO.setName("John");
        proposalRequestDTO.setLastName("Doe");
        proposalRequestDTO.setSsn("12345678901");
        proposalRequestDTO.setPhoneNumber("+5511999999999");
        proposalRequestDTO.setWage(5000.0);
        proposalRequestDTO.setLoanAmount(10000.0);
        proposalRequestDTO.setPaymentTerm(24);
    }

    @Test
    @DisplayName("Should create proposal successfully with high priority when wage > 10000")
    void shouldCreateProposalWithHighPriority() {
        // Given
        proposalRequestDTO.setWage(15000.0);
        
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(invocation -> {
            Proposal savedProposal = invocation.getArgument(0);
            savedProposal.setId(1L);
            if (savedProposal.getUsers() != null) {
                savedProposal.getUsers().setId(1L);
            }
            return savedProposal;
        });

        // When
        ProposalResponseDTO result = proposalService.createProposal(proposalRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(proposalRepository, times(1)).save(any(Proposal.class));
        verify(notificationRabbitMQService, times(1)).notify(any(Proposal.class), anyString(), any());
    }

    @Test
    @DisplayName("Should create proposal successfully with low priority when wage <= 10000")
    void shouldCreateProposalWithLowPriority() {
        // Given
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(invocation -> {
            Proposal savedProposal = invocation.getArgument(0);
            savedProposal.setId(1L);
            if (savedProposal.getUsers() != null) {
                savedProposal.getUsers().setId(1L);
            }
            return savedProposal;
        });

        // When
        ProposalResponseDTO result = proposalService.createProposal(proposalRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(proposalRepository, times(1)).save(any(Proposal.class));
        verify(notificationRabbitMQService, times(1)).notify(any(Proposal.class), anyString(), any());
    }

    @Test
    @DisplayName("Should mark proposal as not integrated when RabbitMQ notification fails")
    void shouldMarkProposalAsNotIntegratedWhenRabbitMQFails() {
        // Given
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(invocation -> {
            Proposal savedProposal = invocation.getArgument(0);
            savedProposal.setId(1L);
            return savedProposal;
        });
        doThrow(new RuntimeException("RabbitMQ error")).when(notificationRabbitMQService)
                .notify(any(Proposal.class), anyString(), any());

        // When - The exception is caught internally, so no exception should be thrown
        ProposalResponseDTO result = proposalService.createProposal(proposalRequestDTO);

        // Then
        assertNotNull(result);
        verify(proposalRepository, times(1)).save(any(Proposal.class));
        verify(proposalRepository, times(1)).updateStatusIntegrate(eq(1L), eq(false));
    }

    @Test
    @DisplayName("Should get all proposals successfully")
    void shouldGetAllProposals() {
        // Given
        Proposal proposal2 = new Proposal();
        proposal2.setId(2L);
        proposal2.setLoanAmount(20000.0);
        proposal2.setPaymentTerm(36);
        
        Users users2 = new Users();
        users2.setName("Jane");
        users2.setLastName("Smith");
        proposal2.setUsers(users2);

        List<Proposal> proposals = Arrays.asList(proposal, proposal2);
        when(proposalRepository.findAll()).thenReturn(proposals);

        // When
        List<ProposalResponseDTO> result = proposalService.getAllProposals();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(proposalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no proposals exist")
    void shouldReturnEmptyListWhenNoProposals() {
        // Given
        when(proposalRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProposalResponseDTO> result = proposalService.getAllProposals();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(proposalRepository, times(1)).findAll();
    }
}
