package org.egov.mr.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.broker.MarriageRegnConsumer;
import org.egov.mr.broker.MarriageRegnProducer;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.RegistrationUnitRepository;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RegistrationUnitService {

	public static final Logger LOGGER = LoggerFactory.getLogger(MarriageRegnConsumer.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private MarriageRegnProducer marriageRegnProducer;

	@Autowired
	private RegistrationUnitRepository registrationUnitRepository;

	public List<RegistrationUnit> createAsync(RegnUnitRequest regnUnitRequest) {
		LOGGER.info("RegnUnitRequest createAsync::" + regnUnitRequest);

		RegistrationUnit registrationUnit = regnUnitRequest.getRegnUnit();
		registrationUnit.setId(registrationUnitRepository.getIdNextVal());

		ObjectMapper objectMapper = new ObjectMapper();
		String value = null;

		try {
			value = objectMapper.writeValueAsString(regnUnitRequest);
		} catch (JsonProcessingException e) {
			LOGGER.info("JsonProcessingException RegnUnitRequest for kafka : " + e);
		}
		marriageRegnProducer.sendMessage(propertiesManager.getCreateRegistrationUnitTopicName(),
				propertiesManager.getRegistrationUnitKey(), value);

		List<RegistrationUnit> registrationUnitList = new ArrayList<>();
		registrationUnitList.add(registrationUnit);
		// List back to UI from controller
		return registrationUnitList;
	}

	public List<RegistrationUnit> search(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		return registrationUnitRepository.search(regnUnitSearchCriteria);
	}

	public List<RegistrationUnit> updateAsync(RegnUnitRequest regnUnitRequest) {
		RegistrationUnit registrationUnit = regnUnitRequest.getRegnUnit();
		// Check whether Record exists in DB
		System.out.println(registrationUnit.getId());
		System.out.println(registrationUnit.getTenantId());
		if (registrationUnitRepository.checkIdsAndTenantIdsFromDB(registrationUnit)) {
			ObjectMapper objectMapper = new ObjectMapper();
			String value = null;
			try {
				value = objectMapper.writeValueAsString(regnUnitRequest);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			try {
				marriageRegnProducer.sendMessage(propertiesManager.getUpdateRegistrationUnitTopicName(),
						propertiesManager.getRegistrationUnitKey(), value);
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<RegistrationUnit> registrationUnitList = new ArrayList<>();
			registrationUnitList.add(registrationUnit);

			// List back to UI from controller
			return registrationUnitList;
		}
		return new ArrayList<>();
	}

	// ******** Called from Kafka and persisted to DB *********
	public void create(RegnUnitRequest regnUnitRequest) {
		LOGGER.info("create regnUnitRequest from consumer : " + regnUnitRequest);
		registrationUnitRepository.create(regnUnitRequest.getRegnUnit());
	}

	public void update(RegnUnitRequest regnUnitRequest) {
		LOGGER.info("update regnUnitRequest from consumer : " + regnUnitRequest);
		registrationUnitRepository.update(regnUnitRequest.getRegnUnit());
	}
}
