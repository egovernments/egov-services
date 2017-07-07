package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.persistence.entity.FundEntity;
import org.egov.egf.master.persistence.entity.FundSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class FundJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(FundJdbcRepository.class);

	static {
		LOG.debug("init fund");
		init(FundEntity.class);
		LOG.debug("end init fund");
	}

	public static synchronized void init(Class T) {
		String TABLE_NAME = "";

		List<String> insertFields = new ArrayList<>();
		List<String> updateFields = new ArrayList<>();
		List<String> uniqueFields = new ArrayList<>();

		String updateQuery;

		try {

			TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
		} catch (Exception e) {

		}
		insertFields.addAll(fetchFields(T));
		uniqueFields.add("name");
		uniqueFields.add("tenantId");
		insertFields.removeAll(uniqueFields);
		allInsertQuery.put(T.getSimpleName(), insertQuery(insertFields, TABLE_NAME, uniqueFields));
		updateFields.addAll(insertFields);
		updateFields.remove("createdBy");
		updateQuery = updateQuery(updateFields, TABLE_NAME, uniqueFields);
		LOG.debug(T.getSimpleName() + "--------" + insertFields);
		allInsertFields.put(T.getSimpleName(), insertFields);
		allUpdateFields.put(T.getSimpleName(), updateFields);
		allUniqueFields.put(T.getSimpleName(), uniqueFields);
		allUpdateQuery.put(T.getSimpleName(), updateQuery);
		getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
		LOG.debug("allInsertQuery : " + allInsertQuery);
	}

	public FundEntity create(FundEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public FundEntity update(FundEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Fund> search(FundSearch domain) {
		FundSearchEntity fundSearchEntity = new FundSearchEntity();
		fundSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "order by id";
		if(fundSearchEntity.getSortBy() != null && !fundSearchEntity.getSortBy().isEmpty())
		    orderBy = "order by " + fundSearchEntity.getSortBy();
		    

		searchQuery = searchQuery.replace(":tablename", FundEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search

		Pagination<Fund> page = new Pagination<>();
		page.setOffSet(fundSearchEntity.getOffset());
		page.setPageSize(fundSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + fundSearchEntity.getPageSize() + " offset "
				+ fundSearchEntity.getOffset() * fundSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FundEntity.class);

		List<FundEntity> fundEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(fundEntities.size());

		List<Fund> funds = new ArrayList<Fund>();
		for (FundEntity fundEntity : fundEntities) {

			funds.add(fundEntity.toDomain());
		}
		page.setPagedData(funds);

		return page;
	}

	public FundEntity findById(FundEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<FundEntity> funds = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(FundEntity.class));
		if (funds.isEmpty()) {
			return null;
		} else {
			return funds.get(0);
		}

	}

}