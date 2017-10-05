package org.egov.infra.indexer.consumer;


import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.infra.indexer.IndexerInfraApplication;
import org.egov.infra.indexer.web.contract.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableKafka
@PropertySource("classpath:application.properties")
@Slf4j
public class KafkaConsumerConfig {

	public static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

	@Value("${spring.kafka.bootstrap.servers}")
    private String brokerAddress;
        
    @Autowired
    private StoppingErrorHandler stoppingErrorHandler;
    
    @Autowired
    private IndexerMessageListener indexerMessageListener;
    
    public String[] topics = {};
    
     
    public String setTopics(){
    	Map<String, Mapping> mappings = IndexerInfraApplication.getMappingMaps();
    	String[] topics = new String[mappings.size()];
    	int i = 0;
    	for(Map.Entry<String, Mapping> map: mappings.entrySet()){
    		topics[i] = map.getKey();
    		i++;
    	}
    	this.topics = topics;  
    	
    	logger.info("Topics intialized..");
    	return topics.toString();
    }
    
    
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "egov-infra-indexer5");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setErrorHandler(stoppingErrorHandler);
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(30000);
        
        logger.info("Custom KafkaListenerContainerFactory built...");
        return factory;

    }

     
    public KafkaMessageListenerContainer<String, String> container() throws Exception { 
    	 setTopics();
    	 ContainerProperties properties = new ContainerProperties(this.topics); // set more properties
    	 properties.setPauseEnabled(true);
    	 properties.setPauseAfter(0);
    	 properties.setMessageListener(indexerMessageListener);
    	 
         logger.info("Custom KafkaListenerContainer built...");

         return new KafkaMessageListenerContainer<>(consumerFactory(), properties); 
    }
    
    
 /*   @Bean 
    public QueueChannel received() { return new QueueChannel(); }
    
    @Bean 
    public KafkaMessageDrivenChannelAdapter<String, String> adapter(KafkaMessageListenerContainer<String, String> container) {
      KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter = 
    		  new KafkaMessageDrivenChannelAdapter<>(container, ListenerMode.record);
      kafkaMessageDrivenChannelAdapter.setOutputChannel(received()); 
      return kafkaMessageDrivenChannelAdapter; 
    }    */
    
    @Bean
    public boolean startContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
		} catch (Exception e) {
			logger.error("Container couldn't be started: ",e);
			return false;
		}
    	container.start();
    	logger.info("Custom KakfaListenerContainer STARTED...");    	
    	return true;
    	
    }
    
    public boolean pauseContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
		} catch (Exception e) {
			logger.error("Container couldn't be started: ",e);
			return false;
		}	   
    	container.stop();
    	logger.info("Custom KakfaListenerContainer STOPPED...");    	

    	return true;
    }
    
    public boolean resumeContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
		} catch (Exception e) {
			logger.error("Container couldn't be started: ",e);
			return false;
		}
    	container.start();
    	logger.info("Custom KakfaListenerContainer STARTED...");    	

    	return true;
    }

}
