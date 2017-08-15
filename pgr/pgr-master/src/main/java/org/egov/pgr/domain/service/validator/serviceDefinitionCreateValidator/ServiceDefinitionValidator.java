package org.egov.pgr.domain.service.validator.serviceDefinitionCreateValidator;


import org.egov.pgr.domain.model.ServiceDefinition;

public interface ServiceDefinitionValidator {
    boolean canValidate(ServiceDefinition serviceDefinition);
    void checkMandatoryField(ServiceDefinition serviceDefinition);
    void checkConstraints(ServiceDefinition serviceDefinition);
    void checkLength(ServiceDefinition serviceDefinition);
    void matchServiceandAttributeCodes(ServiceDefinition serviceDefinition);
}
