package org.egov.pgr.notification.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.EmailRequest;
import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.service.emailstrategy.EmailMessageStrategy;
import org.egov.pgr.notification.persistence.queue.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void send(NotificationContext context) {
        final List<EmailRequest> emailRequests = getEmailRequests(context);
        emailRequests.forEach(emailRequest ->
            messageQueueRepository.sendEmail(emailRequest));
    }

    private List<EmailRequest> getEmailRequests(NotificationContext context) {
        final List<EmailMessageStrategy> strategyList = getEmailMessageStrategy(context);
        return strategyList.stream()
            .map(strategy -> getEmailRequest(context, strategy))
            .collect(Collectors.toList());
    }

    private EmailRequest getEmailRequest(NotificationContext notificationContext,
                                         EmailMessageStrategy strategy) {
        final EmailMessageContext messageContext = strategy.getMessageContext(notificationContext);
        return EmailRequest.builder()
			.subject(getEmailSubject(messageContext))
			.body(getMailBody(messageContext))
            .email(messageContext.getEmail())
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

    private List<EmailMessageStrategy> getEmailMessageStrategy(NotificationContext context) {
        return emailMessageStrategyList.stream()
            .filter(strategy -> strategy.matches(context))
            .collect(Collectors.toList());
    }
}
