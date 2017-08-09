package org.egov.pgr.persistence.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDefinition {

    private static final char YES = 'Y';
    private static final char NO = 'N';

    private char variable;
    private String dataType;
    private char required;
    private String dataTypeDescription;
    private int order;
    private String description;
    private String code;
    private String url;
    private String groupCode;

    public org.egov.pgr.domain.model.AttributeDefinition toDomain() {
        return org.egov.pgr.domain.model.AttributeDefinition.builder()
                .code(code)
                .dataType(dataType)
                .required(isRequired())
                .dataTypeDescription(dataTypeDescription)
                .order(order)
                .description(description)
                .url(url)
                .groupCode(groupCode)
                .readOnly(isReadOnly())
                .build();
    }

    private boolean isRequired() {
        return required == YES;
    }

    private boolean isReadOnly() {
        return variable == NO;
    }

}

