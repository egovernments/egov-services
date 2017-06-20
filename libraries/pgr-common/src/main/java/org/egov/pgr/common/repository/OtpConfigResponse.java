package org.egov.pgr.common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@AllArgsConstructor
class OtpConfigResponse {
    private List<OtpConfig> otgConfigs;

    public boolean isOtpEnabledForAnonymousComplaint() {
        if (CollectionUtils.isEmpty(otgConfigs)) {
            return false;
        }
        return otgConfigs.get(0).isOtpEnabledForAnonymousComplaint();
    }
}
