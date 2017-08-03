package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.BankEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankJdbcRepository;
import org.egov.egf.master.web.contract.BankSearchContract;
import org.egov.egf.master.web.requests.BankRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankRepository {

    @Autowired
    private BankJdbcRepository bankJdbcRepository;
    @Autowired
    private MastersQueueRepository bankQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private BankESRepository bankESRepository;

    public Bank findById(Bank bank) {
        BankEntity entity = bankJdbcRepository.findById(new BankEntity().toEntity(bank));
        return entity.toDomain();

    }

    @Transactional
    public Bank save(Bank bank) {
        BankEntity entity = bankJdbcRepository.create(new BankEntity().toEntity(bank));
        return entity.toDomain();
    }

    @Transactional
    public Bank update(Bank bank) {
        BankEntity entity = bankJdbcRepository.update(new BankEntity().toEntity(bank));
        return entity.toDomain();
    }

    public void add(BankRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("bank_create", request);
        } else {
            message.put("bank_update", request);
        }
        bankQueueRepository.add(message);
    }

    public Pagination<Bank> search(BankSearch domain) {

        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            BankSearchContract bankSearchContract = new BankSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, bankSearchContract);
            return bankESRepository.search(bankSearchContract);
        } else {
            return bankJdbcRepository.search(domain);
        }
    }

}