package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.VacancyRemissionRequest;
import org.egov.property.config.PropertiesManager;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.services.PersisterService;
import org.egov.property.services.PropertyService;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@Slf4j
public class VacancyRemissionConsumer {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PropertyService propertyService;

	@Autowired
	PersisterService persisterService;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/*
	 * This method for creating rest template
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{propertiesManager.getVacancyRemissionCreateWorkflow()}",
			"#{propertiesManager.getVacancyRemissionUpdateWorkflow()}",
			"#{propertiesManager.getVacancyRemissionApproveWorkflow()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		VacancyRemissionRequest vacancyRemissionRequest = objectMapper.convertValue(consumerRecord,
				VacancyRemissionRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + vacancyRemissionRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionCreateWorkflow())) {

			persisterService.addVacancyRemission(vacancyRemissionRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionUpdateWorkflow())) {

			String action = vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails().getAction();

			if (!action.equalsIgnoreCase(propertiesManager.getCancelAction())
					&& action.equalsIgnoreCase(propertiesManager.getRejectAction())) {
				persisterService.updateVacancyRemission(vacancyRemissionRequest);
			} else {
				propertyRepository.setIsPropertyUnderWorkFlowFalse(vacancyRemissionRequest.getVacancyRemission().getUpicNo());
			}

		} else if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionApproveWorkflow())) {

			String action = vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails().getAction();

			if (action.equalsIgnoreCase(propertiesManager.getApproveProperty())) {
				propertyRepository.setIsPropertyUnderWorkFlowFalse(vacancyRemissionRequest.getVacancyRemission().getUpicNo());
			}
			persisterService.updateVacancyRemission(vacancyRemissionRequest);
		}
	}
}
