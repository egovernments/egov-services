package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Setter
public class ServiceTypeConfigurationSearchCriteria {
    private String serviceCode;
    private String tenantId;
}
