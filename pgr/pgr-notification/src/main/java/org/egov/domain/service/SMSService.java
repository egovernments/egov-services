package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SMSService {
    private TemplateService templateService;
    private MessageQueueRepository messageQueueRepository;
    private List<SMSMessageStrategy> messageStrategyList;

    public SMSService(TemplateService templateService,
                      MessageQueueRepository messageQueueRepository,
                      @Qualifier("smsMessageStrategies") List<SMSMessageStrategy> messageStrategyList) {
        this.templateService = templateService;
        this.messageQueueRepository = messageQueueRepository;
        this.messageStrategyList = messageStrategyList;
    }

    public void send(SevaRequest sevaRequest, ServiceType serviceType) {
        final String smsMessage = getSMSMessage(sevaRequest, serviceType);
        final String mobileNumber = sevaRequest.getMobileNumber();
        messageQueueRepository.sendSMS(mobileNumber, smsMessage);
    }

    private String getSMSMessage(SevaRequest sevaRequest, ServiceType serviceType) {
        final SMSMessageStrategy smsMessageStrategy = getSmsMessageStrategy(sevaRequest, serviceType);
        final SMSMessageContext messageContext = smsMessageStrategy.getMessageContext(sevaRequest, serviceType);
        return templateService.loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
    }

    private SMSMessageStrategy getSmsMessageStrategy(SevaRequest sevaRequest, ServiceType serviceType) {
        return messageStrategyList.stream()
            .filter(strategy -> strategy.matches(sevaRequest, serviceType))
            .findFirst()
            .orElse(new UndefinedSMSMessageStrategy());
    }

}
