package org.egov.propertyWorkflow.consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.models.City;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.SearchTenantResponse;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	Environment environment;

	@Autowired
	WorkflowProducer workflowProducer;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("auto.offset.reset"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				environment.getProperty("kafka.config.bootstrap_server_config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("consumer.groupId"));
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer
	 * configuration
	 */
	@Bean
	public ConsumerFactory<String, PropertyRequest> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(PropertyRequest.class));

	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, PropertyRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * This method will listen property object from producer and will process
	 * the workflow
	 * 
	 * start workflow, update workflow
	 */

	@KafkaListener(topics = { "#{environment.getProperty('egov.propertytax.create.demand')}",
			"#{environment.getProperty('egov.propertytax.property.update.workflow')}" })
	public void listen(ConsumerRecord<String, PropertyRequest> record) throws Exception {

		PropertyRequest propertyRequest = record.value();
		logger.info("WorkflowConsumer  listen() propertyRequest ---->>  "+propertyRequest);

		if (record.topic().equalsIgnoreCase(environment.getProperty("egov.propertytax.create.demand"))) {

			for (Property property : propertyRequest.getProperties()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getPropertyWorkflowDetailsRequestInfo(property,
						propertyRequest);
				logger.info("WorkflowConsumer  listen() WorkflowDetailsRequestInfo ---->>  "+workflowDetailsRequestInfo);
				ProcessInstance processInstance = workflowUtil.startWorkflow(workflowDetailsRequestInfo,
						environment.getProperty("businessKey"), environment.getProperty("type"),
						environment.getProperty("create.property.comments"));
				property.getPropertyDetail().setStateId(processInstance.getId());
				workflowProducer.send(environment.getProperty("egov.propertytax.property.create.workflow.started"),
						propertyRequest);
			}

		} else if (record.topic().equals(environment.getProperty("egov.propertytax.property.update.workflow"))) {

			for (Property property : propertyRequest.getProperties()) {
				WorkflowDetailsRequestInfo workflowDetailsRequestInfo = getPropertyWorkflowDetailsRequestInfo(property,
						propertyRequest);

				TaskResponse taskResponse = workflowUtil.updateWorkflow(workflowDetailsRequestInfo,
						property.getPropertyDetail().getStateId());
				property.getPropertyDetail().setStateId(taskResponse.getTask().getId());
				String action = workflowDetailsRequestInfo.getWorkflowDetails().getAction();
				if (action.equalsIgnoreCase(environment.getProperty("property.approved"))) {
					String upicNumber = generateUpicNo(property, propertyRequest);
					property.setUpicNumber(upicNumber);
					workflowProducer.send(environment.getProperty("egov.propertytax.property.update.workflow.approved"),
							propertyRequest);
				} else {
					workflowProducer.send(environment.getProperty("egov.propertytax.property.update.workflow.started"),
							propertyRequest);
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
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.hostname"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.basepath"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.searchpath"));
		String url = tenantCodeUrl.toString();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", property.getTenantId());
		try {
			SearchTenantResponse searchTenantResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(),
					propertyRequest.getRequestInfo(), SearchTenantResponse.class);
			if (searchTenantResponse.getTenant().size() > 0) {
				City city = searchTenantResponse.getTenant().get(0).getCity();
				String cityCode = city.getCode();
				String upicFormat = environment.getProperty("upic.number.format");
				upicNumber = getUpicNumber(property.getTenantId(), propertyRequest, upicFormat);
				upicNumber = String.format("%08d", Integer.parseInt(upicNumber));
				if (cityCode != null) {
					upicNumber = cityCode + "-" + upicNumber;
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
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.hostname"));
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.basepath"));
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.createpath"));

		// generating acknowledgement number for all properties
		String UpicNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(upicFormat);
		idrequest.setIdName(environment.getProperty("id.idName"));
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(propertyRequest.getRequestInfo());
		String response = null;
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
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
			if (idResponse.getResponseInfo().getStatus().toString()
					.equalsIgnoreCase(environment.getProperty("success"))) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					UpicNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return UpicNumber;
	}
}