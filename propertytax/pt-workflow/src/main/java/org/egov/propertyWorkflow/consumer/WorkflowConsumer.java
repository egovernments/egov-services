package org.egov.propertyWorkflow.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.models.ProcessInstanceRequest;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Debabrata
 *
 */

@EnableKafka
@Service
public class WorkflowConsumer {
	@Autowired
	private WorkFlowUtil workflowUtil;

	@Autowired
	Environment environment;

	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("auto.offset.reset"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				environment.getProperty("bootstrap.server.config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("consumer.groupId"));
		return consumerProperties;
	}

	@Bean
	public ConsumerFactory<String, PropertyRequest> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(PropertyRequest.class));

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, PropertyRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@KafkaListener(topics = { "#{environment.getProperty('egov.propertytax.property.create.approved')}",
			"#{environment.getProperty('egov.propertytax.property.update.approved')}" })
	public void listen(ConsumerRecord<String, PropertyRequest> record) throws Exception {

		PropertyRequest propertyRequest = record.value();

		if (record.topic().equalsIgnoreCase(environment.getProperty("egov.propertytax.property.create.approved"))) {

			for (Property property : propertyRequest.getProperties()) {
				WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
				RequestInfo requestInfo = setWorkFlowRequestInfoFromPropertyRequest(propertyRequest,
						property.getTenantId());
				workflowDetailsRequestInfo.setRequestInfo(requestInfo);
				workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
				ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
				workflowUtil.startWorkflow(processInstanceRequest);
			}
		} else if (record.topic().equals(environment.getProperty("egov.propertytax.property.update.approved"))) {
			for (Property property : propertyRequest.getProperties()) {
				WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
				workflowDetailsRequestInfo.setTenantId(property.getTenantId());
				workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
				RequestInfo requestInfo = setWorkFlowRequestInfoFromPropertyRequest(propertyRequest,
						property.getTenantId());
				workflowDetailsRequestInfo.setRequestInfo(requestInfo);
				workflowUtil.updateWorkflow(workflowDetailsRequestInfo);
			}
		}

	}

	private RequestInfo setWorkFlowRequestInfoFromPropertyRequest(PropertyRequest propertyRequest, String tenantId) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(propertyRequest.getRequestInfo().getAction());
		requestInfo.setApiId(propertyRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(propertyRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(propertyRequest.getRequestInfo().getDid());
		requestInfo.setKey(propertyRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(propertyRequest.getRequestInfo().getRequesterId());
		requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(propertyRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(tenantId);

		return requestInfo;
	}
}
