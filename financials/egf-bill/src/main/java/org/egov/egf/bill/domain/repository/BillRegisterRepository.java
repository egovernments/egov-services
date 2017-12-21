package org.egov.egf.bill.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillRegisterRepository {

    private final BillRegisterJdbcRepository billRegisterJdbcRepository;

    private final BillRegisterQueueRepository billRegisterQueueRepository;

    private final FinancialConfigurationContractRepository financialConfigurationContractRepository;

    private final BillRegisterESRepository billRegisterESRepository;

    @Autowired
    public BillRegisterRepository(final BillRegisterJdbcRepository billRegisterJdbcRepository,
            final BillRegisterQueueRepository billRegisterQueueRepository,
            final FinancialConfigurationContractRepository financialConfigurationContractRepository,
            final BillRegisterESRepository billRegisterESRepository) {
        this.billRegisterJdbcRepository = billRegisterJdbcRepository;
        this.billRegisterQueueRepository = billRegisterQueueRepository;
        this.financialConfigurationContractRepository = financialConfigurationContractRepository;
        this.billRegisterESRepository = billRegisterESRepository;

    }

    @Transactional
    public BillRegisterRequest save(BillRegisterRequest billRegisterRequest) {

        return billRegisterQueueRepository.save(billRegisterRequest);

    }

    @Transactional
    public BillRegisterRequest update(BillRegisterRequest billRegisterRequest) {

        return billRegisterQueueRepository.update(billRegisterRequest);

    }

    public Pagination<BillRegister> search(final BillRegisterSearch domain) {
        if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
                && financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {
            return billRegisterESRepository.search(domain);
        } else
            return billRegisterJdbcRepository.search(domain);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName, final String uniqueFieldValue) {

        return billRegisterJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}