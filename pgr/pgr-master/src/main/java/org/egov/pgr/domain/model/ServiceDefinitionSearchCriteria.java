package org.egov.pgr.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceDefinitionSearchCriteria {

    private String serviceCode;

    private String tenantId;
}
