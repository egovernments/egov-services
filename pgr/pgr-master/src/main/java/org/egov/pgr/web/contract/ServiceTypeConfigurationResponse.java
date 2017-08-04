package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceTypeConfigurationResponse {

    ResponseInfo responseInfo;

    private ServiceTypeConfiguration serviceTypeConfiguration;
}