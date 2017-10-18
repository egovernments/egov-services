package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.enums.StatusEnum;
import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.propertyWorkflow.repository.IdgenRepository;
import org.egov.propertyWorkflow.repository.PropertyRepository;
import org.egov.propertyWorkflow.repository.TenantRepository;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Debabrata
 *
 */

@Service
public class WorkflowConsumer {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);

	@Autowired
	private WorkFlowUtil workflowUtil;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	IdgenRepository idgenRepository;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen property object from producer and will process
	 * the workflow
	 * 
	 * start workflow, update workflow
	 */

	@KafkaListener(topics = { "#{propertiesManager.getTaxGenerated()}",
			"#{propertiesManager.getModifyPropertTaxGenerated()}",
			"#{propertiesManager.getUpdatePropertyTaxGenerated()}",
			"#{propertiesManager.getUpdateValidatedDemolition()}",
			"#{propertiesManager.getCreateValidatedDemolition()}"})
	public void listen(ConsumerRecord<String, Object> record) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		if (record.topic().equalsIgnoreCase(propertiesManager.getCreateValidatedDemolition())){
			DemolitionRequest demolitionRequest = objectMapper.convertValue(record.value(), DemolitionRequest.class);

			Demolition demolition = demolitionRequest.getDemolition();
			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getDemolitionWorkFlowDetails(demolitionRequest.getDemolition(),
					demolitionRequest);
			logger.info(
					"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);
			
			
			ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
					propertiesManager.getBusinessKey(), propertiesManager.getType(),
					propertiesManager.getComment());
			demolition.setStateId(processInstance.getId());
			kafkaTemplate.send(propertiesManager.getCreateDemolitionWorkflow(), demolitionRequest);
			
		}
		
		else if (record.topic().equalsIgnoreCase(propertiesManager.getUpdateValidatedDemolition())){
			DemolitionRequest demolitionRequest = objectMapper.convertValue(record.value(), DemolitionRequest.class);
			Demolition demolition = demolitionRequest.getDemolition();
			WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getDemolitionWorkFlowDetails(demolitionRequest.getDemolition(),
					demolitionRequest);
			logger.info(
					"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);
			
			TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
					demolition.getStateId(), propertiesManager.getBusinessKey());
			demolition.setStateId(taskResponse.getTask().getId());
			String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();
			if (action.equalsIgnoreCase(propertiesManager.getApproveProperty())) {
				kafkaTemplate.send(propertiesManager.getApproveDemolitionWorkflow(), demolitionRequest);
			}
			else if(action.equalsIgnoreCase(propertiesManager.getReject())
					|| action.equalsIgnoreCase(propertiesManager.getCancel())) {
				kafkaTemplate.send(propertiesManager.getRejectDemolition(), demolitionRequest);
			}
			else{
				
				kafkaTemplate.send(propertiesManager.getUpdateDemolitionWorkflow(), demolitionRequest);
			}
			
			
			
			
		}

		else {
		PropertyRequest propertyRequest = objectMapper.convertValue(record.value(), PropertyRequest.class);
		logger.info("WorkflowConsumer  listen() propertyRequest ---->>  " + propertyRequest);

		if (record.topic().equalsIgnoreCase(propertiesManager.getTaxGenerated())
				|| record.topic().equals(propertiesManager.getModifyPropertTaxGenerated())) {

			for (Property property : propertyRequest.getProperties()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getPropertyWorkflowDetailsRequestInfo(property,
						propertyRequest);
				logger.info(
						"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);
				ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
						propertiesManager.getBusinessKey(), propertiesManager.getType(),
						propertiesManager.getComment());
				logger.info("WorkflowConsumer  listen() after processing workflow ---------- ");
				property.getPropertyDetail().setStateId(processInstance.getId());
				logger.info("WorkflowConsumer  listen() propertyRequest after workflow ---->>  " + propertyRequest);
				if (record.topic().equalsIgnoreCase(propertiesManager.getTaxGenerated())) {
					kafkaTemplate.send(propertiesManager.getCreateWorkflow(), propertyRequest);
				} else {
					kafkaTemplate.send(propertiesManager.getModifyWorkflow(), propertyRequest);
				}
			}

		} else if (record.topic().equals(propertiesManager.getUpdatePropertyTaxGenerated())) {

			for (Property property : propertyRequest.getProperties()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getPropertyWorkflowDetailsRequestInfo(property,
						propertyRequest);
				logger.info(
						"WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  " + workflowDetailsRequestInfo);
				TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
						property.getPropertyDetail().getStateId(), propertiesManager.getBusinessKey());
				property.getPropertyDetail().setStateId(taskResponse.getTask().getId());
				String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();
				if (action.equalsIgnoreCase(propertiesManager.getApproveProperty())) {
					property.getPropertyDetail().setStatus(StatusEnum.ACTIVE);

					if (propertyRepository.checkWhetherPropertyExistsWithUpic(property.getUpicNumber(),
							property.getTenantId(), propertyRequest.getRequestInfo())) {
						kafkaTemplate.send(propertiesManager.getModifyaprroveWorkflow(), propertyRequest);
					}
					kafkaTemplate.send(propertiesManager.getApproveWorkflow(), propertyRequest);
				} else {
					kafkaTemplate.send(propertiesManager.getUpdateWorkflow(), propertyRequest);
					if (action.equalsIgnoreCase(propertiesManager.getReject())
							|| action.equalsIgnoreCase(propertiesManager.getCancel())) {
						kafkaTemplate.send(propertiesManager.getRejectProperty(), propertyRequest);
					}
				}
			}
		}
		
		}
	}

	/**
	 * This method will generate WorkflowDetailsRequestInfo from the Property
	 * 
	 * @param property
	 * @param propertyRequest
	 * @return workflowDetailsRequestInfo
	 */
	private WorkflowDetailsRequestInfo getPropertyWorkflowDetailsRequestInfo(Property property,
			PropertyRequest propertyRequest) {

		WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(property.getTenantId());
		RequestInfo requestInfo = setWorkFlowRequestInfoFromPropertyRequest(propertyRequest, property.getTenantId());
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);
		return workflowDetailsRequestInfo;
	}
	
	
	private WorkflowDetailsRequestInfo getDemolitionWorkFlowDetails(Demolition demolition,DemolitionRequest demolitionRequest){
		

		WorkFlowDetails workflowDetails = demolition.getWorkFlowDetails();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId(demolition.getTenantId());
		RequestInfo requestInfo = setWorkFlowRequestInfoFromDemolitionRequest(demolitionRequest, demolition.getTenantId());
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
	private RequestInfo setWorkFlowRequestInfoFromPropertyRequest(PropertyRequest propertyRequest, String tenantId) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(propertyRequest.getRequestInfo().getAction());
		requestInfo.setApiId(propertyRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(propertyRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(propertyRequest.getRequestInfo().getDid());
		requestInfo.setKey(propertyRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(propertyRequest.getRequestInfo().getRequesterId());
		// TODO temporary fix for date format and need to replace with actual ts
		// value
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		requestInfo.setTs(dateValue);
		// requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(propertyRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(tenantId);
		requestInfo.setUserInfo(propertyRequest.getRequestInfo().getUserInfo());

		return requestInfo;
	}
	

	/**
	 * This method will generate RequestInfo from the PropertyRequest
	 * 
	 * @param PropertyRequest
	 * @param tenantId
	 * @return RequestInfo
	 */
	private RequestInfo setWorkFlowRequestInfoFromDemolitionRequest(DemolitionRequest demolitionRequest, String tenantId) {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction(demolitionRequest.getRequestInfo().getAction());
		requestInfo.setApiId(demolitionRequest.getRequestInfo().getApiId());
		requestInfo.setAuthToken(demolitionRequest.getRequestInfo().getAuthToken());
		requestInfo.setDid(demolitionRequest.getRequestInfo().getDid());
		requestInfo.setKey(demolitionRequest.getRequestInfo().getMsgId());
		requestInfo.setRequesterId(demolitionRequest.getRequestInfo().getRequesterId());
		// TODO temporary fix for date format and need to replace with actual ts
		// value
		String dateValue = null;
		Date date = new Date();
		dateValue = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
		requestInfo.setTs(dateValue);
		// requestInfo.setTs(String.valueOf(propertyRequest.getRequestInfo().getTs()));
		requestInfo.setVer(demolitionRequest.getRequestInfo().getVer());
		requestInfo.setTenantId(tenantId);
		requestInfo.setUserInfo(demolitionRequest.getRequestInfo().getUserInfo());

		return requestInfo;
	}

	/**
	 * This method will generate UPIC NO from the Property
	 * 
	 * @param property
	 * @param propertyRequest
	 * @return upicNumber
	 * @throws Exception
	 */
	public String generateUpicNo(Property property, PropertyRequest propertyRequest) throws Exception {

		return tenantRepository.generateUpicNoRepository(property, propertyRequest);
	}

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 * @throws Exception
	 */
	public String getUpicNumber(String tenantId, PropertyRequest propertyRequest, String upicFormat) throws Exception {

		return idgenRepository.getUpicNumberRepository(tenantId, propertyRequest, upicFormat);
	}

}