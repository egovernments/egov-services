package org.egov.egf.bill.domain.repository;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillRegisterRepository {

    private final BillRegisterJdbcRepository billRegisterJdbcRepository;

    private final BillRegisterQueueRepository billRegisterQueueRepository;

    @Autowired
    public BillRegisterRepository(final BillRegisterJdbcRepository billRegisterJdbcRepository,
            final BillRegisterQueueRepository billRegisterQueueRepository,
            final BillRegisterESRepository billRegisterESRepository) {
        this.billRegisterJdbcRepository = billRegisterJdbcRepository;
        this.billRegisterQueueRepository = billRegisterQueueRepository;
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
        return billRegisterJdbcRepository.search(domain);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName, final String uniqueFieldValue) {

        return billRegisterJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}