package org.egov.pgr.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.domain.model.ServiceDefinition;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.web.contract.ServiceDefinitionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceDefinitionMessageQueueRepository {

    public static final Logger logger = LoggerFactory.getLogger(ServiceDefinition.class);
    public static final String CREATE = "CREATE";

    private PGRProducer producer;

    private String createTopicName;

    private String createkey;


    public ServiceDefinitionMessageQueueRepository(PGRProducer producer,
                                                   @Value("${kafka.topics.servicedefinition.create.name}") String createTopicName,
                                                   @Value("${kafka.topics.servicedefinition.create.key") String createkey) {
        this.producer = producer;
        this.createTopicName = createTopicName;
        this.createkey = createkey;
    }

    public void save(ServiceDefinitionRequest serviceDefinitionRequest, String action){
        final ObjectMapper mapper = new ObjectMapper();

        try {
            String value = mapper.writeValueAsString(serviceDefinitionRequest);
            if(CREATE.equalsIgnoreCase(action))
                producer.sendMessage(createTopicName, createkey, value);
        }
        catch (JsonProcessingException e) {
            logger.error("Exception while pushing to kafka queue : " + e);
        }
    }
}
