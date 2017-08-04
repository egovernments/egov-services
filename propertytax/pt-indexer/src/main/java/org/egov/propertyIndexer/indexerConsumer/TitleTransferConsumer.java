package org.egov.propertyIndexer.indexerConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Service
@Slf4j
public class TitleTransferConsumer {

	@Autowired
	JestClient client = null;

	@Autowired
	PropertiesManager propertiesManager;
	
	/*
     * This method for getting consumer configuration bean
     */
    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> consumerProperties = new HashMap<String, Object>();
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesManager.getConsumerOffset());
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesManager.getBootstrapServer());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "titleTransferGroupTopic");
        return consumerProperties;
    }

    /*
     * This method will return the consumer factory bean based on consumer configuration
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
                new JsonDeserializer<>(Object.class));

    }

	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	
    @KafkaListener(topics = { "#{propertiesManager.getCreateTitleTranfer()}",
            "#{propertiesManager.getUpdateTitleTransfer()}" })
	public void receive(ConsumerRecord<String, Object> consumerRecord) throws IOException {
		log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is: " + consumerRecord.value());
		TitleTransferRequest titleTransferRequest = new ObjectMapper().convertValue(consumerRecord.value(), TitleTransferRequest.class);
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		String titleTransferData = new ObjectMapper().writeValueAsString(titleTransfer);
		client.execute(new Index.Builder(titleTransferData)
				.index(propertiesManager.getTitleTransferIndex())
				.type(propertiesManager.getTitleTransferType()).id(titleTransfer.getApplicationNo())
				.build());
	}
}
