package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.BudgetGroupSearch;
import org.egov.egf.master.persistence.entity.BudgetGroupEntity;
import org.egov.egf.master.persistence.entity.BudgetGroupSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BudgetGroupJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BudgetGroupJdbcRepository.class);

	static {
		LOG.debug("init budgetGroup");
		init(BudgetGroupEntity.class);
		LOG.debug("end init budgetGroup");
	}

	public BudgetGroupEntity create(BudgetGroupEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BudgetGroupEntity update(BudgetGroupEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<BudgetGroup> search(BudgetGroupSearch domain) {
		BudgetGroupSearchEntity budgetGroupSearchEntity = new BudgetGroupSearchEntity();
		budgetGroupSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", BudgetGroupEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( budgetGroupSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,budgetGroupSearchEntity.getId());} 
if( budgetGroupSearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,budgetGroupSearchEntity.getName());} 
if( budgetGroupSearchEntity.getDescription()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "description =: description");
paramValues.put("description" ,budgetGroupSearchEntity.getDescription());} 
if( budgetGroupSearchEntity.getMajorCodeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "majorCode =: majorCode");
paramValues.put("majorCode" ,budgetGroupSearchEntity.getMajorCodeId());} 
if( budgetGroupSearchEntity.getMaxCodeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "maxCode =: maxCode");
paramValues.put("maxCode" ,budgetGroupSearchEntity.getMaxCodeId());} 
if( budgetGroupSearchEntity.getMinCodeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "minCode =: minCode");
paramValues.put("minCode" ,budgetGroupSearchEntity.getMinCodeId());} 
if( budgetGroupSearchEntity.getAccountTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountType =: accountType");
paramValues.put("accountType" ,budgetGroupSearchEntity.getAccountTypeId());} 
if( budgetGroupSearchEntity.getBudgetingTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "budgetingType =: budgetingType");
paramValues.put("budgetingType" ,budgetGroupSearchEntity.getBudgetingTypeId());} 
if( budgetGroupSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,budgetGroupSearchEntity.getActive());} 

		 

		Pagination<BudgetGroup> page = new Pagination<>();
		page.setOffSet(budgetGroupSearchEntity.getOffset());
		page.setPageSize(budgetGroupSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page,paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + budgetGroupSearchEntity.getPageSize() + " offset "
				+ budgetGroupSearchEntity.getOffset() * budgetGroupSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetGroupEntity.class);

		List<BudgetGroupEntity> budgetGroupEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(budgetGroupEntities.size());

		List<BudgetGroup> budgetgroups = new ArrayList<BudgetGroup>();
		for (BudgetGroupEntity budgetGroupEntity : budgetGroupEntities) {

			budgetgroups.add(budgetGroupEntity.toDomain());
		}
		page.setPagedData(budgetgroups);

		return page;
	}

	public BudgetGroupEntity findById(BudgetGroupEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<BudgetGroupEntity> budgetgroups = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<BudgetGroupEntity>());
		if (budgetgroups.isEmpty()) {
			return null;
		} else {
			return budgetgroups.get(0);
		}

	}

}