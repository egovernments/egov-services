package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.PaymentDetailsRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsQueueRepository {

    @Autowired
    private LogAwareKafkaTemplate kafkaTemplate;

    @Value("${egov.swm.paymentdetails.save.topic}")
    private String createTopic;

    @Value("${egov.swm.paymentdetails.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.paymentdetails.indexer.topic}")
    private String indexTopic;

    public PaymentDetailsRequest save(final PaymentDetailsRequest paymentDetailsRequest) {

        kafkaTemplate.send(createTopic, paymentDetailsRequest);

        kafkaTemplate.send(indexTopic, paymentDetailsRequest);

        return paymentDetailsRequest;
    }

    public PaymentDetailsRequest update(final PaymentDetailsRequest paymentDetailsRequest) {

        kafkaTemplate.send(updateTopic, paymentDetailsRequest);

        kafkaTemplate.send(indexTopic, paymentDetailsRequest);

        return paymentDetailsRequest;
    }
}
