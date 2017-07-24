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
    
    @Value("${egov.services.hostname}")
    private String egovServiceHost;
    
    @Value("${buisnessdetails.search.uri}")
    private String businessDetailsSearch;
    
    @Value("${coa.search.uri}")
    private String chartOfAccountsSearch;
    
    @Value("${statuscode.search.uri}")
    private String statusCodeSearch;
    
    @Value("${rcptno.gen.uri}")
    private String idGeneration;
    
    @Value("${positionforuser.get.uri}")
    private String getPosition;
    
    @Value("${positionforuser.get.append.uri}")
    private String getPositionUriAppend;
    
    @Value("${create.instrument.uri}")
    private String createInstrument;
    
    @Value("${search.instrument.uri}")
    private String searchInstrument;
    
    @Value("${egov.services.billing_service.hostname}")
    private String billingServiceHostName;

    @Value("${egov.services.billing_service.apportion}")
    private String billingServiceApportion;
    
}
