package org.egov.domain.service;

import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SMSServiceTest {

    @Mock
    private TemplateService templateService;

    @Mock
    private MessageQueueRepository messageQueueRepository;

    @Mock
    private SevaRequest sevaRequest;

    @InjectMocks
    private SMSService smsService;

    @Test
    public void test_should_send_sms_with_generated_message_from_template() {
        when(sevaRequest.getComplaintTypeName()).thenReturn("name");
        when(sevaRequest.getCrn()).thenReturn("crn");
        when(sevaRequest.getMobileNumber()).thenReturn("mobileNumber");
        final Map<Object, Object> requestMap = ImmutableMap.of(
            "name", "name",
            "number", "crn"
        );
        when(templateService.loadByName("sms_en", requestMap)).thenReturn("smsMessage");

        smsService.send(sevaRequest);

        verify(messageQueueRepository).sendSMS("mobileNumber", "smsMessage");
    }

}