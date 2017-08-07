package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Yosadhara
 *
 */
@EnableKafka
@Service
public class TitleTransferConsumer {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);

	@Autowired
	Environment environment;

	@Autowired
	WorkflowProducer workflowProducer;

	@Autowired
	private WorkFlowUtil workflowUtil;

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("auto.offset.reset"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				environment.getProperty("bootstrap.server.config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "titletransferworkflow");
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer
	 * configuration
	 */
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(Object.class));

	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * This method will listen from TitleTransferRequest object from producer
	 * and will process the titleTransfer
	 * 
	 * start titleTransfer, update titleTransfer
	 */
	@KafkaListener(topics = { "#{environment.getProperty('egov.propertytax.property.titletransfer.workflow.create')}",
			"#{environment.getProperty('egov.propertytax.property.titletransfer.workflow.update')}" })
	public void listen(ConsumerRecord<String, Object> record) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		TitleTransferRequest titleTransferRequest = objectMapper.convertValue(record.value(),
				TitleTransferRequest.class);
		logger.info("TitleTransferConsumer  listen() titleTransferRequest ---->>  " + titleTransferRequest);

		if (record.topic()
				.equalsIgnoreCase(environment.getProperty("egov.propertytax.property.titletransfer.workflow.create"))) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(titleTransferRequest);
			logger.info(
					"TitleTransferConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
					environment.getProperty("titletransfer.businesskey"), environment.getProperty("titletransfer.type"),
					environment.getProperty("titletransfer.comment"));

			titleTransferRequest.getTitleTransfer().setStateId(processInstance.getId());
			workflowProducer.send(environment.getProperty("egov.propertytax.property.titletransfer.workflow.created"),
					titleTransferRequest);
		} else if (record.topic()
				.equals(environment.getProperty("egov.propertytax.property.titletransfer.workflow.update"))) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(titleTransferRequest);
			logger.info(
					"TitleTransferConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
					titleTransferRequest.getTitleTransfer().getStateId(), environment.getProperty("titletransfer.businesskey"));
			titleTransferRequest.getTitleTransfer().setStateId(taskResponse.getTask().getId());

			if (taskResponse.getTask().getAction().equalsIgnoreCase(environment.getProperty("action")))

				workflowProducer.send(environment.getProperty("egov.propertytax.property.titletransfer.approved"),
						titleTransferRequest);
			else

				workflowProducer.send(
						environment.getProperty("egov.propertytax.property.titletransfer.workflow.updated"),
						titleTransferRequest);
		}
	}

	/**
	 * This method will generate WorkflowDetailsRequestInfo from the
	 * TitleTransferRequest
	 * 
	 * @param titleTransferRequest
	 * @return workflowDetailsRequestInfo
	 */
	private WorkflowDetailsRequestInfo getWorkflowDetailsRequestInfo(TitleTransferRequest titleTransferRequest) {

		WorkFlowDetails workflowDetails = titleTransferRequest.getTitleTransfer().getWorkFlowDetails();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(titleTransferRequest.getTitleTransfer().getTenantId());
		RequestInfo requestInfo = setWorkFlowRequestInfo(titleTransferRequest);
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
		return workflowDetailsRequestInfo;
	}

	/**
	 * This method will generate RequestInfo from the TitleTransferRequest
	 * 
	 * @param titleTransferRequest
	 * @return RequestInfo
	 */
	private RequestInfo setWorkFlowRequestInfo(TitleTransferRequest titleTransferRequest) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(titleTransferRequest.getRequestInfo().getAction());
		requestInfo.setApiId(titleTransferRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(titleTransferRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(titleTransferRequest.getRequestInfo().getDid());
		requestInfo.setKey(titleTransferRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(titleTransferRequest.getRequestInfo().getRequesterId());
		// TODO temporary fix for date format and need to replace with actual ts
		// value
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		requestInfo.setTs(dateValue);
		// requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(titleTransferRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(titleTransferRequest.getTitleTransfer().getTenantId());
		return requestInfo;
	}
}