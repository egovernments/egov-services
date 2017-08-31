package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.AccountEntitySearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.AccountEntityEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountEntityJdbcRepository;
import org.egov.egf.master.web.contract.AccountEntitySearchContract;
import org.egov.egf.master.web.requests.AccountEntityRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountEntityRepository {

    @Autowired
    private AccountEntityJdbcRepository accountEntityJdbcRepository;

    @Autowired
    private MastersQueueRepository accountEntityQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private AccountEntityESRepository accountEntityESRepository;

    public AccountEntity findById(AccountEntity accountEntity) {
        AccountEntityEntity entity = accountEntityJdbcRepository
                .findById(new AccountEntityEntity().toEntity(accountEntity));
        return entity.toDomain();

    }
    
    public String getNextSequence(){
        return accountEntityJdbcRepository.getSequence(AccountEntityEntity.SEQUENCE_NAME);
    }

    @Transactional
    public AccountEntity save(AccountEntity accountEntity) {
        AccountEntityEntity entity = accountEntityJdbcRepository
                .create(new AccountEntityEntity().toEntity(accountEntity));
        return entity.toDomain();
    }

    @Transactional
    public AccountEntity update(AccountEntity accountEntity) {
        AccountEntityEntity entity = accountEntityJdbcRepository
                .update(new AccountEntityEntity().toEntity(accountEntity));
        return entity.toDomain();
    }

    public void add(AccountEntityRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("accountentity_create", request);
        } else {
            message.put("accountentity_update", request);
        }
        accountEntityQueueRepository.add(message);
    }

    public Pagination<AccountEntity> search(AccountEntitySearch domain) {

        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            AccountEntitySearchContract accountEntitySearchContract = new AccountEntitySearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, accountEntitySearchContract);
            return accountEntityESRepository.search(accountEntitySearchContract);
        } else {
            return accountEntityJdbcRepository.search(domain);
        }

    }

    public boolean uniqueCheck(String fieldName, AccountEntity accountEntity) {
        return accountEntityJdbcRepository.uniqueCheck(fieldName, new AccountEntityEntity().toEntity(accountEntity));
    }

}