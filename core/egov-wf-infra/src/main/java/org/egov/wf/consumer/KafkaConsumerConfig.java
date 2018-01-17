package org.egov.wf.consumer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.wf.model.ServiceMap;
import org.egov.wf.model.TopicMap;
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

	@Value("${spring.kafka.bootstrap.servers}")
    private String brokerAddress;
        
    @Autowired
    private StoppingErrorHandler stoppingErrorHandler;
    
    @Autowired
    private WfMessageListener indexerMessageListener;
    
    @Autowired
    private ServiceMap serviceMap;
    
    public String[] topics = {};
    
    @Bean 
    public String setTopics(){
    	
    	List<TopicMap> topicMaps = serviceMap.getTopicMap();
    	String [] topics =  new String[topicMaps.size()];
    	int i = 0;
    	for(TopicMap topicMap : topicMaps) {
    		topics[i++] = topicMap.getFromTopic();
    	}
    	
    	this.topics = topics;  
    	
    	log.info("Topics intialized..");
    	return topics.toString();
    }
    
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "egov-wf");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HashMapDeserializer.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setErrorHandler(stoppingErrorHandler);
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(30000);
        
        log.info("Custom KafkaListenerContainerFactory built...");
        return factory;

    }

    @Bean 
    public KafkaMessageListenerContainer<String, String> container() throws Exception { 
    	 ContainerProperties properties = new ContainerProperties(this.topics); // set more properties
    	 properties.setPauseEnabled(true);
    	 properties.setPauseAfter(0);
    	 properties.setMessageListener(indexerMessageListener);
    	 
         log.info("Custom KafkaListenerContainer built...");

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
			log.error("Container couldn't be started: ",e);
			return false;
		}
    	container.start();
    	log.info("Custom KakfaListenerContainer STARTED...");    	
    	return true;
    	
    }
    
    public boolean pauseContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
		} catch (Exception e) {
			log.error("Container couldn't be started: ",e);
			return false;
		}	   
    	container.stop();
    	log.info("Custom KakfaListenerContainer STOPPED...");    	

    	return true;
    }
    
    public boolean resumeContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
		} catch (Exception e) {
			log.error("Container couldn't be started: ",e);
			return false;
		}
    	container.start();
    	log.info("Custom KakfaListenerContainer STARTED...");    	

    	return true;
    }

}
