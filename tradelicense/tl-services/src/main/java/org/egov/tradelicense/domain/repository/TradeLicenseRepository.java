package org.egov.tradelicense.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeLicenseRepository {

	@Autowired
	TradeLicenseQueueRepository tradeLicenseQueueRepository;

	@Autowired
	TradeLicenseJdbcRepository tradeLicenseJdbcRepository;

	@Autowired
	PropertiesManager propertiesManager;

	public Long getNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(TradeLicenseEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public void add(TradeLicenseRequest request) {

		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getCreateLegacyTradeValidated(), request);
		tradeLicenseQueueRepository.add(message);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {
		TradeLicenseEntity entity = tradeLicenseJdbcRepository.create(new TradeLicenseEntity().toEntity(tradeLicense));
		return entity.toDomain();
	}

}