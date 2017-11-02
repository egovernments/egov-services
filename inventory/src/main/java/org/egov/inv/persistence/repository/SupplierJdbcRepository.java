package org.egov.inv.persistence.repository;

import org.egov.inv.persistence.entity.SupplierEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SupplierJdbcRepository extends JdbcRepository {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(SupplierJdbcRepository.class);

    static {
		LOG.debug("init supplier");
		init(SupplierEntity.class);
		LOG.debug("end init supplier");
	}

}
