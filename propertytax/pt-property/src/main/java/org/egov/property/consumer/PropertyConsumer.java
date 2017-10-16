package org.egov.property.consumer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.models.DemandId;
import org.egov.models.DemandUpdateMisRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.property.config.PropertiesManager;
import org.egov.property.repository.DemandRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.services.PersisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer class will use for listing property object from kafka server to
 * insert data in postgres database
 * 
 * @author: S Anilkumar
 */
@Service
@Slf4j
@EnableKafka
public class PropertyConsumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PersisterService persisterService;

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	PropertyRepository propertyRepository;

	/**
	 * receive method
	 * 
	 * @param PropertyRequest
	 *            This method is listened whenever property is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateWorkflow()}", "#{propertiesManager.getUpdateWorkflow()}",
			"#{propertiesManager.getApproveWorkflow()}", "#{propertiesManager.getModifyaprroveWorkflow()}",
			"#{propertiesManager.getModifyWorkflow()}" })

	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + propertyRequest);
		if (topic.equalsIgnoreCase(propertiesManager.getCreateWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getModifyWorkflow())) {
			persisterService.addProperty(propertyRequest);
		}

		else if (topic.equalsIgnoreCase(propertiesManager.getApproveWorkflow())) {
			persisterService.updateProperty(propertyRequest);
			Property property = propertyRequest.getProperties().get(0);
			Set<String> id = new HashSet<String>();

			String demands = propertyRepository.getDemandForProperty(property.getUpicNumber());

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<DemandId>> typeReference = new TypeReference<List<DemandId>>() {
			};

			List<DemandId> demandList = mapper.readValue(demands, typeReference);
			demandList.forEach((demand) -> id.add(demand.getId()));

			DemandUpdateMisRequest demandUpdateMisRequest = new DemandUpdateMisRequest();
			demandUpdateMisRequest.setTenantId(property.getTenantId());
			demandUpdateMisRequest.setConsumerCode(property.getUpicNumber());
			demandUpdateMisRequest.setRequestInfo(propertyRequest.getRequestInfo());
			demandUpdateMisRequest.setId(id);
			demandRepository.updateMisDemands(demandUpdateMisRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdateWorkflow())) {
			WorkFlowDetails workFlowDetails = propertyRequest.getProperties().get(0).getPropertyDetail()
					.getWorkFlowDetails();
			if (!workFlowDetails.getAction().equalsIgnoreCase(propertiesManager.getCancelAction())
					&& !workFlowDetails.getAction().equalsIgnoreCase(propertiesManager.getRejectAction())) {
				persisterService.updateProperty(propertyRequest);
			}
		}

		else if (topic.equalsIgnoreCase(propertiesManager.getModifyaprroveWorkflow())) {

			persisterService.movePropertyToHistory(propertyRequest);
		}
	}
}