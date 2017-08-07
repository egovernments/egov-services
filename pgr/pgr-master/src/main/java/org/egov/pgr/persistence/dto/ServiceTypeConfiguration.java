package org.egov.pgr.persistence.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

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
}