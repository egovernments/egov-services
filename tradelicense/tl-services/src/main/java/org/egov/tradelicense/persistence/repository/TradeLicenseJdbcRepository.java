package org.egov.tradelicense.persistence.repository;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseJdbcRepository extends JdbcRepository {

	public TradeLicenseEntity create(TradeLicenseEntity entity) {
		super.create(entity);
		return entity;
	}
}