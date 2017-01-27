package org.egov.notification.web.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.notification.web.messaging.MessagePriority;
import org.egov.notification.web.messaging.email.EmailService;
import org.egov.notification.web.messaging.sms.SMSService;
import org.egov.notification.web.model.EmailRequest;
import org.egov.notification.web.model.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

public class NotificationListener {


    @Autowired
    @Qualifier("smsService")
    private SMSService smsService;

    @Autowired
    private EmailService emailService;

    @KafkaListener(id = "notificationListener", topics = {"egov-notification-sms", "egov-notification-email"}, group = "notifications")
    public void listen(ConsumerRecord<String, String> record) {
        if (record.topic().equals("egov-notification-sms")) {
            System.err.println("***** received message [key" + record.key() + "] + value [" + record.value()
                    + "] from topic egov-notification-sms");
            ObjectMapper mapper = new ObjectMapper();
            SMSRequest request;
            try {
                request = mapper.readValue(record.value(), SMSRequest.class);
                smsService.sendSMS(request.getMobile_no(), request.getMessage(), MessagePriority.HIGH);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (record.topic().equals("egov-notification-email")) {
            System.err.println("***** received message [key " + record.key() + "] + value [" + record.value()
                    + "] from topic egov-notification-email");
            Gson gson = new Gson();
            EmailRequest request = gson.fromJson(record.value(), EmailRequest.class);
            emailService.sendMail(request.getEmail(), request.getSubject(), request.getBody());
        }
    }

}
