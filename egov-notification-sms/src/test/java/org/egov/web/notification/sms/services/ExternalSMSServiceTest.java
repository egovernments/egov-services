package org.egov.web.notification.sms.services;

import org.egov.web.notification.sms.config.SmsProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class ExternalSMSServiceTest {

    @Mock
    private SmsProperties smsProperties;

    private MockRestServiceServer server;

    private ExternalSMSService smsService;

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        smsService = new ExternalSMSService(smsProperties, restTemplate);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_send_sms() {
        when(smsProperties.getSmsProviderURL()).thenReturn("http://sms/sms");
        final LinkedMultiValueMap<String, String> expectedContent = getExpectedContent();
        when(smsProperties.getSmsRequestBody("mobileNumber", "testMessage", Priority.MEDIUM))
                .thenReturn(expectedContent);
        when(smsProperties.getSmsErrorCodes()).thenReturn(Arrays.asList("404", "401"));

        server.expect(once(), requestTo("http://sms/sms"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().formData(expectedContent))
                .andRespond(withSuccess("sometextmessage", MediaType.TEXT_PLAIN));

        smsService.sendSMS("mobileNumber", "testMessage", Priority.MEDIUM);

        server.verify();
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_exception_when_response_is_not_successful() {
        when(smsProperties.getSmsProviderURL()).thenReturn("http://sms/sms");
        final LinkedMultiValueMap<String, String> expectedContent = getExpectedContent();
        when(smsProperties.getSmsRequestBody("mobileNumber", "testMessage", Priority.MEDIUM))
                .thenReturn(expectedContent);
        when(smsProperties.getSmsErrorCodes()).thenReturn(Arrays.asList("400", "401"));

        server.expect(once(), requestTo("http://sms/sms"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().formData(expectedContent))
                .andRespond(withBadRequest());

        smsService.sendSMS("mobileNumber", "testMessage", Priority.MEDIUM);
    }


    private LinkedMultiValueMap<String, String> getExpectedContent() {
        final LinkedMultiValueMap<String, String> expectedContent = new LinkedMultiValueMap<>();
        expectedContent.add("key1", "value1");
        expectedContent.add("key2", "value2");
        return expectedContent;
    }

}