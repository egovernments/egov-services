package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.sql.Date;

@Builder
@Getter
public class ServiceDefinitionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private ServiceDefinition serviceDefinition;

    public org.egov.pgr.domain.model.ServiceDefinition toDomain(){
        return org.egov.pgr.domain.model.ServiceDefinition.builder()
                    .tenantId(serviceDefinition.getTenantId())
                    .code(serviceDefinition.getCode())
                    .createdBy(requestInfo.getUserInfo().getId())
                    .createdDate(new Date(new java.util.Date().getTime()))
                    .attributes(serviceDefinition.mapToModelAttributes())
                    .build();
    }
}