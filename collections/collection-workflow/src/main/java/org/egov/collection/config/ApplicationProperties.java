package org.egov.collection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.env.Environment;

@Getter
@Setter
@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {

    @Autowired
    Environment environment;

    private String businessType;

    private String type;

    private String comments;

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

    @Value("${kafka.topics.receipt.create.name}")
    private String kafkaStartWorkflowTopic;

    @Value("${kafka.topics.workflow.start.key}")
    private String kafkaStartWorkflowTopicKey;
    
    @Value("${kafka.topics.receipt.update.name}")
    private String kafkaUpdateworkflowTopic;
    
    @Value("${kafka.topics.workflow.update.key}")
    private String kafkaUpdateworkflowTopicKey;

    @Value("${kafka.topics.update.receipt.workflowdetails}")
    private String kafkaUpdateWorkFlowDetails;


    public String getType() {
        return environment.getProperty("type");
    }

    public String getBusinessType() {
        return environment.getProperty("businessType");
    }

    public String getComments() {
        return environment.getProperty("create.receipt.comments");
    }
       

}
