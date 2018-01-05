package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.inv.persistence.entity.DisposalDetailEntity;
import org.egov.inv.persistence.entity.MaterialIssueDetailEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class DisposalDetailJdbcRepository extends JdbcRepository {
	
	public DisposalDetailEntity findById(Object entity, String entityName) {
		List<String> list = allIdentitiferFields.get(entityName);

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<DisposalDetailEntity> disposals = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(DisposalDetailEntity.class));
		if (disposals.isEmpty()) {
			return null;
		} else {
			return disposals.get(0);
		}

	}
}
