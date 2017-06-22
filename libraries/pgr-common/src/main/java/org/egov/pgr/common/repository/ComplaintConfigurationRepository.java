package org.egov.pgr.common.repository;

import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintConfigurationRepository {

    private static final String ANONYMOUS_COMPLAINT_OTP_CONFIG_URL = "pgr-master/OTPConfig/_search?tenantId={tenantId}";
    private final String url;
    private RestTemplate restTemplate;

    public ComplaintConfigurationRepository(RestTemplate restTemplate,
                                            @Value("${pgrmaster.host}") String pgrMasterHost) {
        this.restTemplate = restTemplate;
        this.url = pgrMasterHost + ANONYMOUS_COMPLAINT_OTP_CONFIG_URL;
    }

    public boolean isOtpEnabledForAnonymousComplaint(String tenantId) {
        final RequestInfoWrapper request = buildRequestInfo();
        final OtpConfigResponse response =
                restTemplate.postForObject(url, request, OtpConfigResponse.class, tenantId);

        return response.isOtpEnabledForAnonymousComplaint();
    }

    private RequestInfoWrapper buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return RequestInfoWrapper.builder()
            .RequestInfo(requestInfo)
            .build();
    }

}