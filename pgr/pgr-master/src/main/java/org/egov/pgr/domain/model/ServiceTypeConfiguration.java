package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static org.springframework.util.StringUtils.isEmpty;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeConfiguration {

    private String tenantId;

    private String serviceCode;

    private boolean isApplicationFeesEnabled;

    private boolean isNotificationEnabled;

    private boolean isSlaEnabled;

    public boolean isTenantIdAbsent(){
        return isEmpty(tenantId);
    }

    public boolean isServiceCodeAbsent(){
        return isEmpty(serviceCode);
    }
}
