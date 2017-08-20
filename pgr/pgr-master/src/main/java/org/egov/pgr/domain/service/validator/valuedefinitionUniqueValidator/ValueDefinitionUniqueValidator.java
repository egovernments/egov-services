package org.egov.pgr.domain.service.validator.valuedefinitionUniqueValidator;


import org.egov.pgr.domain.model.ValueDefinition;

public interface ValueDefinitionUniqueValidator {
    boolean canValidate(ValueDefinition valueDefinition);
    void validateUniqueConstratint(ValueDefinition valueDefinition,String serviceCode,String attributeCode,String tenantId);

}
