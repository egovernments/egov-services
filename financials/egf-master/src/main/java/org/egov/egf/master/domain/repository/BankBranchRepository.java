package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.BankBranchEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankBranchJdbcRepository;
import org.egov.egf.master.web.contract.BankBranchSearchContract;
import org.egov.egf.master.web.requests.BankBranchRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankBranchRepository {

    @Autowired
    private BankBranchJdbcRepository bankBranchJdbcRepository;

    @Autowired
    private MastersQueueRepository bankBranchQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private BankBranchESRepository bankBranchESRepository;

    public BankBranch findById(BankBranch bankBranch) {
        BankBranchEntity entity = bankBranchJdbcRepository.findById(new BankBranchEntity().toEntity(bankBranch));
        return entity.toDomain();

    }

    public String getNextSequence(){
        return bankBranchJdbcRepository.getSequence(BankBranchEntity.SEQUENCE_NAME);
    }
    
    @Transactional
    public BankBranch save(BankBranch bankBranch) {
        BankBranchEntity entity = bankBranchJdbcRepository.create(new BankBranchEntity().toEntity(bankBranch));
        return entity.toDomain();
    }

    @Transactional
    public BankBranch update(BankBranch bankBranch) {
        BankBranchEntity entity = bankBranchJdbcRepository.update(new BankBranchEntity().toEntity(bankBranch));
        return entity.toDomain();
    }

    public void add(BankBranchRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("bankbranch_create", request);
        } else {
            message.put("bankbranch_update", request);
        }
        bankBranchQueueRepository.add(message);
    }

    public Pagination<BankBranch> search(BankBranchSearch domain) {

        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            BankBranchSearchContract bankBranchSearchContract = new BankBranchSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, bankBranchSearchContract);
            return bankBranchESRepository.search(bankBranchSearchContract);
        } else {
            return bankBranchJdbcRepository.search(domain);
        }

    }

}