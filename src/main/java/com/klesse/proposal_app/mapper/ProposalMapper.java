package com.klesse.proposal_app.mapper;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.dto.ProposalResponseDTO;
import com.klesse.proposal_app.entity.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.NumberFormat;
import java.util.List;

@Mapper
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    @Mapping(target = "users.name", source = "name")
    @Mapping(target = "users.lastName", source = "lastName")
    @Mapping(target = "users.ssn", source = "ssn")
    @Mapping(target = "users.phoneNumber", source = "phoneNumber")
    @Mapping(target = "users.wage", source = "wage")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "integrate", constant = "true")
    @Mapping(target = "description", ignore = true)
    Proposal convertDtoToProposal(ProposalRequestDTO request);

    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "ssn", source = "users.ssn")
    @Mapping(target = "phoneNumber", source = "users.phoneNumber")
    @Mapping(target = "wage", source = "users.wage")
    @Mapping(target = "loanAmount", expression = "java(setLoanAmount(proposal))")
    ProposalResponseDTO convertEntityToDto(Proposal proposal);

    List<ProposalResponseDTO> convertListEntityToListDto(Iterable<Proposal> proposals);

    default String setLoanAmount(Proposal proposal) {
        return NumberFormat.getCurrencyInstance().format(proposal.getLoanAmount());
    }
}
