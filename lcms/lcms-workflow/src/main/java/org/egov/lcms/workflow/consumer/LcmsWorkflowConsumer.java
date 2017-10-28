package org.egov.lcms.workflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.workflow.models.Opinion;
import org.egov.lcms.workflow.models.OpinionRequest;
import org.egov.lcms.workflow.models.ProcessInstance;
import org.egov.lcms.workflow.models.RequestInfo;
import org.egov.lcms.workflow.models.TaskResponse;
import org.egov.lcms.workflow.models.WorkFlowDetails;
import org.egov.lcms.workflow.models.WorkflowDetailsRequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author veswanth
 *
 */
@Service
public class LcmsWorkflowConsumer {
	private static final Logger logger = LoggerFactory.getLogger(LcmsWorkflowConsumer.class);

	boolean lcms = true;
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LcmsWorkflowUtil workflowUtil;

	@Autowired
	LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = { "#{propertiesManager.getCreateOpinionWorkflow()}",
			"#{propertiesManager.getUpdateOpinionWorkflow()}" })
	public void listen(ConsumerRecord<String, Object> record) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		if (record.topic().equalsIgnoreCase(propertiesManager.getCreateOpinionWorkflow())) {
			OpinionRequest opinionRequest = objectMapper.convertValue(record.value(), OpinionRequest.class);
			for (Opinion opinion : opinionRequest.getOpinions()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getOpinionWorkflowDetailsRequestInfo(
						opinion.getWorkFlowDetails(), opinion.getTenantId(), opinionRequest.getRequestInfo());

				logger.info(
						"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);

				ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
						propertiesManager.getOpinionBusinessKey(), propertiesManager.getOpinionType(),
						propertiesManager.getOpinionComment());
				logger.info("WorkflowConsumer  listen() after processing workflow ---------- ");
				opinion.setStateId(processInstance.getId());
			}
			logger.info("WorkflowConsumer  listen() opinionRequest after workflow ---->>  " + opinionRequest);
			kafkaTemplate.send(propertiesManager.getCreateOpinion(), opinionRequest);

		} else if (record.topic().equalsIgnoreCase(propertiesManager.getUpdateOpinionWorkflow())) {
			OpinionRequest opinionRequest = objectMapper.convertValue(record.value(), OpinionRequest.class);
			for (Opinion opinion : opinionRequest.getOpinions()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getOpinionWorkflowDetailsRequestInfo(
						opinion.getWorkFlowDetails(), opinion.getTenantId(), opinionRequest.getRequestInfo());
				logger.info(
						"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);
				TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
						opinion.getStateId(), propertiesManager.getBusinessKey());
				opinion.setStateId(taskResponse.getTask().getId());
				// String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();
				kafkaTemplate.send(propertiesManager.getUpdateOpinion(), opinionRequest);
			}
		}
	}

	/**
	 * This method will generate WorkflowDetailsRequestInfo from the Property
	 * 
	 * @param opinion
	 * @param propertyRequest
	 * @return workflowDetailsRequestInfo
	 */
	private WorkflowDetailsRequestInfo getOpinionWorkflowDetailsRequestInfo(WorkFlowDetails workflowDetails,
			String tenantId, org.egov.common.contract.request.RequestInfo rquestInfo) {

		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(tenantId);
		RequestInfo requestInfo = setWorkFlowRequestInfoFromPropertyRequest(rquestInfo, tenantId);
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
		return workflowDetailsRequestInfo;
	}

	/**
	 * This method will generate RequestInfo from the PropertyRequest
	 * 
	 * @param PropertyRequest
	 * @param tenantId
	 * @return RequestInfo
	 */
	private RequestInfo setWorkFlowRequestInfoFromPropertyRequest(
			org.egov.common.contract.request.RequestInfo requestInfo, String tenantId) {

		RequestInfo reqInfo = new RequestInfo();
		reqInfo.setAction(requestInfo.getAction());
		reqInfo.setApiId(requestInfo.getApiId());
		reqInfo.setAuthToken(requestInfo.getAuthToken());
		reqInfo.setDid(requestInfo.getDid());
		reqInfo.setKey(requestInfo.getMsgId());
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		reqInfo.setTs(dateValue);
		reqInfo.setVer(requestInfo.getVer());
		reqInfo.setTenantId(tenantId);
		reqInfo.setUserInfo(requestInfo.getUserInfo());

		return reqInfo;
	}
}