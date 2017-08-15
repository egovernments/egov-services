package org.egov.pgr.domain.service.validator.valueDeficnitionValidator;


import org.egov.pgr.domain.model.ValueDefinition;

public interface VAlueDefinitionValidator {
    boolean canValidate(ValueDefinition valueDefinition);
    void checkMandatoryField(ValueDefinition valueDefinition);
    void validateLength(ValueDefinition valueDefinition);

}
