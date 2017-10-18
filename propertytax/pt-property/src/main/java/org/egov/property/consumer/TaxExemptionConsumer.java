package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.TaxExemptionRequest;
import org.egov.models.WorkFlowDetails;
import org.egov.property.config.PropertiesManager;
import org.egov.property.services.PersisterService;
import org.egov.property.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@Slf4j
public class TaxExemptionConsumer {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PropertyService propertyService;

	@Autowired
	PersisterService persisterService;

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{propertiesManager.getTaxExemptionCreateWorkflow()}",
			"#{propertiesManager.getTaxExemptionUpdateWorkflow()}",
			"#{propertiesManager.getTaxExemptionApproveWorkflow()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TaxExemptionRequest taxExemptionRequest = objectMapper.convertValue(consumerRecord, TaxExemptionRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + taxExemptionRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getTaxExemptionCreateWorkflow())) {
			persisterService.addTaxExemption(taxExemptionRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getTaxExemptionUpdateWorkflow())) {
			WorkFlowDetails workFlowDetails = taxExemptionRequest.getTaxExemption().getWorkFlowDetails();
			if (!workFlowDetails.getAction().equalsIgnoreCase(propertiesManager.getCancelAction())
					&& !workFlowDetails.getAction().equalsIgnoreCase(propertiesManager.getRejectAction())) {
				persisterService.updateTaxExemption(taxExemptionRequest);
			}
		} else if (topic.equalsIgnoreCase(propertiesManager.getTaxExemptionApproveWorkflow())) {
			propertyService.savePropertyHistoryandUpdateProperty(taxExemptionRequest);
		}
	}
}
