package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ServiceDefinition {
    private String code;
    private String tenantId;
    private List<AttributeDefinition> attributes;
}

