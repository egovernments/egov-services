package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceDefinitionSearchCriteria {
    private String serviceCode;
    private String tenantId;
}
