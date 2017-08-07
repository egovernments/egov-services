package org.egov.pgr.persistence.repository;


import org.egov.pgr.persistence.dto.ServiceTypeConfiguration;
import org.egov.pgr.producers.PGRProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class ServiceTypeConfigurationMessageQueueRepository {

    private PGRProducer producer;

    private String topicName;

    private String key;
    
    private String updateTopicname;
    
    private String updatekey;

    public ServiceTypeConfigurationMessageQueueRepository(PGRProducer producer,
                                                          @Value("${kafka.topics.servicetypeconfiguration.create.name}") String topicName,
                                                          @Value("${kafka.topics.servicetypeconfiguration.create.key}") String key,
                                                          @Value("${kafka.topics.servicetypeconfiguration.update.name}") String updateTopicname,
                                                          @Value("${kafka.topics.servicetypeconfiguration.update.key}") String updatekey) {
        this.producer = producer;
        this.topicName = topicName;
        this.key = key;
        this.updatekey=updatekey;
        this.updateTopicname=updateTopicname;
    }

    public void save(ServiceTypeConfiguration serviceTypeConfiguration){
    	final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String serviceRequestValue = null;
		
		try {
			serviceRequestValue = mapper.writeValueAsString(serviceTypeConfiguration);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	producer.sendMessage(topicName, key, serviceRequestValue);

    }
    
    public void saves(ServiceTypeConfiguration serviceTypeConfiguration){
    	final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String serviceRequestValue = null;
		
		try {
			serviceRequestValue = mapper.writeValueAsString(serviceTypeConfiguration);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	producer.sendMessage(updateTopicname, updatekey, serviceRequestValue);

    }
}