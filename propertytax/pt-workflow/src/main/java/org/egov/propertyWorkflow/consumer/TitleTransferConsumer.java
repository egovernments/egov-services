package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.egov.models.TitleTransferRequest;
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
 * @author Yosadhara
 *
 */
@EnableKafka
@Service
public class TitleTransferConsumer {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private WorkFlowUtil workflowUtil;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen from TitleTransferRequest object from producer
	 * and will process the titleTransfer
	 * 
	 * start titleTransfer, update titleTransfer
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateTitleTransferTaxGenerated()}",
			"#{propertiesManager.getUpdateTitleTransferTaxGenerated()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord,
				TitleTransferRequest.class);
		logger.info("TitleTransferConsumer  listen() titleTransferRequest ---->>  " + titleTransferRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getCreateTitleTransferTaxGenerated())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(titleTransferRequest);
			logger.info(
					"TitleTransferConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
					propertiesManager.getTitileTransferBusinesskey(), propertiesManager.getTitleTransferType(),
					propertiesManager.getTitleTransferComment());

			titleTransferRequest.getTitleTransfer().setStateId(processInstance.getId());
			kafkaTemplate.send(propertiesManager.getCreateTitleTransferWorkflow(), titleTransferRequest);
		} else if (topic.equals(propertiesManager.getUpdateTitleTransferTaxGenerated())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(titleTransferRequest);
			logger.info(
					"TitleTransferConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

			TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
					titleTransferRequest.getTitleTransfer().getStateId(),
					propertiesManager.getTitileTransferBusinesskey());
			titleTransferRequest.getTitleTransfer().setStateId(taskResponse.getTask().getId());

			String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();

			if (action.equalsIgnoreCase(propertiesManager.getApproveProperty()))

				kafkaTemplate.send(propertiesManager.getApproveTitletransfer(), titleTransferRequest);
			else {
				
				kafkaTemplate.send(propertiesManager.getUpdateTitletransferWorkflow(), titleTransferRequest);
				if (action.equalsIgnoreCase(propertiesManager.getReject())) {
					kafkaTemplate.send(propertiesManager.getTitleTransferRejectTopic(), titleTransferRequest);
				}

			}
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
		requestInfo.setUserInfo(titleTransferRequest.getRequestInfo().getUserInfo());
		return requestInfo;
	}
}