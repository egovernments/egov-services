package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceTypeConfigurationRequest {

    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;

    private ServiceTypeConfiguration serviceTypeConfiguration;
}
