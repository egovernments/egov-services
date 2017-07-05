package org.egov.pgrrest.common.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AttributeDefinitionKey implements Serializable {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "servicecode", nullable = false)
    private String serviceCode;

    @Column(name = "tenantid", nullable = false)
    private String tenantId;
}

