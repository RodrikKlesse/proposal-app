package com.klesse.proposal_app.repository;

import com.klesse.proposal_app.entity.Proposal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProposalRepository extends CrudRepository<Proposal, Long> {

    List<Proposal> findAllByIntegrateIsFalse();

    @Transactional
    @Modifying
    @Query(value = "UPDATE proposal SET status = :status, description = :description WHERE id = :id", nativeQuery = true)
    void updateProposal(Long id, boolean status, String description);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Proposal SET integrate = :integrate WHERE id = :id")
    void updateStatusIntegrate(Long id, boolean integrate);
}
