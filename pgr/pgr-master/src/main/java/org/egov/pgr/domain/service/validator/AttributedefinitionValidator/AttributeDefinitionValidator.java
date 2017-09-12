package org.egov.pgr.domain.service.validator.AttributedefinitionValidator;


import org.egov.pgr.domain.model.AttributeDefinition;

public interface AttributeDefinitionValidator {
    boolean canValidate(AttributeDefinition attributeDefinition);
    void validatingLength(AttributeDefinition attributeDefinition);
    void checkMandatoryField(AttributeDefinition attributeDefinition);
    void validateDataType(AttributeDefinition attributeDefinition);


}
