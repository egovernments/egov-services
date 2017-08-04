package org.egov.pgr.persistence.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceTypeConfiguration {

    private String tenantId;

    private String serviceCode;

    private boolean isApplicationFeesEnabled;

    private boolean isNotificationEnabled;

    private boolean isSlaEnabled;
}
