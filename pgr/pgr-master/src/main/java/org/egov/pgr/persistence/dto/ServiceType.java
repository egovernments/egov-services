package org.egov.pgr.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer department;
    private Boolean metadata;
    private String type;
    private List<String> keywords;
    private Integer category;
    private Integer slahours;
    private String tenantid;
    private Boolean isday;
    private Boolean isactive;
    private boolean hasfinancialimpact;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;

    public org.egov.pgr.domain.model.ServiceType toDomain(){
        return org.egov.pgr.domain.model.ServiceType.builder()
                .serviceCode(code)
                .type(type)
                .department(department)
                .description(description)
                .slaHours(slahours)
                .metadata(null == metadata ? false : metadata)
                .active(isactive)
                .category(category)
                .hasFinancialImpact(hasfinancialimpact)
                .isDay(isday)
                .tenantId(tenantid)
                .keywords(keywords)
                .build();
    }
}