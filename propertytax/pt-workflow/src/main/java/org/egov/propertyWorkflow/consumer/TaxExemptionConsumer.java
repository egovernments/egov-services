package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.egov.models.TaxExemptionRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Anil
 *
 */
@EnableKafka
@Service
public class TaxExemptionConsumer {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private WorkFlowUtil workflowUtil;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen from TaxExemptionRequest object from producer and
	 * will process the taxExemption
	 * 
	 * start taxExemption, update taxExemption
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateValidatedTaxExemption()}",
			"#{propertiesManager.getUpdateValidatedTaxExemption()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		TaxExemptionRequest taxExemptionRequest = objectMapper.convertValue(consumerRecord, TaxExemptionRequest.class);
		logger.info("TaxExemptionConsumer  listen() taxExemptionRequest ---->>  " + taxExemptionRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getCreateValidatedTaxExemption())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(taxExemptionRequest);
			logger.info(
					"TaxExemptionConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
					propertiesManager.getTaxExemptionBusinesskey(), propertiesManager.getTaxExemptionType(),
					propertiesManager.getTaxExemptionComment());

			taxExemptionRequest.getTaxExemption().setStateId(processInstance.getId());

			kafkaTemplate.send(propertiesManager.getTaxExemptionCreateWorkflow(), taxExemptionRequest);
		} else if (topic.equals(propertiesManager.getUpdateValidatedTaxExemption())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(taxExemptionRequest);
			logger.info(
					"TaxExemptionConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
					taxExemptionRequest.getTaxExemption().getStateId(), propertiesManager.getTaxExemptionBusinesskey());
			taxExemptionRequest.getTaxExemption().setStateId(taskResponse.getTask().getId());

			String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();

			if (action.equalsIgnoreCase(propertiesManager.getApproveTaxExemption()))

				kafkaTemplate.send(propertiesManager.getTaxExemptionApproveWorkflow(), taxExemptionRequest);
			else {
				if (action.equalsIgnoreCase(propertiesManager.getCancel())) {
					kafkaTemplate.send(propertiesManager.getRejectTaxExemption(), taxExemptionRequest);
				}
				kafkaTemplate.send(propertiesManager.getTaxExemptionUpdateWorkflow(), taxExemptionRequest);
			}
		}
	}

	/**
	 * This method will generate WorkflowDetailsRequestInfo from the
	 * TaxExemptionRequest
	 * 
	 * @param taxExemptionRequest
	 * @return workflowDetailsRequestInfo
	 */
	private WorkflowDetailsRequestInfo getWorkflowDetailsRequestInfo(TaxExemptionRequest taxExemptionRequest) {

		WorkFlowDetails workflowDetails = taxExemptionRequest.getTaxExemption().getWorkFlowDetails();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(taxExemptionRequest.getTaxExemption().getTenantId());
		RequestInfo requestInfo = setWorkFlowRequestInfo(taxExemptionRequest);
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
		return workflowDetailsRequestInfo;
	}

	/**
	 * This method will generate RequestInfo from the TaxExemptionRequest
	 * 
	 * @param taxExemptionRequest
	 * @return RequestInfo
	 */
	private RequestInfo setWorkFlowRequestInfo(TaxExemptionRequest taxExemptionRequest) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(taxExemptionRequest.getRequestInfo().getAction());
		requestInfo.setApiId(taxExemptionRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(taxExemptionRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(taxExemptionRequest.getRequestInfo().getDid());
		requestInfo.setKey(taxExemptionRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(taxExemptionRequest.getRequestInfo().getRequesterId());
		// TODO temporary fix for date format and need to replace with actual ts
		// value
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		requestInfo.setTs(dateValue);
		// requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(taxExemptionRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(taxExemptionRequest.getTaxExemption().getTenantId());
		requestInfo.setUserInfo(taxExemptionRequest.getRequestInfo().getUserInfo());
		return requestInfo;
	}
}