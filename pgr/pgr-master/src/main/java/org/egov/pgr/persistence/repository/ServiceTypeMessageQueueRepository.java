package org.egov.pgr.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.persistence.dto.ServiceType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.web.contract.ServiceTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeMessageQueueRepository {

    public static final Logger logger = LoggerFactory.getLogger(ServiceType.class);
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";

    private PGRProducer producer;

    private String createTopicName;

    private String createkey;

    private String updateTopicName;

    private String updateKey;

    public ServiceTypeMessageQueueRepository(PGRProducer producer,
                                             @Value("${kafka.topics.servicetypes.create.name}") String createTopicName,
                                             @Value("${kafka.topics.servicetypes.create.key}") String createkey,
                                             @Value("${kafka.topics.servicetypes.update.name}") String updateTopicName,
                                             @Value("${kafka.topics.servicetypes.update.key}") String updatekey) {
        this.producer = producer;
        this.createTopicName = createTopicName;
        this.createkey = createkey;
        this.updateTopicName = updateTopicName;
        this.updateKey = updatekey;
    }

    public void save(ServiceTypeRequest serviceTypeRequest, String action){
        final ObjectMapper mapper = new ObjectMapper();

        try {
            String serviceTypeValue = mapper.writeValueAsString(serviceTypeRequest);
            if(CREATE.equalsIgnoreCase(action))
                producer.sendMessage(createTopicName, createkey, serviceTypeValue);

            if(UPDATE.equalsIgnoreCase(action))
                producer.sendMessage(updateTopicName, updateKey, serviceTypeValue);
        }
        catch (JsonProcessingException e) {
            logger.error("Exception while pushing to kafka queue : " + e);
        }
    }
}