package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.egov.models.VacancyRemissionRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.propertyWorkflow.repository.PropertyRepository;
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
public class VacanyRemissionWorkflowConsumer {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private WorkFlowUtil workflowUtil;
	
	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen from TitleTransferRequest object from producer
	 * and will process the titleTransfer
	 * 
	 * start titleTransfer, update titleTransfer
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateValidatedVacancyRemission()}",
			"#{propertiesManager.getUpdateValidatedVacancyRemission()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		VacancyRemissionRequest vacancyRemissionRequest = objectMapper.convertValue(consumerRecord,
				VacancyRemissionRequest.class);
		logger.info("VacancyRemissionConsumer  listen() vacancyRemissionRequest ---->>  " + vacancyRemissionRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getCreateValidatedVacancyRemission())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(
					vacancyRemissionRequest);
			logger.info("VacancyRemissionConsumer  listen() WorkflowDetailsRequestInfo ---->>  "
					+ workflowDetailsRequestInfo);

			ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
					propertiesManager.getVacancyRemissionBusinesskey(), propertiesManager.getVacancyRemissionType(),
					propertiesManager.getVacancyRemissionComment());

			vacancyRemissionRequest.getVacancyRemission().setStateId(processInstance.getId());

			kafkaTemplate.send(propertiesManager.getVacancyRemissionCreateWorkflow(), vacancyRemissionRequest);
		} else if (topic.equals(propertiesManager.getUpdateValidatedVacancyRemission())) {

			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getWorkflowDetailsRequestInfo(
					vacancyRemissionRequest);
			logger.info("VacancyRemissionConsumer  listen() WorkflowDetailsRequestInfo ---->>  "
					+ workflowDetailsRequestInfo);
			TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
					vacancyRemissionRequest.getVacancyRemission().getStateId(),
					propertiesManager.getVacancyRemissionBusinesskey());
			vacancyRemissionRequest.getVacancyRemission().setStateId(taskResponse.getTask().getId());
			String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();
			

			if (action.equalsIgnoreCase(propertiesManager.getCancel())) {
				kafkaTemplate.send(propertiesManager.getRejectVacancyRemission(), vacancyRemissionRequest);
			} else {
				
				if (action.equalsIgnoreCase(propertiesManager.getApproveVacancyRemission())) {
					
					vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails()
							.setAction(propertiesManager.getApproveVacancyRemission());
					
					kafkaTemplate.send(propertiesManager.getVacancyRemissionApproveWorkflow(), vacancyRemissionRequest);
				}
				kafkaTemplate.send(propertiesManager.getVacancyRemissionUpdateWorkflow(), vacancyRemissionRequest);
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
	private WorkflowDetailsRequestInfo getWorkflowDetailsRequestInfo(VacancyRemissionRequest vacancyRemissionRequest) {

		WorkFlowDetails workflowDetails = vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(vacancyRemissionRequest.getVacancyRemission().getTenantId());
		RequestInfo requestInfo = setWorkFlowRequestInfo(vacancyRemissionRequest);
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
	private RequestInfo setWorkFlowRequestInfo(VacancyRemissionRequest vacancyRemissionRequest) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(vacancyRemissionRequest.getRequestInfo().getAction());
		requestInfo.setApiId(vacancyRemissionRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(vacancyRemissionRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(vacancyRemissionRequest.getRequestInfo().getDid());
		requestInfo.setKey(vacancyRemissionRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(vacancyRemissionRequest.getRequestInfo().getRequesterId());
		// TODO temporary fix for date format and need to replace with actual ts
		// value
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		requestInfo.setTs(dateValue);
		// requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(vacancyRemissionRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(vacancyRemissionRequest.getVacancyRemission().getTenantId());
		requestInfo.setUserInfo(vacancyRemissionRequest.getRequestInfo().getUserInfo());
		return requestInfo;
	}
}