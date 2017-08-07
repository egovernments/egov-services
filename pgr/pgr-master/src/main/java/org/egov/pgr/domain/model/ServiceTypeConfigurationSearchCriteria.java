package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceTypeConfigurationSearchCriteria {
    private String serviceCode;
    private String tenantId;
}
