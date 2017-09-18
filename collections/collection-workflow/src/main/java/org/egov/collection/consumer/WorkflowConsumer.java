package org.egov.collection.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.ReceiptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkflowConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	@Autowired
	private WorkflowService workflowService;

    @Autowired
    private ObjectMapper objectMapper;

	@KafkaListener(topics = {"${kafka.topics.receipt.create.name}","${kafka.topics.receipt.update.name}"})
    public void listen(final Map<String, Object> receiptRequestMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Record: "+receiptRequestMap);
        ReceiptRequest receiptRequest = objectMapper.convertValue(receiptRequestMap, ReceiptRequest.class);
        try {
            if(topic.equals(applicationProperties.getKafkaStartWorkflowTopic())){
                logger.info("Consuming start workflow request");
                workflowService.startWorkflow(receiptRequest);
            }else if(topic.equals(applicationProperties.getKafkaUpdateworkflowTopic())){
                logger.info("Consuming update workflow request");
               // workflowService.updateWorkflow(receiptRequest.getReceipt().get(0).getWorkflowDetails());
            }

        } catch (final Exception e) {
            e.printStackTrace();
            logger.error("Error while listening to value: "+receiptRequestMap+" on topic: "+topic+": ", e.getMessage());
        }
    }

}