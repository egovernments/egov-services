package org.egov.consumer;

import org.egov.domain.model.SevaRequest;
import org.egov.domain.service.EmailService;
import org.egov.domain.service.SMSService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class NotificationListener {
    private SMSService smsService;
    private EmailService emailService;

    public NotificationListener(SMSService smsService, EmailService emailService) {
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${incoming.topic}", group = "${consumer.id}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        smsService.send(sevaRequest);
        emailService.send(sevaRequest);
    }

}
