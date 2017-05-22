package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trimou.util.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
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

    @Mock
    private SMSMessageStrategy smsMessageStrategy;

    private SMSService smsService;

    @Before
    public void before() {
        final List<SMSMessageStrategy> messageStrategyList = Collections.singletonList(smsMessageStrategy);
        smsService = new SMSService(templateService, messageQueueRepository, messageStrategyList);
    }

    @Test
    public void test_should_send_sms_with_generated_message_from_template() {
        when(sevaRequest.getServiceTypeName()).thenReturn("name");
        when(sevaRequest.getCrn()).thenReturn("crn");
        when(sevaRequest.getStatusName()).thenReturn("Registerd");
        when(sevaRequest.getMobileNumber()).thenReturn("mobileNumber");
        final Map<Object, Object> requestMap = ImmutableMap.of(
            "name", "name",
            "number", "crn",
            "statusLowerCase","registerd"
        );
        final List<String> keywords = Collections.singletonList("COMPLAINT");
        final ServiceType serviceType = new ServiceType("serviceName", keywords);
        when(smsMessageStrategy.matches(sevaRequest, serviceType)).thenReturn(true);
        final SMSMessageContext messageContext = new SMSMessageContext("sms_en", requestMap);
        when(smsMessageStrategy.getMessageContext(sevaRequest, serviceType)).thenReturn(messageContext);
        when(templateService.loadByName("sms_en", requestMap)).thenReturn("smsMessage");

        smsService.send(sevaRequest, serviceType);

        verify(messageQueueRepository).sendSMS("mobileNumber", "smsMessage");
    }

    @Test(expected = NotImplementedException.class)
    public void test_should_throw_exception_when_matching_strategy_not_found() {
        when(sevaRequest.getServiceTypeName()).thenReturn("name");
        when(sevaRequest.getCrn()).thenReturn("crn");
        when(sevaRequest.getStatusName()).thenReturn("Registerd");
        when(sevaRequest.getMobileNumber()).thenReturn("mobileNumber");
        final List<String> keywords = Collections.singletonList("COMPLAINT");
        final ServiceType serviceType = new ServiceType("serviceName", keywords);
        when(smsMessageStrategy.matches(sevaRequest, serviceType)).thenReturn(false);

        smsService.send(sevaRequest, serviceType);

        verify(messageQueueRepository).sendSMS("mobileNumber", "smsMessage");
    }

}