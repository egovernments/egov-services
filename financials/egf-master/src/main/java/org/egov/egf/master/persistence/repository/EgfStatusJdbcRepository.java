package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.EgfStatus;
import org.egov.egf.master.domain.model.EgfStatusSearch;
import org.egov.egf.master.persistence.entity.EgfStatusEntity;
import org.egov.egf.master.persistence.entity.EgfStatusSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EgfStatusJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(EgfStatusJdbcRepository.class);

	static {
		LOG.debug("init egfStatus");
		init(EgfStatusEntity.class);
		LOG.debug("end init egfStatus");
	}

	public EgfStatusEntity create(EgfStatusEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public EgfStatusEntity update(EgfStatusEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<EgfStatus> search(EgfStatusSearch domain) {
		EgfStatusSearchEntity egfStatusSearchEntity = new EgfStatusSearchEntity();
		egfStatusSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", EgfStatusEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (egfStatusSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", egfStatusSearchEntity.getId());
		}
		if (egfStatusSearchEntity.getModuleType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("moduleType =:moduleType");
			paramValues.put("moduleType", egfStatusSearchEntity.getModuleType());
		}
		if (egfStatusSearchEntity.getCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("code =:code");
			paramValues.put("code", egfStatusSearchEntity.getCode());
		}
		if (egfStatusSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", egfStatusSearchEntity.getDescription());
		}

		Pagination<EgfStatus> page = new Pagination<>();
		page.setOffSet(egfStatusSearchEntity.getOffSet());
		page.setPageSize(egfStatusSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + egfStatusSearchEntity.getPageSize() + " offset "
				+ egfStatusSearchEntity.getOffSet() * egfStatusSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(EgfStatusEntity.class);

		List<EgfStatusEntity> egfStatusEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);

		page.setTotalResults(egfStatusEntities.size());

		List<EgfStatus> egfstatuses = new ArrayList<>();
		for (EgfStatusEntity egfStatusEntity : egfStatusEntities) {

			egfstatuses.add(egfStatusEntity.toDomain());
		}
		page.setPagedData(egfstatuses);

		return page;
	}

	public EgfStatusEntity findById(EgfStatusEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());
		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<EgfStatusEntity> egfstatuses = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(EgfStatusEntity.class));
		if (egfstatuses.isEmpty()) {
			return null;
		} else {
			return egfstatuses.get(0);
		}

	}

}