package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.persistence.queue.repository.VehicleFuellingDetailsQueueRepository;
import org.egov.swm.persistence.repository.VehicleFuellingDetailsJdbcRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsRepository {

    @Autowired
    private VehicleFuellingDetailsJdbcRepository vehicleFuellingDetailsJdbcRepository;

    @Autowired
    private VehicleFuellingDetailsQueueRepository vehicleFuellingDetailsQueueRepository;

    public VehicleFuellingDetailsRequest save(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        return vehicleFuellingDetailsQueueRepository.save(vehicleFuellingDetailsRequest);

    }

    public VehicleFuellingDetailsRequest update(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        return vehicleFuellingDetailsQueueRepository.update(vehicleFuellingDetailsRequest);

    }

    public Pagination<VehicleFuellingDetails> search(final VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {
        return vehicleFuellingDetailsJdbcRepository.search(vehicleFuellingDetailsSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return vehicleFuellingDetailsJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}