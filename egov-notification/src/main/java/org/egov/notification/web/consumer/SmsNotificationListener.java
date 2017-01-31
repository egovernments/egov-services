package org.egov.notification.web.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.notification.web.services.MessagePriority;
import org.egov.notification.web.services.sms.SMSService;
import org.egov.notification.web.model.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

public class SmsNotificationListener {

    @Autowired
    @Qualifier("smsService")
    private SMSService smsService;

    @KafkaListener(id = "notificationListener", topics = "egov-notification-sms1", group = "notifications")
    public void listen(ConsumerRecord<String, String> record) {
        System.err.println("***** received message [key" + record.key() + "] + value [" + record.value() + "] from topic egov-notification-sms");
        ObjectMapper mapper = new ObjectMapper();
        SMSRequest request;
        try {
            request = mapper.readValue(record.value(), SMSRequest.class);
            smsService.sendSMS(request.getMobileNumber(), request.getMessage(), MessagePriority.HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
