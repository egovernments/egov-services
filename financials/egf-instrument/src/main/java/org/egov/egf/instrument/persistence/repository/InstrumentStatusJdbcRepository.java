package org.egov.egf.instrument.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentStatusSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentStatusEntity;
import org.egov.egf.instrument.persistence.entity.InstrumentStatusSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InstrumentStatusJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(InstrumentStatusJdbcRepository.class);

	static {
		LOG.debug("init instrumentStatus");
		init(InstrumentStatusEntity.class);
		LOG.debug("end init instrumentStatus");
	}

	public InstrumentStatusJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public InstrumentStatusEntity create(InstrumentStatusEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public InstrumentStatusEntity update(InstrumentStatusEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<InstrumentStatus> search(InstrumentStatusSearch domain) {
		InstrumentStatusSearchEntity instrumentStatusSearchEntity = new InstrumentStatusSearchEntity();
		instrumentStatusSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (instrumentStatusSearchEntity.getSortBy() != null && !instrumentStatusSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(instrumentStatusSearchEntity.getSortBy());
			validateEntityFieldName(instrumentStatusSearchEntity.getSortBy(), InstrumentStatusEntity.class);
		}

		String orderBy = "order by id";
		if (instrumentStatusSearchEntity.getSortBy() != null && !instrumentStatusSearchEntity.getSortBy().isEmpty())
			orderBy = "order by " + instrumentStatusSearchEntity.getSortBy();

		searchQuery = searchQuery.replace(":tablename", InstrumentStatusEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (instrumentStatusSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", instrumentStatusSearchEntity.getId());
		}
		if (instrumentStatusSearchEntity.getModuleType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("moduleType =:moduleType");
			paramValues.put("moduleType", instrumentStatusSearchEntity.getModuleType());
		}
		if (instrumentStatusSearchEntity.getName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("name =:name");
			paramValues.put("name", instrumentStatusSearchEntity.getName());
		}
		if (instrumentStatusSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", instrumentStatusSearchEntity.getDescription());
		}

		Pagination<InstrumentStatus> page = new Pagination<>();
		if (instrumentStatusSearchEntity.getOffset() != null)
			page.setOffset(instrumentStatusSearchEntity.getOffset());
		if (instrumentStatusSearchEntity.getPageSize() != null)
			page.setPageSize(instrumentStatusSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<InstrumentStatus>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(InstrumentStatusEntity.class);

		List<InstrumentStatusEntity> instrumentStatusEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(instrumentStatusEntities.size());

		List<InstrumentStatus> instrumentstatuses = new ArrayList<>();
		for (InstrumentStatusEntity instrumentStatusEntity : instrumentStatusEntities) {

			instrumentstatuses.add(instrumentStatusEntity.toDomain());
		}
		page.setPagedData(instrumentstatuses);

		return page;
	}

	public InstrumentStatusEntity findById(InstrumentStatusEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<InstrumentStatusEntity> instrumentstatuses = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(InstrumentStatusEntity.class));
		if (instrumentstatuses.isEmpty()) {
			return null;
		} else {
			return instrumentstatuses.get(0);
		}

	}

}