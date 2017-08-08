package org.egov.pgr.persistence.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ServiceTypeConfiguration {

	@NotNull
    private String tenantId;

    @NotNull
    private String serviceCode;

    private boolean applicationFeesEnabled;

    private boolean notificationEnabled;

    private boolean slaEnabled;
    
    private String glCode;
    
    private boolean online;
    
    private String source;
    
    private String url;
    
    public static final String
    
    TENANTID = "tenantId",
    SERVICECODE = "serviceCode",
    GLCODE = "glCode",
    ONLINE = "online",
    SOURCE = "source",
    URL = "url",
    APPLICATIONFEESENABLED = "applicationFeesEnabled",
    NOTIFICATIONENABLED = "notificationEnabled",
    SLAENABLED = "slaEnabled";
  
    public org.egov.pgr.domain.model.ServiceTypeConfiguration toDomain(){
        return org.egov.pgr.domain.model.ServiceTypeConfiguration.builder()
                .serviceCode(serviceCode)
                .tenantId(tenantId)
                .applicationFeesEnabled(applicationFeesEnabled)
                .notificationEnabled(notificationEnabled)
                .slaEnabled(slaEnabled)
                .glCode(glCode)
                .online(online)
                .source(source)
                .url(url)
                .build();
    }
}


