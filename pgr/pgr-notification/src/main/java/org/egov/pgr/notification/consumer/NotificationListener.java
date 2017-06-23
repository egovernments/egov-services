package org.egov.pgr.notification.consumer;

import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.domain.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class NotificationListener {
    private NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${consumer.topic}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        notificationService.notify(sevaRequest);
    }

}
