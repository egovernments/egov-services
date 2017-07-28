package org.egov.collection.notification.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.notification.domain.service.NotificationService;
import org.egov.collection.notification.web.contract.ReceiptRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CollectionNotificationListener {
    private NotificationService notificationService;

    private ObjectMapper objectMapper;

    public CollectionNotificationListener(NotificationService notificationService,ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${collection.create.topic}")
    public void process(HashMap<String, Object> receiptRequestMap) {
        ReceiptRequest receiptRequest = objectMapper.convertValue(receiptRequestMap, ReceiptRequest.class);
        notificationService.notify(receiptRequest);
    }

}
