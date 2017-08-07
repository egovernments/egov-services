package org.egov.pgr.web.contract;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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