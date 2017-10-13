package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.enums.StatusEnum;
import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.models.City;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.SearchTenantResponse;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	RestTemplate restTemplate;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen property object from producer and will process
	 * the workflow
	 * 
	 * start workflow, update workflow
	 */

	@KafkaListener(topics = { "#{propertiesManager.getTaxGenerated()}",
			"#{propertiesManager.getModifyPropertTaxGenerated()}",
			"#{propertiesManager.getUpdatePropertyTaxGenerated()}" })
	public void listen(ConsumerRecord<String, Object> record) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

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
	 * This method will generate UPIC NO from the Property
	 * 
	 * @param property
	 * @param propertyRequest
	 * @return upicNumber
	 */
	private String generateUpicNo(Property property, PropertyRequest propertyRequest) {

		String upicNumber = null;
		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasepath());
		tenantCodeUrl.append(propertiesManager.getTenantSearchpath());
		String url = tenantCodeUrl.toString();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", property.getTenantId());
		logger.info("WorkflowConsumer builderuri :" + builder.buildAndExpand().toUri()
				+ "\n WorkflowConsumer PropertyRequest :" + propertyRequest.getRequestInfo());
		try {
			SearchTenantResponse searchTenantResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(),
					propertyRequest.getRequestInfo(), SearchTenantResponse.class);
			logger.info("WorkflowConsumer SearchTenantResponse  ---->>  " + searchTenantResponse);
			if (searchTenantResponse.getTenant().size() > 0) {
				City city = searchTenantResponse.getTenant().get(0).getCity();
				String cityCode = city.getCode();
				String upicFormat = propertiesManager.getUpicNumberFormat();
				upicNumber = getUpicNumber(property.getTenantId(), propertyRequest, upicFormat);
				upicNumber = String.format("%08d", Integer.parseInt(upicNumber));
				if (cityCode != null) {
					upicNumber = cityCode + upicNumber;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return upicNumber;
	}

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 */
	public String getUpicNumber(String tenantId, PropertyRequest propertyRequest, String upicFormat) {

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(propertiesManager.getIdHostName());
		idGenerationUrl.append(propertiesManager.getIdBasepath());
		idGenerationUrl.append(propertiesManager.getIdCreatepath());

		// generating acknowledgement number for all properties
		String UpicNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(upicFormat);
		idrequest.setIdName(propertiesManager.getIdName());
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(propertyRequest.getRequestInfo());
		String response = null;
		logger.info("WorkflowConsumer idGenerationUrl :" + idGenerationUrl.toString()
				+ "\n WorkflowConsumer idGenerationRequest :" + idGeneration);
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
			logger.info("WorkflowConsumer getupicnumber response  ---->>  " + response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			// TODO throw error exception
			// Error error = errorResponse.getErrors().get(0);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase(propertiesManager.getSuccess())) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					UpicNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return UpicNumber;
	}
}