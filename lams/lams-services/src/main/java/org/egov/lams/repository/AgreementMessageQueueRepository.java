package org.egov.lams.repository;

import org.egov.lams.model.Agreement;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AgreementMessageQueueRepository {

    public static final Logger logger = LoggerFactory.getLogger(AgreementMessageQueueRepository.class);
    public static final String KEY = "save-agreement";
    public static final String CREATE = "CREATE";

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private String createTopicName;

    private String updateTopicName;

    public AgreementMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                           @Value("${kafka.topics.start.workflow}") String createTopicName,
                                           @Value("${kafka.topics.update.workflow}") String updateTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopicName = createTopicName;
        this.updateTopicName = updateTopicName;
    }

    public void save(AgreementRequest agreementRequest, String action){
        try {
            Agreement agreement = agreementRequest.getAgreement();
            logger.info("agreement before sending" + agreement);
            kafkaTemplate.send(getTopicName(action), KEY, agreementRequest);
        } catch (Exception exception) {
            logger.info("AgreementService : " + exception.getMessage(), exception);
            throw exception;
        }
    }

    private String getTopicName(String action){
        if(action.equals(CREATE))
            return createTopicName;
        else
            return updateTopicName;
    }
}
