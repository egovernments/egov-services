package org.egov.egf.bill.domain.repository;

import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.persistence.queue.repository.ChecklistQueueRepository;
import org.egov.egf.bill.persistence.repository.ChecklistJdbcRepository;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChecklistRepository {

    @Autowired
    private ChecklistJdbcRepository checklistJdbcRepository;

    @Autowired
    private ChecklistQueueRepository checklistQueueRepository;

    @Transactional
    public ChecklistRequest save(ChecklistRequest checklistRequest) {

        return checklistQueueRepository.save(checklistRequest);

    }

    @Transactional
    public ChecklistRequest update(ChecklistRequest checklistRequest) {

        return checklistQueueRepository.update(checklistRequest);

    }

    public Pagination<Checklist> search(final ChecklistSearch search) {
        return checklistJdbcRepository.search(search);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName, final String uniqueFieldValue) {

        return checklistJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }
}