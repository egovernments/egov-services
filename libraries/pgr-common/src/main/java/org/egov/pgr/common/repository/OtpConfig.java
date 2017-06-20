package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OtpConfig {
    @JsonProperty("otpEnabledForAnonymousComplaint")
    private boolean otpEnabledForAnonymousComplaint;
}
