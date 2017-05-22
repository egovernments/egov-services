package org.egov.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.domain.model.*;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmailService {

    private TemplateService templateService;
    private MessageQueueRepository messageQueueRepository;
    private List<EmailMessageStrategy> emailMessageStrategyList;

    public EmailService(TemplateService templateService,
                        MessageQueueRepository messageQueueRepository,
                        @Qualifier("emailMessageStrategies") List<EmailMessageStrategy> emailMessageStrategyList) {
        this.templateService = templateService;
        this.messageQueueRepository = messageQueueRepository;
        this.emailMessageStrategyList = emailMessageStrategyList;
    }

    public void send(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        if (sevaRequest.isRequesterEmailAbsent()) {
            log.info("Skipping email notification for CRN {}", sevaRequest.getCrn());
            return;
        }
        final EmailRequest emailRequest = getEmailRequest(sevaRequest, serviceType);
        messageQueueRepository.sendEmail(sevaRequest.getRequesterEmail(), emailRequest);
    }

    private EmailRequest getEmailRequest(SevaRequest sevaRequest, ServiceType serviceType) {
        final EmailMessageStrategy emailMessageStrategy = getEmailMessageStrategy(sevaRequest, serviceType);
        final EmailMessageContext messageContext = emailMessageStrategy.getMessageContext(sevaRequest, serviceType);
        return EmailRequest.builder()
            .subject(getEmailSubject(messageContext))
            .body(getMailBody(messageContext))
            .build();
    }

    private String getEmailSubject(EmailMessageContext messageContext) {
        return templateService
            .loadByName(messageContext.getSubjectTemplateName(), messageContext.getSubjectTemplateValues());
    }

    private String getMailBody(EmailMessageContext messageContext) {
        return templateService
            .loadByName(messageContext.getBodyTemplateName(), messageContext.getBodyTemplateValues());
    }

    private EmailMessageStrategy getEmailMessageStrategy(SevaRequest sevaRequest, ServiceType serviceType) {
        return emailMessageStrategyList.stream()
            .filter(strategy -> strategy.matches(sevaRequest, serviceType))
            .findFirst()
            .orElse(new UndefinedEmailMessageStrategy());
    }
}
