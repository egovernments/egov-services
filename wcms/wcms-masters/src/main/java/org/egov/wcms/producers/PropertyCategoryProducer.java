package org.egov.wcms.producers;

import org.egov.wcms.repository.PropertyCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class PropertyCategoryProducer {

	  @Autowired
	  private KafkaTemplate<String, Object> kafkaTemplate;
	  
      public static final Logger LOGGER = LoggerFactory.getLogger(PropertyCategoryRepository.class);

	    public void sendMessage(final String topic, final String key, final Object message) {
	    	LOGGER.info("KAfkaTemplate: "+kafkaTemplate);
	        final ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
	        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
	            @Override
	            public void onSuccess(final SendResult<String, Object> stringTSendResult) {

	            }

	            @Override
	            public void onFailure(final Throwable throwable) {

	            }
	        });
	    }

}
