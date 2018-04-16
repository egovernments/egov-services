package org.egov.eis.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class AttendanceProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(final String topic, final String key, final Object message) {
        final ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onSuccess(final SendResult<String, Object> stringTSendResult) {

                    }

                    @Override
                    public void onFailure(final Throwable throwable) {

                    }
                });
    }
}