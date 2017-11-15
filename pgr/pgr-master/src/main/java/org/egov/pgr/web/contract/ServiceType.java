package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceType {

    private Long id;
    private String serviceName;
    private String serviceCode;
    private String description;
    private boolean metadata;
    private Integer department;
    private String type;
    private List<String> keywords;
    private Integer category;
    private List<String> config;
    private Integer slaHours;
    private String tenantId;
    private Boolean days;
    private Boolean active;
    private boolean hasFinancialImpact;
    private String localName;

    public ServiceType(org.egov.pgr.domain.model.ServiceType serviceType) {
        this.id = serviceType.getId();
        this.serviceName = serviceType.getServiceName();
        this.active = serviceType.getActive();
        this.category = serviceType.getCategory();
        this.days = serviceType.getIsDay();
        this.department = serviceType.getDepartment();
        this.description = serviceType.getDescription();
        this.hasFinancialImpact = serviceType.isHasFinancialImpact();
        this.metadata = serviceType.isMetadata();
        this.serviceCode = serviceType.getServiceCode();
        this.tenantId = serviceType.getTenantId();
        this.keywords = serviceType.getKeywords();
        this.type = serviceType.getType();
        this.slaHours = serviceType.getSlaHours();
        this.localName = serviceType.getLocalName();
    }

}