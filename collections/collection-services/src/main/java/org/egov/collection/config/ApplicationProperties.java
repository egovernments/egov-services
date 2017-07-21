package org.egov.collection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {
	
    
    @Value("${kafka.topics.receipt.create.name}")
    private String createReceiptTopicName;
    
    @Value("${kafka.topics.receipt.create.key}")
    private String createReceiptTopicKey;
    
    @Value("${kafka.topics.receipt.cancel.name}")
    private String cancelReceiptTopicName;
    
    @Value("${kafka.topics.receipt.cancel.key}")
    private String cancelReceiptTopicKey;
    
    @Value("${egov.services.workflow_service.hostname}")
    private String workflowServiceHostName;

    @Value("${egov.services.workflow_service.startpath}")
    private String workflowServiceStartPath;

    @Value("${egov.services.workflow_service.updatepath}")
    private String workflowServiceUpdatePath;

    @Value("${egov.services.workflow_service.searchpath}")
    private String workflowServiceSearchPath;

    @Value("${egov.services.workflow_service.taskpath}")
    private String workflowServiceTaskPAth;

    @Value("${kafka.topics.workflow.start.name}")
    private String kafkaStartWorkflowTopic;

    @Value("${kafka.topics.workflow.start.key}")
    private String kafkaStartWorkflowTopicKey;
    
    @Value("${kafka.topics.workflow.update.name}")
    private String kafkaUpdateworkflowTopic;
    
    @Value("${kafka.topics.workflow.update.key}")
    private String kafkaUpdateworkflowTopicKey;
    
    @Value("${kafka.topics.stateId.update.name}")
    private String kafkaUpdateStateIdTopic;
    
    @Value("${kafka.topics.stateId.update.key}")
    private String kafkaUpdateStateIdTopicKey;
    
    @Value("${BD_SEARCH_URI}")
    private String businessDetailsSearch;
    
    @Value("${COA_SEARCH_URI}")
    private String chartOfAccountsSearch;
    
    @Value("${STATUS_SEARCH_URI}")
    private String statusCodeSearch;
    
    @Value("${ID_GEN_URI}")
    private String idGeneration;
    
    @Value("${GET_POSITION_URI}")
    private String getPosition;
    
    @Value("${GET_POSITION_URI_APPEND}")
    private String getPositionUriAppend;
    
    @Value("${CREATE_INSTRUMENT}")
    private String createInstrument;
    
    @Value("${SEARCH_INSTRUMENT}")
    private String searchInstrument;
    
    @Value("${egov.services.billing_service.hostname}")
    private String billingServiceHostName;

    @Value("${egov.services.billing_service.apportion}")
    private String billingServiceApportion;
    
}
