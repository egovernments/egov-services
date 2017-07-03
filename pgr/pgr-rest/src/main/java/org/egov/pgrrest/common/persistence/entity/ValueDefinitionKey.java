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
public class ValueDefinitionKey implements Serializable {

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "attributecode", nullable = false)
    private String attributeCode;

    @Column(name = "tenantid", nullable = false)
    private String tenantId;
}
