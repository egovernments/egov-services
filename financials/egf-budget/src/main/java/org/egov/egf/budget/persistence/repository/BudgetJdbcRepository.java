package org.egov.egf.budget.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.entity.BudgetSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BudgetJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BudgetJdbcRepository.class);

	static {
		LOG.debug("init budget");
		init(BudgetEntity.class);
		LOG.debug("end init budget");
	}

	public BudgetEntity create(BudgetEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BudgetEntity update(BudgetEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Budget> search(BudgetSearch domain) {
		BudgetSearchEntity budgetSearchEntity = new BudgetSearchEntity();
		budgetSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "order by id";
		if (budgetSearchEntity.getSortBy() != null && !budgetSearchEntity.getSortBy().isEmpty())
			orderBy = "order by " + budgetSearchEntity.getSortBy();

		searchQuery = searchQuery.replace(":tablename", BudgetEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( budgetSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =:id");
paramValues.put("id" ,budgetSearchEntity.getId());} 
if( budgetSearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =:name");
paramValues.put("name" ,budgetSearchEntity.getName());} 
if( budgetSearchEntity.getFinancialYearId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "financialYear =:financialYear");
paramValues.put("financialYear" ,budgetSearchEntity.getFinancialYearId());} 
if( budgetSearchEntity.getEstimationTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "estimationType =:estimationType");
paramValues.put("estimationType" ,budgetSearchEntity.getEstimationTypeId());} 
if( budgetSearchEntity.getParentId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "parent =:parent");
paramValues.put("parent" ,budgetSearchEntity.getParentId());} 
if( budgetSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =:active");
paramValues.put("active" ,budgetSearchEntity.getActive());} 
if( budgetSearchEntity.getPrimaryBudget()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "primaryBudget =:primaryBudget");
paramValues.put("primaryBudget" ,budgetSearchEntity.getPrimaryBudget());} 
if( budgetSearchEntity.getReferenceBudgetId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "referenceBudget =:referenceBudget");
paramValues.put("referenceBudget" ,budgetSearchEntity.getReferenceBudgetId());} 
if( budgetSearchEntity.getStatusId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "status =:status");
paramValues.put("status" ,budgetSearchEntity.getStatusId());} 
if( budgetSearchEntity.getDocumentNumber()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "documentNumber =:documentNumber");
paramValues.put("documentNumber" ,budgetSearchEntity.getDocumentNumber());} 
if( budgetSearchEntity.getDescription()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "description =:description");
paramValues.put("description" ,budgetSearchEntity.getDescription());} 
if( budgetSearchEntity.getMaterializedPath()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "materializedPath =:materializedPath");
paramValues.put("materializedPath" ,budgetSearchEntity.getMaterializedPath());} 


		Pagination<Budget> page = new Pagination<>();
		if (budgetSearchEntity.getOffset() != null)
			page.setOffset(budgetSearchEntity.getOffset());
		if (budgetSearchEntity.getPageSize() != null)
			page.setPageSize(budgetSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<Budget>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetEntity.class);

		List<BudgetEntity> budgetEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(budgetEntities.size());

		List<Budget> budgets = new ArrayList<Budget>();
		for (BudgetEntity budgetEntity : budgetEntities) {

			budgets.add(budgetEntity.toDomain());
		}
		page.setPagedData(budgets);

		return page;
	}

	public BudgetEntity findById(BudgetEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<BudgetEntity> budgets = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(BudgetEntity.class));
		if (budgets.isEmpty()) {
			return null;
		} else {
			return budgets.get(0);
		}

	}

}