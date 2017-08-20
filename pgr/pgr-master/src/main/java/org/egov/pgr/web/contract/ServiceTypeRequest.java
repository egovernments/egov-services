package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.sql.Date;

@Builder
@Getter
public class ServiceTypeRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private ServiceType serviceType;

    public org.egov.pgr.domain.model.ServiceType toDomain(){
        return org.egov.pgr.domain.model.ServiceType.builder()
                .serviceCode(serviceType.getServiceCode() != null ? serviceType.getServiceCode().trim() : null)
                .id(serviceType.getId())
                .serviceName(serviceType.getServiceName() != null ? serviceType.getServiceName().trim() : null)
                .description(serviceType.getDescription())
                .department(serviceType.getDepartment())
                .metadata(serviceType.isMetadata())
                .type(serviceType.getType())
                .keywords(serviceType.getKeywords())
                .category(serviceType.getCategory())
                .config(serviceType.getConfig())
                .slaHours(serviceType.getSlaHours())
                .tenantId(serviceType.getTenantId())
                .isDay(serviceType.getDays())
                .active(serviceType.getActive())
                .hasFinancialImpact(serviceType.isHasFinancialImpact())
                .createdDate(new Date(new java.util.Date().getTime()))
                .createdBy(requestInfo.getUserInfo().getId())
                .lastModifiedBy(requestInfo.getUserInfo().getId())
                .lastModifiedDate(new Date(new java.util.Date().getTime()))
                .build();
    }
}