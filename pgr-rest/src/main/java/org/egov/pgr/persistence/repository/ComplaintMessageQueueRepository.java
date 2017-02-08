package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.queue.contract.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class ComplaintMessageQueueRepository {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ComplaintMessageQueueRepository(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //TODO: Change to use new Spring Cloud Stream
    public void save(Complaint complaint) {
        String topicName = complaint.getJurisdictionId() + ".mseva.validated";
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, complaint);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onSuccess(SendResult<String, Object> stringTSendResult) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                }
        );
    }
}
