package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.BankAccountEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankAccountJdbcRepository;
import org.egov.egf.master.web.contract.BankAccountSearchContract;
import org.egov.egf.master.web.requests.BankAccountRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountRepository {

    @Autowired
    private BankAccountJdbcRepository bankAccountJdbcRepository;

    @Autowired
    private MastersQueueRepository bankAccountQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private BankAccountESRepository bankAccountESRepository;

    public BankAccount findById(BankAccount bankAccount) {
        BankAccountEntity entity = bankAccountJdbcRepository.findById(new BankAccountEntity().toEntity(bankAccount));
        return entity.toDomain();

    }

    public String getNextSequence(){
        return bankAccountJdbcRepository.getSequence(BankAccountEntity.SEQUENCE_NAME);
    }
    
    @Transactional
    public BankAccount save(BankAccount bankAccount) {
        BankAccountEntity entity = bankAccountJdbcRepository.create(new BankAccountEntity().toEntity(bankAccount));
        return entity.toDomain();
    }

    @Transactional
    public BankAccount update(BankAccount bankAccount) {
        BankAccountEntity entity = bankAccountJdbcRepository.update(new BankAccountEntity().toEntity(bankAccount));
        return entity.toDomain();
    }

    public void add(BankAccountRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("bankaccount_create", request);
        } else {
            message.put("bankaccount_update", request);
        }
        bankAccountQueueRepository.add(message);
    }

    public Pagination<BankAccount> search(BankAccountSearch domain) {

        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            BankAccountSearchContract bankAccountSearchContract = new BankAccountSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, bankAccountSearchContract);
            return bankAccountESRepository.search(bankAccountSearchContract);
        } else {
            return bankAccountJdbcRepository.search(domain);
        }

    }

    public boolean uniqueCheck(String fieldName, BankAccount bankAccount) {
        return bankAccountJdbcRepository.uniqueCheck(fieldName, new BankAccountEntity().toEntity(bankAccount));
    }

}