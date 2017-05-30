package org.egov.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.domain.model.*;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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

    public void send(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        if (sevaRequest.isRequesterEmailAbsent()) {
            log.info("Skipping email notification for CRN {}", sevaRequest.getCrn());
            return;
        }
        final List<EmailRequest> emailRequests = getEmailRequests(sevaRequest, serviceType, tenant);
        emailRequests.forEach(emailRequest ->
            messageQueueRepository.sendEmail(sevaRequest.getRequesterEmail(), emailRequest));
    }

    private List<EmailRequest> getEmailRequests(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        final List<EmailMessageStrategy> strategyList = getEmailMessageStrategy(sevaRequest, serviceType, tenant);
        return strategyList.stream()
            .map(strategy -> getEmailRequest(sevaRequest, serviceType, tenant, strategy))
            .collect(Collectors.toList());
    }

    private EmailRequest getEmailRequest(SevaRequest sevaRequest,
                                         ServiceType serviceType,
                                         Tenant tenant,
                                         EmailMessageStrategy strategy) {
        final EmailMessageContext messageContext = strategy.getMessageContext(sevaRequest, serviceType, tenant);
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

    private List<EmailMessageStrategy> getEmailMessageStrategy(SevaRequest sevaRequest,
                                                               ServiceType serviceType,
                                                               Tenant tenant) {
        final List<EmailMessageStrategy> strategyList = emailMessageStrategyList.stream()
            .filter(strategy -> strategy.matches(sevaRequest, serviceType, tenant))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(strategyList)) {
            return Collections.singletonList(new UndefinedEmailMessageStrategy());
        } else {
            return strategyList;
        }
    }
}
