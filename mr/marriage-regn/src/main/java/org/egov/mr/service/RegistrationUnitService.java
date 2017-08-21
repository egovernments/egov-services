package org.egov.mr.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.RegistrationUnitRepository;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationUnitService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private RegistrationUnitRepository registrationUnitRepository;

	/**
	 * @CREATE
	 * 
	 * @param regnUnitRequest
	 * @return
	 */
	public List<RegistrationUnit> createAsync(RegnUnitRequest regnUnitRequest) {
		log.info("RegnUnitRequest createAsync::" + regnUnitRequest);

		RegistrationUnit registrationUnit = regnUnitRequest.getRegnUnit();
		registrationUnit.setId(registrationUnitRepository.getIdNextVal());

		kafkaTemplate.send(propertiesManager.getCreateRegistrationUnitTopicName(), regnUnitRequest);

		List<RegistrationUnit> registrationUnitList = new ArrayList<RegistrationUnit>();
		registrationUnitList.add(registrationUnit);
		return registrationUnitList;
	}

	/**
	 * @SEARCH
	 * 
	 * @param regnUnitSearchCriteria
	 * @return
	 */
	public List<RegistrationUnit> search(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		return registrationUnitRepository.search(regnUnitSearchCriteria);
	}

	/**
	 * @UPDATE
	 * 
	 * @param regnUnitRequest
	 * @return
	 */
	public List<RegistrationUnit> updateAsync(RegnUnitRequest regnUnitRequest) {

		RegistrationUnit registrationUnit = regnUnitRequest.getRegnUnit();

		/**
		 * @Check whether Record exists in DB
		 */
		if (registrationUnitRepository.checkIdsAndTenantIdsFromDB(registrationUnit)) {
			kafkaTemplate.send(propertiesManager.getUpdateRegistrationUnitTopicName(), regnUnitRequest);

			List<RegistrationUnit> registrationUnitList = new ArrayList<RegistrationUnit>();
			registrationUnitList.add(registrationUnit);
			return registrationUnitList;
		}
		return new ArrayList<RegistrationUnit>();
	}

	/**
	 * @Kafka persisted to DB
	 * @param regnUnitRequest
	 */
	public void create(RegnUnitRequest regnUnitRequest) {
		log.info("create regnUnitRequest from consumer : " + regnUnitRequest);
		registrationUnitRepository.create(regnUnitRequest.getRegnUnit());
	}

	public void update(RegnUnitRequest regnUnitRequest) {
		log.info("update regnUnitRequest from consumer : " + regnUnitRequest);
		registrationUnitRepository.update(regnUnitRequest.getRegnUnit());
	}
}
