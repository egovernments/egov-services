package org.egov.works.workorder.domain.repository;

import java.util.List;

import org.egov.works.workorder.web.contract.ContractorAdvanceRequisition;
import org.egov.works.workorder.web.contract.ContractorAdvanceSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ContractorAdvanceRequisitionRepository {

    @Autowired
    private ContractorAdvanceJdbcRepository contractorAdvanceJdbcRepository;

    public List<ContractorAdvanceRequisition> searchContractorAdvances(
            final ContractorAdvanceSearchContract contractorAdvanceSearchCriteria,
            final RequestInfo requestInfo) {
        return contractorAdvanceJdbcRepository.searchContractorAdvanceRequisitions(contractorAdvanceSearchCriteria, requestInfo);
    }
}