package org.egov.pgr.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class ServiceType {

    private Long id;
    private String serviceName;
    private String serviceCode;
    private String description;
    private Integer department;
    private boolean metadata;
    private String type;
    private List<String> keywords;
    private Integer category;
    private List<String> config;
    private Integer slaHours;
    private String tenantId;
    private Boolean isDay;
    private Boolean active;
    private boolean hasFinancialImpact;
    @NotNull
    private Long createdBy;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;
    private Long lastModifiedBy;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date lastModifiedDate;


    public org.egov.pgr.persistence.dto.ServiceType toDto(){
        return org.egov.pgr.persistence.dto.ServiceType.builder()
                .id(id)
                .name(serviceName)
                .code(serviceCode)
                .description(description)
                .department(department)
                .metadata(metadata)
                .type(type)
                .keywords(keywords)
                .category(category)
                .slahours(slaHours)
                .tenantid(tenantId)
                .isday(isDay)
                .isactive(active)
                .hasfinancialimpact(hasFinancialImpact)
                .createdDate(createdDate)
                .createdBy(createdBy)
                .lastModifiedDate(lastModifiedDate)
                .lastModifiedBy(lastModifiedBy)
                .build();
    }
}
