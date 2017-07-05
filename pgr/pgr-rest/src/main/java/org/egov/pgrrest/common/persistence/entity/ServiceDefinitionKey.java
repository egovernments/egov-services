package org.egov.pgrrest.common.persistence.entity;

import lombok.*;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ServiceDefinitionKey implements Serializable {

    private String code;
    @Column(name = "tenantid", nullable = false)
    private String tenantId;

    public ServiceDefinitionKey(ServiceDefinitionSearchCriteria searchCriteria) {
        this.code = searchCriteria.getServiceCode();
        this.tenantId = searchCriteria.getTenantId();
    }
}

