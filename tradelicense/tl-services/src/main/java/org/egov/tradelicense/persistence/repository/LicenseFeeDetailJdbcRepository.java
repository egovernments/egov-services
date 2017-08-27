package org.egov.tradelicense.persistence.repository;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LicenseFeeDetailJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseFeeDetailJdbcRepository.class);
	static {
		LOG.debug("init SupportDocument");
		init(LicenseFeeDetailEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public LicenseFeeDetailEntity create(LicenseFeeDetailEntity entity) {
		
		super.create(entity);

		return entity;
	}
	
	public LicenseFeeDetailEntity update(LicenseFeeDetailEntity entity) {
		
		super.update(entity);

		return entity;
	}
}