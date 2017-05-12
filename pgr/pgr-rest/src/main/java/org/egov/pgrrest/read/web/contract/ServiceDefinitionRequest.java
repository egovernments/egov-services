package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ServiceDefinitionRequest {
    private RequestInfo requestInfo;
    private String tenantId;
    private String serviceCode;
    private List<AttributeDefinition> attributes;
}

