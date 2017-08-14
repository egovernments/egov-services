package org.egov.pgr.domain.service.validator.Attributedefinition.validator;


import org.egov.pgr.domain.model.AttributeDefinition;

public interface AttributeDefinitionValidator {
    boolean canValidate(AttributeDefinition attributeDefinition);
    void validatingLength(AttributeDefinition attributeDefinition);

}
