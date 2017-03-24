package org.egov.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.domain.model.EmailRequest;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.stereotype.Service;
import org.trimou.util.ImmutableMap;

import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_en";
    private TemplateService templateService;
    private MessageQueueRepository messageQueueRepository;

    public EmailService(TemplateService templateService,
                        MessageQueueRepository messageQueueRepository) {
        this.templateService = templateService;
        this.messageQueueRepository = messageQueueRepository;
    }

    public void send(SevaRequest sevaRequest) {
        if (sevaRequest.isComplainantEmailAbsent()) {
            log.info("Skipping email notification for CRN {}", sevaRequest.getCrn());
            return;
        }
        final EmailRequest emailRequest = getEmailRequest(sevaRequest);
        messageQueueRepository.sendEmail(sevaRequest.getComplainantEmail(), emailRequest);
    }

    private EmailRequest getEmailRequest(SevaRequest sevaRequest) {
        return EmailRequest.builder()
            .subject(getEmailSubject(sevaRequest))
            .body(getEmailBody(sevaRequest))
            .build();
    }

    private String getEmailBody(SevaRequest sevaRequest) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        builder.put("complainantName", sevaRequest.getComplainantName());
        builder.put("crn", sevaRequest.getCrn());
        builder.put("complaintType", sevaRequest.getComplaintTypeName());
        builder.put("locationName", sevaRequest.getLocationName());
        builder.put("complaintDetails", sevaRequest.getDetails());
        builder.put("registeredDate", sevaRequest.getFormattedCreatedDate());
        builder.put("statusName", sevaRequest.getStatusName());
        return templateService.loadByName(EMAIL_BODY_EN_TEMPLATE, builder.build());
    }

    private String getEmailSubject(SevaRequest sevaRequest) {
        Map<Object, Object> map = ImmutableMap.of("crn", sevaRequest.getCrn());
        return templateService.loadByName(EMAIL_SUBJECT_EN_TEMPLATE, map);
    }
}
