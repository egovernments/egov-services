package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;
import org.egov.egf.master.persistence.entity.FunctionaryEntity;
import org.egov.egf.master.persistence.entity.FunctionarySearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class FunctionaryJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(FunctionaryJdbcRepository.class);

	static {
		LOG.debug("init functionary");
		init(FunctionaryEntity.class);
		LOG.debug("end init functionary");
	}

	public FunctionaryEntity create(FunctionaryEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public FunctionaryEntity update(FunctionaryEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Functionary> search(FunctionarySearch domain) {
		FunctionarySearchEntity functionarySearchEntity = new FunctionarySearchEntity();
		functionarySearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", FunctionaryEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( functionarySearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,functionarySearchEntity.getId());} 
if( functionarySearchEntity.getCode()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "code =: code");
paramValues.put("code" ,functionarySearchEntity.getCode());} 
if( functionarySearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,functionarySearchEntity.getName());} 
if( functionarySearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,functionarySearchEntity.getActive());} 

		 

		Pagination<Functionary> page = new Pagination<>();
		page.setOffSet(functionarySearchEntity.getOffset());
		page.setPageSize(functionarySearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page,paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + functionarySearchEntity.getPageSize() + " offset "
				+ functionarySearchEntity.getOffset() * functionarySearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FunctionaryEntity.class);

		List<FunctionaryEntity> functionaryEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(functionaryEntities.size());

		List<Functionary> functionaries = new ArrayList<Functionary>();
		for (FunctionaryEntity functionaryEntity : functionaryEntities) {

			functionaries.add(functionaryEntity.toDomain());
		}
		page.setPagedData(functionaries);

		return page;
	}

	public FunctionaryEntity findById(FunctionaryEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<FunctionaryEntity> functionaries = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<FunctionaryEntity>());
		if (functionaries.isEmpty()) {
			return null;
		} else {
			return functionaries.get(0);
		}

	}

}