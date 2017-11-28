package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsQueueRepository {

    private final LogAwareKafkaTemplate kafkaTemplate;

    private final String createTopic;

    private final String updateTopic;

    private final String indexTopic;

    public VendorPaymentDetailsQueueRepository(final LogAwareKafkaTemplate kafkaTemplate,
            @Value("${egov.swm.vendorpaymentdetails.save.topic}") final String createTopic,
            @Value("${egov.swm.vendorpaymentdetails.update.topic}") final String updateTopic,
            @Value("${egov.swm.vendorpaymentdetails.indexer.topic}") final String indexTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopic = createTopic;
        this.updateTopic = updateTopic;
        this.indexTopic = indexTopic;
    }

    public VendorPaymentDetailsRequest save(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        kafkaTemplate.send(createTopic, vendorPaymentDetailsRequest);

        kafkaTemplate.send(indexTopic, vendorPaymentDetailsRequest);

        return vendorPaymentDetailsRequest;
    }

    public VendorPaymentDetailsRequest update(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        kafkaTemplate.send(updateTopic, vendorPaymentDetailsRequest);

        kafkaTemplate.send(indexTopic, vendorPaymentDetailsRequest);

        return vendorPaymentDetailsRequest;
    }
}
