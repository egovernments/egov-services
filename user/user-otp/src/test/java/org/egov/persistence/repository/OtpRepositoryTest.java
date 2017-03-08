package org.egov.persistence.repository;

import org.egov.Resources;
import org.egov.domain.model.OtpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class OtpRepositoryTest {

    private static final String HOST = "http://host";
    private static final String CREATE_OTP_URL = "/otp/_create";
    private Resources resources = new Resources();
    private MockRestServiceServer server;
    private OtpRepository otpRepository;

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        otpRepository = new OtpRepository(restTemplate, HOST, CREATE_OTP_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_return_otp_for_given_request() {
        server.expect(once(), requestTo("http://host/otp/_create"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(resources.getFileContents("otpTokenRequest.json")))
                .andRespond(
                        withSuccess(resources.getFileContents("otpTokenResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));
        final OtpRequest domainOtpRequest = OtpRequest.builder()
                .tenantId("tenantId")
                .mobileNumber("mobileNumber")
                .build();

        final String otp = otpRepository.fetchOtp(domainOtpRequest);

        server.verify();
        assertEquals("otpNumber", otp);
    }



}