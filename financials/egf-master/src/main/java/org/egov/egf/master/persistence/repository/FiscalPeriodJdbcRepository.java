package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.FiscalPeriod;
import org.egov.egf.master.domain.model.FiscalPeriodSearch;
import org.egov.egf.master.persistence.entity.FiscalPeriodEntity;
import org.egov.egf.master.persistence.entity.FiscalPeriodSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class FiscalPeriodJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(FiscalPeriodJdbcRepository.class);

	static {
		LOG.debug("init fiscalPeriod");
		init(FiscalPeriodEntity.class);
		LOG.debug("end init fiscalPeriod");
	}

	public FiscalPeriodEntity create(FiscalPeriodEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public FiscalPeriodEntity update(FiscalPeriodEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<FiscalPeriod> search(FiscalPeriodSearch domain) {
		FiscalPeriodSearchEntity fiscalPeriodSearchEntity = new FiscalPeriodSearchEntity();
		fiscalPeriodSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", FiscalPeriodEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( fiscalPeriodSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,fiscalPeriodSearchEntity.getId());} 
if( fiscalPeriodSearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,fiscalPeriodSearchEntity.getName());} 
if( fiscalPeriodSearchEntity.getFinancialYearId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "financialYear =: financialYear");
paramValues.put("financialYear" ,fiscalPeriodSearchEntity.getFinancialYearId());} 
if( fiscalPeriodSearchEntity.getStartingDate()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "startingDate =: startingDate");
paramValues.put("startingDate" ,fiscalPeriodSearchEntity.getStartingDate());} 
if( fiscalPeriodSearchEntity.getEndingDate()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "endingDate =: endingDate");
paramValues.put("endingDate" ,fiscalPeriodSearchEntity.getEndingDate());} 
if( fiscalPeriodSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,fiscalPeriodSearchEntity.getActive());} 
if( fiscalPeriodSearchEntity.getIsActiveForPosting()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "isActiveForPosting =: isActiveForPosting");
paramValues.put("isActiveForPosting" ,fiscalPeriodSearchEntity.getIsActiveForPosting());} 
if( fiscalPeriodSearchEntity.getIsClosed()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "isClosed =: isClosed");
paramValues.put("isClosed" ,fiscalPeriodSearchEntity.getIsClosed());} 

		 

		Pagination<FiscalPeriod> page = new Pagination<>();
		page.setOffSet(fiscalPeriodSearchEntity.getOffset());
		page.setPageSize(fiscalPeriodSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page,paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + fiscalPeriodSearchEntity.getPageSize() + " offset "
				+ fiscalPeriodSearchEntity.getOffset() * fiscalPeriodSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FiscalPeriodEntity.class);

		List<FiscalPeriodEntity> fiscalPeriodEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(fiscalPeriodEntities.size());

		List<FiscalPeriod> fiscalperiods = new ArrayList<FiscalPeriod>();
		for (FiscalPeriodEntity fiscalPeriodEntity : fiscalPeriodEntities) {

			fiscalperiods.add(fiscalPeriodEntity.toDomain());
		}
		page.setPagedData(fiscalperiods);

		return page;
	}

	public FiscalPeriodEntity findById(FiscalPeriodEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<FiscalPeriodEntity> fiscalperiods = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<FiscalPeriodEntity>());
		if (fiscalperiods.isEmpty()) {
			return null;
		} else {
			return fiscalperiods.get(0);
		}

	}

}