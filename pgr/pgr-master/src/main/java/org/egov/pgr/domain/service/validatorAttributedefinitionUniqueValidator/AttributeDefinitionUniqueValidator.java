package org.egov.pgr.domain.service.validatorAttributedefinitionUniqueValidator;


import org.egov.pgr.domain.model.AttributeDefinition;

public interface AttributeDefinitionUniqueValidator {
    boolean canValidate(AttributeDefinition attributeDefinition);
    void validateUniqueConstratint(AttributeDefinition attributeDefinition);

}
