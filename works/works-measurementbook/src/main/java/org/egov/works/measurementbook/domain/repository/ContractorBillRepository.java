package org.egov.works.measurementbook.domain.repository;

import java.util.List;

import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.ContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.MBForContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ContractorBillRepository {
    
    @Autowired
    private ContractorBillJdbcRepository contractorBillJdbcRepository;
    
    public List<ContractorBill> searchContractorBills(final ContractorBillSearchContract contractorBillSearchContract, final RequestInfo requestInfo) {
        return contractorBillJdbcRepository.searchContractorBills(contractorBillSearchContract, requestInfo);
    }
    
    public List<ContractorBill> searchContractorBillsByMb(final MBForContractorBillSearchContract mbForContractorBillSearchContract, final RequestInfo requestInfo) {
        return contractorBillJdbcRepository.searchContractorBillsByMb(mbForContractorBillSearchContract, requestInfo);
    }
}
