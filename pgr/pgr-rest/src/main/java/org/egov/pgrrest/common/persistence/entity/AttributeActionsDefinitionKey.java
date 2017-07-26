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
public class AttributeActionsDefinitionKey implements Serializable {

    private static final long serialVersionUID = 5033468783850139270L;
    @Column(name = "attributecode", nullable = false)
    private String attributeCode;

    @Column(name = "tenantid", nullable = false)
    private String tenantId;

    @Column(name = "servicecode")
    private String serviceCode;

    @Column(name = "name")
    private String name;
}

