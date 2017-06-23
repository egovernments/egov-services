package org.egov.pgr.notification.domain.service;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.egov.pgr.notification.domain.model.SMSRequest;
import org.egov.pgr.notification.domain.service.smsstrategy.SMSMessageStrategy;
import org.egov.pgr.notification.domain.service.smsstrategy.UndefinedSMSMessageStrategy;
import org.egov.pgr.notification.persistence.queue.MessageQueueRepository;
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
        final List<SMSRequest> smsRequests = getSMSRequests(context);
        smsRequests.forEach(smsRequest -> messageQueueRepository.sendSMS(smsRequest));
    }

    private List<SMSRequest> getSMSRequests(NotificationContext context) {
        final List<SMSMessageStrategy> strategyList = getSmsMessageStrategies(context);
        return strategyList.stream()
            .map(strategy -> getSMSRequests(context, strategy))
            .collect(Collectors.toList());
    }

    private SMSRequest getSMSRequests(NotificationContext notificationContext,
                                      SMSMessageStrategy strategy) {
        final SMSMessageContext messageContext = strategy.getMessageContext(notificationContext);
        final String smsMessage = templateService
            .loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
        return new SMSRequest(smsMessage, messageContext.getMobileNumber());
    }

    private List<SMSMessageStrategy> getSmsMessageStrategies(NotificationContext context) {
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
