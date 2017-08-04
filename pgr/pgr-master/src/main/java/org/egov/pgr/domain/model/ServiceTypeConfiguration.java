package org.egov.pgr.domain.model;

import lombok.*;

import static org.springframework.util.StringUtils.isEmpty;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
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

    public org.egov.pgr.persistence.dto.ServiceTypeConfiguration toDto(){
        return org.egov.pgr.persistence.dto.ServiceTypeConfiguration.builder()
                .serviceCode(serviceCode)
                .tenantId(tenantId)
                .isApplicationFeesEnabled(isApplicationFeesEnabled)
                .isNotificationEnabled(isNotificationEnabled)
                .isSlaEnabled(isSlaEnabled)
                .build();
    }
}