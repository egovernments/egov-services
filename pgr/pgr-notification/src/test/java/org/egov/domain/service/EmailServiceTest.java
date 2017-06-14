package org.egov.domain.service;

import org.egov.domain.model.*;
import org.egov.persistence.queue.MessageQueueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private TemplateService templateService;

    @Mock
    private MessageQueueRepository messageQueueRepository;

    @Mock
    private SevaRequest sevaRequest;

    @Mock
    private EmailMessageStrategy emailMessageStrategy;

    private EmailService emailService;

    @Before
    public void before() {
        final List<EmailMessageStrategy> emailMessageStrategyList = Collections.singletonList(emailMessageStrategy);
        emailService = new EmailService(templateService, messageQueueRepository, emailMessageStrategyList);
    }

    @Test
    public void test_should_send_email_message_to_topic() {
        when(sevaRequest.isRequesterEmailAbsent()).thenReturn(false);
        final String emailAddress = "email@email.com";
        final String crn = "crn";
        when(sevaRequest.getRequesterEmail()).thenReturn(emailAddress);
        when(sevaRequest.getCrn()).thenReturn(crn);
        final String body = "emailBody";
        final String subject = "emailSubject";
        final String status = "Status";
        final String complainantName = "name";
        when(sevaRequest.getRequesterName()).thenReturn(complainantName);
        final String complaintTypeName = "complaintTypeName";
        when(sevaRequest.getServiceTypeName()).thenReturn(complaintTypeName);
        final String locationName = "locationName";
        when(sevaRequest.getLocationName()).thenReturn(locationName);
        final String details = "details";
        when(sevaRequest.getDetails()).thenReturn(details);
        final String formattedDate = "formattedDate";
        when(sevaRequest.getFormattedCreatedDate()).thenReturn(formattedDate);
        when(sevaRequest.getStatusName()).thenReturn(status);
        final Tenant tenant = new Tenant("tenantName", "ulbGrade");
        final List<String> keywords = Collections.emptyList();
        final ServiceType serviceType = new ServiceType("serviceType", keywords);
        when(emailMessageStrategy.matches(any())).thenReturn(true);
        final HashMap<Object, Object> bodyTemplateValues = new HashMap<>();
        final HashMap<Object, Object> subjectTemplateValues = new HashMap<>();
        final EmailMessageContext emailMessageContext = EmailMessageContext.builder()
            .email(emailAddress)
            .bodyTemplateName("bodyTemplateName")
            .subjectTemplateName("subjectTemplateName")
            .bodyTemplateValues(bodyTemplateValues)
            .subjectTemplateValues(subjectTemplateValues)
            .build();
        when(emailMessageStrategy.getMessageContext(any())).thenReturn(emailMessageContext);
        when(templateService.loadByName("bodyTemplateName", bodyTemplateValues)).thenReturn(body);
        when(templateService.loadByName("subjectTemplateName", subjectTemplateValues)).thenReturn(subject);
        final NotificationContext context = NotificationContext.builder()
            .sevaRequest(sevaRequest)
            .serviceType(serviceType)
            .tenant(tenant)
            .build();

        emailService.send(context);

        final EmailRequest expectedEmailRequest = EmailRequest.builder()
            .body(body)
            .subject(subject)
            .email(emailAddress)
            .build();
        verify(messageQueueRepository).sendEmail(expectedEmailRequest);
    }

    @Test(expected = NotImplementedException.class)
    public void test_should_throw_exception_when_matching_email_message_strategy_not_found() {
        when(sevaRequest.isRequesterEmailAbsent()).thenReturn(false);
        final String emailAddress = "email@email.com";
        final String crn = "crn";
        when(sevaRequest.getRequesterEmail()).thenReturn(emailAddress);
        when(sevaRequest.getCrn()).thenReturn(crn);
        final String status = "Status";
        final String complainantName = "name";
        when(sevaRequest.getRequesterName()).thenReturn(complainantName);
        final String complaintTypeName = "complaintTypeName";
        when(sevaRequest.getServiceTypeName()).thenReturn(complaintTypeName);
        final String locationName = "locationName";
        when(sevaRequest.getLocationName()).thenReturn(locationName);
        final String details = "details";
        when(sevaRequest.getDetails()).thenReturn(details);
        final String formattedDate = "formattedDate";
        when(sevaRequest.getFormattedCreatedDate()).thenReturn(formattedDate);
        when(sevaRequest.getStatusName()).thenReturn(status);
        final Tenant tenant = new Tenant("tenantName", "ulbGrade");
        final List<String> keywords = Collections.emptyList();
        final ServiceType serviceType = new ServiceType("serviceType", keywords);
        when(emailMessageStrategy.matches(any())).thenReturn(false);
        final NotificationContext context = NotificationContext.builder()
            .sevaRequest(sevaRequest)
            .serviceType(serviceType)
            .tenant(tenant)
            .build();

        emailService.send(context);
    }
}