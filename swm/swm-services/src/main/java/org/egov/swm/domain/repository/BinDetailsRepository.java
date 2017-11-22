package org.egov.swm.domain.repository;

import java.util.List;

import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.persistence.repository.BinDetailsJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinDetailsRepository {

    @Autowired
    private BinDetailsJdbcRepository binDetailsJdbcRepository;

    public List<BinDetails> search(final BinDetailsSearch binDetailsSearch) {
        return binDetailsJdbcRepository.search(binDetailsSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return binDetailsJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

}