package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeCodeException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/*
    Responsible for validating and computing custom fields.
 */
@Service
public class ServiceRequestCustomFieldService {

    private ServiceDefinitionService serviceDefinitionService;

    public ServiceRequestCustomFieldService(ServiceDefinitionService serviceDefinitionService) {
        this.serviceDefinitionService = serviceDefinitionService;
    }

    public void process(ServiceRequest serviceRequest) {
        final ServiceDefinition serviceDefinition = getServiceDefinition(serviceRequest);
        validateKnownServiceDefinition(serviceRequest, serviceDefinition);
        if(serviceDefinition.isAttributesAbsent()) {
            return;
        }
        final List<AttributeDefinition> nonComputedAttributes = serviceDefinition.getNonComputedAttributes();

    }

    private void validateKnownServiceDefinition(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition) {
        if(serviceDefinition == null) {
            throw new InvalidServiceTypeCodeException(serviceRequest.getServiceTypeCode());
        }
    }

    private ServiceDefinition getServiceDefinition(ServiceRequest serviceRequest) {
        final ServiceDefinitionSearchCriteria serviceDefinitionSearchCriteria = serviceRequest
            .getServiceDefinitionSearchCriteria();
        return serviceDefinitionService.find(serviceDefinitionSearchCriteria);
    }
}
