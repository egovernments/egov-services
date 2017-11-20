package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.persistence.entity.PurchaseOrderDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderDetailJdbcRepository extends org.egov.common.JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderDetailJdbcRepository.class);

	static {
		LOG.debug("init purchase order detail");
		init(PurchaseOrderDetailEntity.class);
		LOG.debug("end init purchase order detail");
	}

	public PurchaseOrderDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public PurchaseOrderDetailEntity create(PurchaseOrderDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	public PurchaseOrderDetailEntity update(PurchaseOrderDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(PurchaseOrderDetailEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	public PurchaseOrderDetailEntity findById(PurchaseOrderDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<PurchaseOrderDetailEntity> poDetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(PurchaseOrderDetailEntity.class));
		if (poDetails.isEmpty()) {
			return null;
		} else {
			return poDetails.get(0);
		}

	}

}