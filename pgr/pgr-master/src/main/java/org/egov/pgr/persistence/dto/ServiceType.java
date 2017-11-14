package org.egov.pgr.persistence.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
    private Integer slaHours;
    private String tenantId;
    private Boolean isday;
    private Boolean isactive;
    private boolean hasfinancialimpact;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;
    private String localname;

    public org.egov.pgr.domain.model.ServiceType toDomain(){
        return org.egov.pgr.domain.model.ServiceType.builder()
                .id(id)
                .serviceCode(code)
                .serviceName(name)
                .type(type)
                .department(department)
                .description(description)
                .slaHours(slaHours)
                .metadata(null == metadata ? false : metadata)
                .active(isactive)
                .category(category)
                .hasFinancialImpact(hasfinancialimpact)
                .isDay(isday)
                .tenantId(tenantId)
                .keywords(keywords)
                .localName(localname)
                .build();
    }

    public boolean isKeywordPresent(List<String> keywordsList){
        return keywords.stream().anyMatch(keywordsList::contains);
    }

}