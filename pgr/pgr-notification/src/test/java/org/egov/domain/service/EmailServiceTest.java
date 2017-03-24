package org.egov.domain.service;

import org.egov.domain.model.EmailRequest;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trimou.util.ImmutableMap;

import java.util.Map;

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

    @InjectMocks
    private EmailService emailService;

    @Test
    public void test_should_not_send_email_when_complainant_email_is_absent() {
        when(sevaRequest.isComplainantEmailAbsent()).thenReturn(true);

        emailService.send(sevaRequest);

        verify(messageQueueRepository, times(0)).sendEmail(any(), any());
    }

    @Test
    public void test_should_send_email_message_to_topic() {
        when(sevaRequest.isComplainantEmailAbsent()).thenReturn(false);
        final String emailAddress = "email@email.com";
        final String crn = "crn";
        when(sevaRequest.getComplainantEmail()).thenReturn(emailAddress);
        when(sevaRequest.getCrn()).thenReturn(crn);
        final String body = "body";
        final String subject = "subject";

        final Map<Object, Object> subjectRequestMap = ImmutableMap.of("crn", crn);
        when(templateService.loadByName("email_subject_en", subjectRequestMap)).thenReturn(subject);

        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        final String complainantName = "name";
        when(sevaRequest.getComplainantName()).thenReturn(complainantName);
        final String complaintTypeName = "complaintTypeName";
        when(sevaRequest.getComplaintTypeName()).thenReturn(complaintTypeName);
        final String locationName = "locationName";
        when(sevaRequest.getLocationName()).thenReturn(locationName);
        final String details = "details";
        when(sevaRequest.getDetails()).thenReturn(details);
        final String formattedDate = "formattedDate";
        when(sevaRequest.getFormattedCreatedDate()).thenReturn(formattedDate);
        final String statusName = "statusName";
        when(sevaRequest.getStatusName()).thenReturn(statusName);
        builder.put("complainantName", complainantName);
        builder.put("crn", crn);
        builder.put("complaintType", complaintTypeName);
        builder.put("locationName", locationName);
        builder.put("complaintDetails", details);
        builder.put("registeredDate", formattedDate);
        builder.put("statusName", statusName);
        final Map<Object, Object> bodyRequestMap = builder.build();
        when(templateService.loadByName("email_body_en", bodyRequestMap)).thenReturn(body);

        emailService.send(sevaRequest);

        final EmailRequest expectedEmailRequest = EmailRequest.builder()
            .body(body)
            .subject(subject)
            .build();
        verify(messageQueueRepository).sendEmail(emailAddress, expectedEmailRequest);
    }
}