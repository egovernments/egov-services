package org.egov.domain.service;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SMSMessageContext;
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

    public void send(NotificationContext context) {
        final List<String> smsMessages = getSMSMessages(context);
        final String mobileNumber = context.getSevaRequest().getMobileNumber();
        smsMessages.forEach(message -> messageQueueRepository.sendSMS(mobileNumber, message));
    }

    private List<String> getSMSMessages(NotificationContext context) {
        final List<SMSMessageStrategy> strategyList = getSmsMessageStrategy(context);
        return strategyList.stream()
            .map(strategy -> getSMSMessage(context, strategy))
            .collect(Collectors.toList());
    }

    private String getSMSMessage(NotificationContext notificationContext,
                                 SMSMessageStrategy strategy) {
        final SMSMessageContext messageContext = strategy.getMessageContext(notificationContext);
        return templateService.loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
    }

    private List<SMSMessageStrategy> getSmsMessageStrategy(NotificationContext context) {
        final List<SMSMessageStrategy> strategyList = messageStrategyList.stream()
            .filter(strategy -> strategy.matches(context))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(strategyList)) {
            return Collections.singletonList(new UndefinedSMSMessageStrategy());
        } else {
            return strategyList;
        }
    }

}
