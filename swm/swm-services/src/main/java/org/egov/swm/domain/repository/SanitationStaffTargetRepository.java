package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.persistence.repository.SanitationStaffTargetJdbcRepository;
import org.egov.swm.persistence.repository.SanitationStaffTargetMapJdbcRepository;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffTargetRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private SanitationStaffTargetJdbcRepository sanitationStaffTargetJdbcRepository;

	@Autowired
	private SanitationStaffTargetMapJdbcRepository sanitationStaffTargetMapJdbcRepository;

	@Value("${egov.swm.sanitationstafftarget.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.sanitationstafftarget.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.sanitationstafftarget.indexer.topic}")
	private String indexerTopic;

	public SanitationStaffTargetRequest save(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		kafkaTemplate.send(saveTopic, sanitationStaffTargetRequest);

		kafkaTemplate.send(indexerTopic, sanitationStaffTargetRequest.getSanitationStaffTargets());

		return sanitationStaffTargetRequest;

	}

	public SanitationStaffTargetRequest update(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		for (SanitationStaffTarget sst : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

			sanitationStaffTargetMapJdbcRepository.delete(sst.getTenantId(), sst.getTargetNo());

		}

		kafkaTemplate.send(updateTopic, sanitationStaffTargetRequest);

		kafkaTemplate.send(indexerTopic, sanitationStaffTargetRequest.getSanitationStaffTargets());

		return sanitationStaffTargetRequest;

	}

	public Pagination<SanitationStaffTarget> search(SanitationStaffTargetSearch sanitationStaffTargetSearch) {
		return sanitationStaffTargetJdbcRepository.search(sanitationStaffTargetSearch);

	}

}