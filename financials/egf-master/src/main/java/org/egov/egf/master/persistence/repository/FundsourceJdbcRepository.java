package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.FundsourceSearch;
import org.egov.egf.master.persistence.entity.FundsourceEntity;
import org.egov.egf.master.persistence.entity.FundsourceSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class FundsourceJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(FundsourceJdbcRepository.class);

	static {
		LOG.debug("init fundsource");
		init(FundsourceEntity.class);
		LOG.debug("end init fundsource");
	}

	public FundsourceEntity create(FundsourceEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public FundsourceEntity update(FundsourceEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Fundsource> search(FundsourceSearch domain) {
		FundsourceSearchEntity fundsourceSearchEntity = new FundsourceSearchEntity();
		fundsourceSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", FundsourceEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( fundsourceSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,fundsourceSearchEntity.getId());} 
if( fundsourceSearchEntity.getCode()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "code =: code");
paramValues.put("code" ,fundsourceSearchEntity.getCode());} 
if( fundsourceSearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,fundsourceSearchEntity.getName());} 
if( fundsourceSearchEntity.getType()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "type =: type");
paramValues.put("type" ,fundsourceSearchEntity.getType());} 
if( fundsourceSearchEntity.getFundSourceId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "fundSource =: fundSource");
paramValues.put("fundSource" ,fundsourceSearchEntity.getFundSourceId());} 
if( fundsourceSearchEntity.getLlevel()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "llevel =: llevel");
paramValues.put("llevel" ,fundsourceSearchEntity.getLlevel());} 
if( fundsourceSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,fundsourceSearchEntity.getActive());} 
if( fundsourceSearchEntity.getIsParent()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "isParent =: isParent");
paramValues.put("isParent" ,fundsourceSearchEntity.getIsParent());} 

		 

		Pagination<Fundsource> page = new Pagination<>();
		page.setOffSet(fundsourceSearchEntity.getOffset());
		page.setPageSize(fundsourceSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + fundsourceSearchEntity.getPageSize() + " offset "
				+ fundsourceSearchEntity.getOffset() * fundsourceSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FundsourceEntity.class);

		List<FundsourceEntity> fundsourceEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(fundsourceEntities.size());

		List<Fundsource> fundsources = new ArrayList<Fundsource>();
		for (FundsourceEntity fundsourceEntity : fundsourceEntities) {

			fundsources.add(fundsourceEntity.toDomain());
		}
		page.setPagedData(fundsources);

		return page;
	}

	public FundsourceEntity findById(FundsourceEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<FundsourceEntity> fundsources = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<FundsourceEntity>());
		if (fundsources.isEmpty()) {
			return null;
		} else {
			return fundsources.get(0);
		}

	}

}