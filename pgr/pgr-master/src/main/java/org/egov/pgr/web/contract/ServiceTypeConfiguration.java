package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTypeConfiguration {

    private String tenantId;

    private String serviceCode;

    private boolean isApplicationFeesEnabled;

    private boolean isNotificationEnabled;

    private boolean isSlaEnabled;
}
