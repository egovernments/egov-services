package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.queue.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<String> smsMessages = getSMSMessages(sevaRequest, serviceType);
        final String mobileNumber = sevaRequest.getMobileNumber();
        smsMessages.forEach(message -> messageQueueRepository.sendSMS(mobileNumber, message));
    }

    private List<String> getSMSMessages(SevaRequest sevaRequest, ServiceType serviceType) {
        final List<SMSMessageStrategy> strategyList = getSmsMessageStrategy(sevaRequest, serviceType);
        return strategyList.stream()
            .map(strategy -> getSMSMessage(sevaRequest, serviceType, strategy))
            .collect(Collectors.toList());
    }

    private String getSMSMessage(SevaRequest sevaRequest, ServiceType serviceType, SMSMessageStrategy strategy) {
        final SMSMessageContext messageContext = strategy.getMessageContext(sevaRequest, serviceType);
        return templateService.loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
    }

    private List<SMSMessageStrategy> getSmsMessageStrategy(SevaRequest sevaRequest, ServiceType serviceType) {
        final List<SMSMessageStrategy> strategyList = messageStrategyList.stream()
            .filter(strategy -> strategy.matches(sevaRequest, serviceType))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(strategyList)) {
            return Collections.singletonList(new UndefinedSMSMessageStrategy());
        } else {
            return strategyList;
        }
    }

}
