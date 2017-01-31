package org.egov.notification.web.consumer;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.notification.web.services.email.EmailService;
import org.egov.notification.web.model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class EmailNotificationListener {


    @Autowired
    private EmailService emailService;

    @KafkaListener(id = "notificationListener", topics = "egov-notification-email1", group = "notifications")
    public void listen(ConsumerRecord<String, String> record) {
        System.err.println("***** received message [key " + record.key() + "] + value [" + record.value()
                + "] from topic egov-notification-email");
        Gson gson = new Gson();
        EmailRequest request = gson.fromJson(record.value(), EmailRequest.class);
        emailService.sendMail(request.getEmail(), request.getSubject(), request.getBody());
    }

}
