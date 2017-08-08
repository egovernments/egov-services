package org.egov.pgr.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
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
    
    
    public ServiceTypeConfiguration(org.egov.pgr.domain.model.ServiceTypeConfiguration serviceTypeConfiguration) {
        this.serviceCode = serviceTypeConfiguration.getServiceCode();
        this.tenantId = serviceTypeConfiguration.getTenantId();
        this.glCode = serviceTypeConfiguration.getGlCode();
        this.online = serviceTypeConfiguration.isOnline();
        this.source = serviceTypeConfiguration.getSource();
        this.url = serviceTypeConfiguration.getUrl();
        this.notificationEnabled = serviceTypeConfiguration.isNotificationEnabled();
        this.applicationFeesEnabled = serviceTypeConfiguration.isApplicationFeesEnabled();
        this.slaEnabled=serviceTypeConfiguration.isSlaEnabled();
    }
}