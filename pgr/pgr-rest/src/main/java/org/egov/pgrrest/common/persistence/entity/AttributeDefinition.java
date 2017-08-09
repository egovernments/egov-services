package org.egov.pgrrest.common.persistence.entity;

import lombok.*;
import org.egov.pgrrest.common.domain.model.AttributeDataType;
import org.egov.pgrrest.common.domain.model.AttributeActionsDefinition;
import org.egov.pgrrest.common.domain.model.AttributeRolesDefinition;
import org.egov.pgrrest.common.domain.model.ComputeRuleDefinition;
import org.egov.pgrrest.common.domain.model.ValueDefinition;
import org.egov.pgrrest.read.domain.exception.InvalidAttributeDataTypeException;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attribute_definition")
public class AttributeDefinition extends AbstractPersistable<AttributeDefinitionKey> {

    private static final char YES = 'Y';
    private static final char NO = 'N';

    @EmbeddedId
    private AttributeDefinitionKey id;

    @Column(name = "variable")
    private char variable;

    @Column(name = "datatype")
    private String dataType;

    @Column(name = "required")
    private char required;

    @Column(name = "datatypedescription")
    private String dataTypeDescription;

    @Column(name = "ordernum")
    private int order;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "groupcode")
    private String groupCode;

    public String getCode() {
        return id.getCode();
    }

    public org.egov.pgrrest.common.domain.model.AttributeDefinition toDomain(List<ValueDefinition> domainValues,
                                                                             List<AttributeRolesDefinition> roles,
                                                                             List<AttributeActionsDefinition> actions,
                                                                             List<ComputeRuleDefinition> computeRules) {
        return org.egov.pgrrest.common.domain.model.AttributeDefinition.builder()
            .code(getCode())
            .dataType(getEnumDataType())
            .readOnly(isReadOnly())
            .required(isRequired())
            .order(order)
            .dataTypeDescription(dataTypeDescription)
            .description(description)
            .url(url)
            .roles(roles)
            .actions(actions)
            .values(domainValues)
            .computeRules(computeRules)
            .build();
    }

    private AttributeDataType getEnumDataType() {
        return Stream.of(AttributeDataType.values())
            .filter(dataType -> dataType.getName().equalsIgnoreCase(this.dataType))
            .findFirst()
            .orElseThrow(() -> new InvalidAttributeDataTypeException(this.dataType, this.id.getCode()));
    }

    private boolean isRequired() {
        return required == YES;
    }

    private boolean isReadOnly() {
        return variable == NO;
    }
}

