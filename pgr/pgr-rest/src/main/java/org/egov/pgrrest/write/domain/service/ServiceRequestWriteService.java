package org.egov.pgrrest.write.domain.service;

import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.egov.pgrrest.write.persistence.repository.ServiceRequestWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestWriteService {

    private ServiceRequestWriteRepository serviceRequestWriteRepository;

    @Autowired
    public ServiceRequestWriteService(ServiceRequestWriteRepository serviceRequestWriteRepository) {
        this.serviceRequestWriteRepository = serviceRequestWriteRepository;
    }

    public void updateOrInsert(ServiceRequestRecord serviceRequestRecord) {
        serviceRequestWriteRepository.updateOrInsert(serviceRequestRecord);
    }
}