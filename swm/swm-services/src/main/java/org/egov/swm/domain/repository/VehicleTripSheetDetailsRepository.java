package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleTripSheetDetails;
import org.egov.swm.domain.model.VehicleTripSheetDetailsSearch;
import org.egov.swm.persistence.queue.repository.VehicleTripSheetDetailsQueueRepository;
import org.egov.swm.persistence.repository.VehicleTripSheetDetailsJdbcRepository;
import org.egov.swm.web.requests.VehicleTripSheetDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleTripSheetDetailsRepository {

    @Autowired
    private VehicleTripSheetDetailsQueueRepository vehicleTripSheetDetailsQueueRepository;

    @Autowired
    private VehicleTripSheetDetailsJdbcRepository vehicleTripSheetDetailsJdbcRepository;

    public VehicleTripSheetDetailsRequest save(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        return vehicleTripSheetDetailsQueueRepository.save(vehicleTripSheetDetailsRequest);

    }

    public VehicleTripSheetDetailsRequest update(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        return vehicleTripSheetDetailsQueueRepository.update(vehicleTripSheetDetailsRequest);

    }

    public Pagination<VehicleTripSheetDetails> search(final VehicleTripSheetDetailsSearch vehicleTripSheetDetailsSearch) {
        return vehicleTripSheetDetailsJdbcRepository.search(vehicleTripSheetDetailsSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return vehicleTripSheetDetailsJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}