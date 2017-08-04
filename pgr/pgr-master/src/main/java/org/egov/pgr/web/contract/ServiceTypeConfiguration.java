package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ServiceTypeConfiguration {

    private String tenantId;

    private String serviceCode;

    private boolean applicationFeesEnabled;

    private boolean notificationEnabled;

    private boolean slaEnabled;

    public org.egov.pgr.domain.model.ServiceTypeConfiguration toDomain(){
        return org.egov.pgr.domain.model.ServiceTypeConfiguration.builder()
                .serviceCode(serviceCode)
                .tenantId(tenantId)
                .isApplicationFeesEnabled(applicationFeesEnabled)
                .isNotificationEnabled(notificationEnabled)
                .isSlaEnabled(slaEnabled)
                .build();
    }
}
