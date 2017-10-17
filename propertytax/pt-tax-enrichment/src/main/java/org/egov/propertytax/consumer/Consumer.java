package org.egov.propertytax.consumer;

import java.util.Map;

import org.egov.models.DemandResponse;
import org.egov.models.PropertyRequest;
import org.egov.models.TitleTransferRequest;
import org.egov.propertytax.config.PropertiesManager;
import org.egov.propertytax.service.DemandService;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Consumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private DemandService demandService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * receive method
	 *
	 * @param PropertyRequest
	 *            This method is listened whenever property is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreatePropertyTaxCalculated()}",
			"#{propertiesManager.getCreateTitleTransferTaxCalculated()}",
			"#{propertiesManager.getUpdateTitleTransferTaxCalculated()}",
			"#{propertiesManager.getModifyPropertyTaxCalculated()}",
			"#{propertiesManager.getUpdatePropertyTaxCalculated()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		if (topic.equalsIgnoreCase(propertiesManager.getCreatePropertyTaxCalculated())) {
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			log.info("consumer topic value is: " + topic + " consumer value is" + consumerRecord);
			demandService.createDemand(propertyRequest);
			log.info("demand generated >>>> \n next topic ----->> " + propertiesManager.getCreatePropertyTaxGenerated()
					+ " \n Property >>>>> " + propertyRequest);
			kafkaTemplate.send(propertiesManager.getCreatePropertyTaxGenerated(), propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getCreateTitleTransferTaxCalculated())) {
			TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord,
					TitleTransferRequest.class);
			DemandResponse demandResponse = demandService.createDemandForTitleTransfer(titleTransferRequest);
			titleTransferRequest.getTitleTransfer().setDemandId(demandResponse.getDemands().get(0).getId());
			kafkaTemplate.send(propertiesManager.getCreateTitleTransferTaxGenerated(), titleTransferRequest);

		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdateTitleTransferTaxCalculated())) {
			TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord,
					TitleTransferRequest.class);
			demandService.updateDemandForTitleTransfer(titleTransferRequest);
			kafkaTemplate.send(propertiesManager.getUpdateTitleTransferTaxGenerated(), titleTransferRequest);

		} else if (topic.equalsIgnoreCase(propertiesManager.getModifyPropertyTaxCalculated())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdatePropertyTaxCalculated())) {
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			
			Boolean isModify = Boolean.FALSE;
			if (topic.equalsIgnoreCase(propertiesManager.getModifyPropertyTaxCalculated())){
				isModify = Boolean.TRUE;
			}
				
			
			demandService.updateDemand(propertyRequest,isModify);

			if (topic.equalsIgnoreCase(propertiesManager.getModifyPropertyTaxCalculated()))
				kafkaTemplate.send(propertiesManager.getModifyPropertyTaxGenerated(), propertyRequest);
			else
				kafkaTemplate.send(propertiesManager.getUpdatePropertyTaxGenerated(), propertyRequest);
		}

	}
}
