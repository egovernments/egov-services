package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.persistence.entity.PurchaseIndentDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PurchaseIndentDetailJdbcRepository extends org.egov.common.JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PurchaseIndentDetailJdbcRepository.class);

	static {
		LOG.debug("init purchase order");
		init(PurchaseIndentDetailEntity.class);
		LOG.debug("end init purchase order");
	}

	public PurchaseIndentDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public PurchaseIndentDetailEntity create(PurchaseIndentDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	public PurchaseIndentDetailEntity update(PurchaseIndentDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(PurchaseIndentDetailEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	public PurchaseIndentDetailEntity findById(PurchaseIndentDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<PurchaseIndentDetailEntity> poIndentDetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(PurchaseIndentDetailEntity.class));
		if (poIndentDetails.isEmpty()) {
			return null;
		} else {
			return poIndentDetails.get(0);
		}

	}

}