package org.egov.pgr.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Builder
@Getter
@Setter
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
    private String action;
    private String localName;
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
                .name(serviceName.trim())
                .code(serviceCode.trim())
                .description(description)
                .department(department)
                .metadata(metadata)
                .type(type)
                .keywords(keywords)
                .category(category)
                .slaHours(slaHours)
                .tenantId(tenantId.trim())
                .isday(isDay)
                .isactive(active)
                .hasfinancialimpact(hasFinancialImpact)
                .localname(localName)
                .createdDate(createdDate)
                .createdBy(createdBy)
                .lastModifiedDate(lastModifiedDate)
                .lastModifiedBy(lastModifiedBy)
                .build();
    }
   
    	/*Length validation*/
    public boolean isTenantIdLengthMatch(){
    	
        return (!tenantId.isEmpty() && (tenantId.length() > 0 && tenantId.length() <= 256));
    }
    
    public boolean isCodeLengthMatch(){
    	if(serviceCode != null){
        return (serviceCode.length() > 0 && serviceCode.length() <= 20);
    	}
    	return true;
    }
    
    public boolean isDescriptionLengthMatch(){
    	if(description != null){
        return (description.length() > 0 && description.length() <= 250);
    	}
    	return true;
    }
    
    public boolean isTypeLengthMatch(){
    	if(type != null){

        return (type.length() >0 &&  type.length() <= 50);
    	}
    	return true;
    }
    
    public boolean isnameLengthMatch(){
    	if(serviceName != null){
        return (serviceName.length() > 0 && serviceName.length() <= 150);
    	}
    	return true;
    }

    public boolean isTenantIdAbsent(){
        return isEmpty(tenantId) || tenantId == null;
    }
    
    public boolean isServiceCodeAbsent(){
        return isEmpty(serviceCode) || serviceCode == null;
    }
    
    public boolean isServiceNameAbsent(){
        return isEmpty(serviceName) || serviceName == null;
    }
    
    public boolean isCategoryAbsent(){
        return isEmpty(category) || category == null;
    }
    
     public boolean isKeywordAbsent()
     {
    	 return (!keywords.isEmpty() && keywords.get(0).isEmpty() || keywords.size() == 0) ;
     }
     
     public boolean isKeywordValid()
     {
    	 List<String> keywordsExpected = Arrays.asList("Deliverable_Service","Complaint");

    	   return (!keywords.isEmpty() && (!keywordsExpected.stream().anyMatch(keywords::contains)));
     }
     

    public boolean isUpdate(){
        return "UPDATE".equalsIgnoreCase(action);
    }

}
