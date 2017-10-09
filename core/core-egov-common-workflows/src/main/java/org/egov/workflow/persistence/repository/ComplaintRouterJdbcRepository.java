package org.egov.workflow.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.domain.model.ComplaintRouterModel;
import org.egov.workflow.domain.model.ComplaintRouterSearchModel;
import org.egov.workflow.domain.model.Pagination;
import org.egov.workflow.persistence.entity.ComplaintRouter;
import org.egov.workflow.persistence.entity.ComplaintRouterSearchEntity;
import org.egov.workflow.persistence.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class ComplaintRouterJdbcRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(StateJdbcRepository.class);

	static {
		LOG.debug("init complaintRouter");
		init(ComplaintRouter.class);
		LOG.debug("end init complaintRouter");
	}

	public ComplaintRouterJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public ComplaintRouter create(ComplaintRouter entity) {
		super.create(entity);
		return entity;
	}

	public ComplaintRouter update(ComplaintRouter entity) {
		super.update(entity);
		return entity;

	}

	public ComplaintRouter findById(ComplaintRouter router) {
		List<String> list = allIdentitiferFields.get(router.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(router, s), router));
		}

		List<ComplaintRouter> routers = namedParameterJdbcTemplate.query(
				getByIdQuery.get(router.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(ComplaintRouter.class));
		if (routers.isEmpty()) {
			return null;
		} else {
			return routers.get(0);
		}

	}


	public Boolean uniqueCheck(String fieldName, ComplaintRouter router) {
		return super.uniqueCheck(fieldName, router);
	}

}
