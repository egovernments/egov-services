package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.AccountDetailTypeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountDetailTypeJdbcRepository;
import org.egov.egf.master.web.contract.AccountDetailTypeSearchContract;
import org.egov.egf.master.web.requests.AccountDetailTypeRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailTypeRepository {

    @Autowired
    private AccountDetailTypeJdbcRepository accountDetailTypeJdbcRepository;
    @Autowired
    private MastersQueueRepository accountDetailTypeQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private AccountDetailTypeESRepository accountDetailTypeESRepository;

    public AccountDetailType findById(AccountDetailType accountDetailType) {
        AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
                .findById(new AccountDetailTypeEntity().toEntity(accountDetailType));
        return entity.toDomain();

    }
    
    public String getNextSequence(){
        return accountDetailTypeJdbcRepository.getSequence(AccountDetailTypeEntity.SEQUENCE_NAME);
    }

    @Transactional
    public AccountDetailType save(AccountDetailType accountDetailType) {
        AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
                .create(new AccountDetailTypeEntity().toEntity(accountDetailType));
        return entity.toDomain();
    }

    @Transactional
    public AccountDetailType update(AccountDetailType accountDetailType) {
        AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
                .update(new AccountDetailTypeEntity().toEntity(accountDetailType));
        return entity.toDomain();
    }

    public void add(AccountDetailTypeRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("accountdetailtype_create", request);
        } else {
            message.put("accountdetailtype_update", request);
        }
        accountDetailTypeQueueRepository.add(message);
    }

    public Pagination<AccountDetailType> search(AccountDetailTypeSearch domain) {
        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            AccountDetailTypeSearchContract accountCodeTypeSearchContract = new AccountDetailTypeSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, accountCodeTypeSearchContract);
            return accountDetailTypeESRepository.search(accountCodeTypeSearchContract);
        } else {
            return accountDetailTypeJdbcRepository.search(domain);
        }

    }

    public boolean uniqueCheck(String fieldName, AccountDetailType accountDetailType) {
        return accountDetailTypeJdbcRepository.uniqueCheck(fieldName, new AccountDetailTypeEntity().toEntity(accountDetailType));
    }

}