package org.egov.pgr.domain.model;

import lombok.*;

import static org.springframework.util.StringUtils.isEmpty;

import javax.validation.constraints.NotNull;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
    

    public boolean isTenantIdAbsent(){
        return isEmpty(tenantId);
    }

    public boolean isServiceCodeAbsent(){
        return isEmpty(serviceCode);
    }

    public boolean isGlCodeEnabled(){
        return isEmpty(glCode);
    }
    
    public boolean isOnlineEnabled(){
        return isEmpty(online);
    }
    
    public boolean isSourceEnabled(){
        return isEmpty(source);
    }
    
    public boolean isurl(){
        return isEmpty(url);
    }
    

    public org.egov.pgr.persistence.dto.ServiceTypeConfiguration toDto(){
        return org.egov.pgr.persistence.dto.ServiceTypeConfiguration.builder()
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