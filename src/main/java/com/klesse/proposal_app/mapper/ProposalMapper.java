package com.klesse.proposal_app.mapper;

import com.klesse.proposal_app.dto.ProposalRequestDTO;
import com.klesse.proposal_app.entity.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProposalMapper {

    @Mapping(target = "users.name", source = "name")
    @Mapping(target = "users.lastName", source = "lastName")
    @Mapping(target = "users.ssn", source = "ssn")
    @Mapping(target = "users.phoneNumber", source = "phoneNumber")
    @Mapping(target = "users.wage", source = "wage")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "integrate", ignore = true)
    @Mapping(target = "description", ignore = true)
    Proposal convertDtoToProposal(ProposalRequestDTO request);
}
