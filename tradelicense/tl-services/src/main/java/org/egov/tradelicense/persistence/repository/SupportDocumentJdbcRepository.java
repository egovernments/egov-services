package org.egov.tradelicense.persistence.repository;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SupportDocumentJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(SupportDocumentJdbcRepository.class);
	static {
		LOG.debug("init SupportDocument");
		init(SupportDocumentEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public SupportDocumentEntity create(SupportDocumentEntity entity) {
		
		super.create(entity);

		return entity;
	}
	
	public SupportDocumentEntity update(SupportDocumentEntity entity) {
		
		super.update(entity);

		return entity;
	}
}