package org.egov.tl.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tl.service.PaymentUpdateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.HashMap;

public class ReceiptConsumer {

    private PaymentUpdateService paymentUpdateService;

    @KafkaListener(topics = {"${kafka.topics.receipt.create}"})
    public void listenPayments(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        paymentUpdateService.process(record);
    }
}
