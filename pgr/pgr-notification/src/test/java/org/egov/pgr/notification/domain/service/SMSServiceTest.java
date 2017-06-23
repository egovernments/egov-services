package org.egov.pgr.notification.domain.service;

import org.egov.pgr.notification.domain.model.*;
import org.egov.pgr.notification.domain.service.smsstrategy.SMSMessageStrategy;
import org.egov.pgr.notification.persistence.queue.MessageQueueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trimou.util.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
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
    private SMSMessageStrategy smsMessageStrategy1;

    @Mock
    private SMSMessageStrategy smsMessageStrategy2;

    private SMSService smsService;

    @Before
    public void before() {
        final List<SMSMessageStrategy> messageStrategyList = Arrays.asList(smsMessageStrategy1, smsMessageStrategy2);
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
        when(smsMessageStrategy1.matches(any())).thenReturn(true);
        when(smsMessageStrategy2.matches(any())).thenReturn(true);
        final SMSMessageContext messageContext1 =
            new SMSMessageContext("sms_en1", requestMap, "mobileNumber");
        final SMSMessageContext messageContext2 =
            new SMSMessageContext("sms_en2", requestMap, "mobileNumber");
        final Tenant tenant = new Tenant("name", "grade");
        when(smsMessageStrategy1.getMessageContext(any())).thenReturn(messageContext1);
        when(smsMessageStrategy2.getMessageContext(any())).thenReturn(messageContext2);
        when(templateService.loadByName("sms_en1", requestMap)).thenReturn("smsMessage1");
        when(templateService.loadByName("sms_en2", requestMap)).thenReturn("smsMessage2");
        final NotificationContext context = NotificationContext.builder()
            .sevaRequest(sevaRequest)
            .serviceType(serviceType)
            .tenant(tenant)
            .build();

        smsService.send(context);

        verify(messageQueueRepository).sendSMS(new SMSRequest("smsMessage1", "mobileNumber"));
        verify(messageQueueRepository).sendSMS(new SMSRequest("smsMessage2", "mobileNumber"));
    }

    @Test(expected = NotImplementedException.class)
    public void test_should_throw_exception_when_matching_strategy_not_found() {
        when(sevaRequest.getServiceTypeName()).thenReturn("name");
        when(sevaRequest.getCrn()).thenReturn("crn");
        when(sevaRequest.getStatusName()).thenReturn("Registerd");
        when(sevaRequest.getMobileNumber()).thenReturn("mobileNumber");
        final List<String> keywords = Collections.singletonList("COMPLAINT");
        final ServiceType serviceType = new ServiceType("serviceName", keywords);
        when(smsMessageStrategy1.matches(any())).thenReturn(false);
        when(smsMessageStrategy2.matches(any())).thenReturn(false);
        final Tenant tenant = new Tenant("name", "grade");
        final NotificationContext context = NotificationContext.builder()
            .sevaRequest(sevaRequest)
            .serviceType(serviceType)
            .tenant(tenant)
            .build();

        smsService.send(context);

        verify(messageQueueRepository).sendSMS(new SMSRequest("smsMessage", "mobileNumber"));
    }

}