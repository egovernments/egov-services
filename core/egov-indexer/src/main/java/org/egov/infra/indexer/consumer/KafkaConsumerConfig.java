package org.egov.infra.indexer.consumer;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.web.contract.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;



@Configuration
@EnableKafka
@PropertySource("classpath:application.properties")
@Order(2)
public class KafkaConsumerConfig implements ApplicationRunner {

	public static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

	public static KafkaMessageListenerContainer<String, String> kafkContainer;
	
	@Value("${spring.kafka.bootstrap.servers}")
    private String brokerAddress;
	
	@Value("${spring.kafka.consumer.group}")
    private String consumerGroup;
	
	@Value("${egov.core.reindex.topic.name}")
	private String reindexTopic;
	
	@Value("${egov.core.legacyindex.topic.name}")
	private String legacyIndexTopic;
        
    @Autowired
    private StoppingErrorHandler stoppingErrorHandler;
    
    @Autowired
    private IndexerMessageListener indexerMessageListener;
    
	@Autowired
	private IndexerApplicationRunnerImpl runner;
    
    public String[] topics = {};
    
     
    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
    	try {
				logger.info("Starting kafka listener container......");			
				startContainer();
			}catch(Exception e){
				logger.error("Exception while Starting kafka listener container: ",e);
			}
    }
    
    public String setTopics(){
    	Map<String, Mapping> mappings = runner.getMappingMaps();
    	String[] topics = new String[mappings.size() + 2];
    	int i = 0;
    	for(Map.Entry<String, Mapping> map: mappings.entrySet()){
    		topics[i] = map.getKey();
    		i++;
    	}
    	topics[i] = reindexTopic;
    	topics[i + 1] = legacyIndexTopic;
    	this.topics = topics;  
    	
    	logger.info("Topics intialized..");
    	return topics.toString();
    }
    
    
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "600000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "300");
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
        
    public boolean startContainer(){
    	KafkaMessageListenerContainer<String, String> container = null;
    	try {
			    container = container();
			    kafkContainer = container;
		} catch (Exception e) {
			logger.error("Container couldn't be started: ",e);
			return false;
		}
    	kafkContainer.start();
    	logger.info("Custom KakfaListenerContainer STARTED...");    	
    	return true;
    	
    }
    
    public boolean pauseContainer(){
    	try {
        	kafkContainer.stop();
		} catch (Exception e) {
			logger.error("Container couldn't be started: ",e);
			return false;
		}	   
    	logger.info("Custom KakfaListenerContainer STOPPED...");    	

    	return true;
    }

}
