package org.egov.pgr.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<AttributeDefinition> attributeDefinitions;

    public org.egov.pgr.domain.model.ServiceType toDomain(){
        return org.egov.pgr.domain.model.ServiceType.builder()
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
                .attributes(mapToDomainAttributes())
                .build();
    }

    public boolean isKeywordPresent(List<String> keywordsList){
        return keywords.stream().anyMatch(keywordsList::contains);
    }

    private List<org.egov.pgr.domain.model.AttributeDefinition> mapToDomainAttributes(){
        return attributeDefinitions.stream()
                .map(a -> a.toDomain())
                .collect(Collectors.toList());
    }
}