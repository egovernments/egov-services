package org.egov.mr.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class MarriageRegnProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessage(String topic, String key, Object message) {
		ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

			@Override
			public void onSuccess(SendResult<String, Object> result) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable ex) {
			}
		});
	}
}
