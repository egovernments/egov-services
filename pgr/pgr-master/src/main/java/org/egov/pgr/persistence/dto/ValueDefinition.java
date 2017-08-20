package org.egov.pgr.persistence.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ValueDefinition {

    private static final char YES = 'Y';

    private String key;
    private String attributeCode;
    private String tenantId;
    private String serviceCode;
    private String name;
    private char active;
    private char required;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;

    private boolean isActive() {
        return YES == active;
    }

    private boolean isRequired() {
        return YES == required;
    }

    public org.egov.pgr.domain.model.ValueDefinition toDomain(){


        return new org.egov.pgr.domain.model.ValueDefinition(name, key, isActive(),isRequired(),serviceCode,tenantId,attributeCode);
    }

}
