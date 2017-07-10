package org.egov.pgrrest.write.domain.service;

import org.egov.pgrrest.write.consumer.contracts.request.SevaRequest;
import org.egov.pgrrest.read.persistence.repository.IntegrationMessageQueueRepository;
import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.egov.pgrrest.write.persistence.repository.ServiceRequestWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestWriteService {

    private ServiceRequestWriteRepository serviceRequestWriteRepository;

    private IntegrationMessageQueueRepository integrationMessageQueueRepository;

    @Autowired
    public ServiceRequestWriteService(ServiceRequestWriteRepository serviceRequestWriteRepository,
                                      IntegrationMessageQueueRepository integrationMessageQueueRepository) {
        this.serviceRequestWriteRepository = serviceRequestWriteRepository;
        this.integrationMessageQueueRepository = integrationMessageQueueRepository;
    }

    public void updateOrInsert(ServiceRequestRecord serviceRequestRecord) {
        serviceRequestWriteRepository.updateOrInsert(serviceRequestRecord);
    }

    public void pushToThirdPartySystem(SevaRequest sevaRequest){
        integrationMessageQueueRepository.save(sevaRequest);
    }
}