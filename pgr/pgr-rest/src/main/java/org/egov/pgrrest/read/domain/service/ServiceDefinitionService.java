package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceDefinitionRepository;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class ServiceDefinitionService {

    private ServiceDefinitionRepository repository;

    public ServiceDefinitionService(ServiceDefinitionRepository repository) {
        this.repository = repository;
    }

    public ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        return repository.find(searchCriteria);
    }

    public ServiceDefinition create() {
        throw new NotImplementedException();
    }
}
