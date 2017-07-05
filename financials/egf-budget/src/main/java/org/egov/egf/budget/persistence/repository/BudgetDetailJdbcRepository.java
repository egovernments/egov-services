package org.egov.egf.budget.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.entity.BudgetDetailSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BudgetDetailJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BudgetDetailJdbcRepository.class);

	static {
		LOG.debug("init budgetDetail");
		init(BudgetDetailEntity.class);
		LOG.debug("end init budgetDetail");
	}

	public BudgetDetailEntity create(BudgetDetailEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BudgetDetailEntity update(BudgetDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<BudgetDetail> search(BudgetDetailSearch domain) {
		BudgetDetailSearchEntity budgetDetailSearchEntity = new BudgetDetailSearchEntity();
		budgetDetailSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", BudgetDetailEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (budgetDetailSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", budgetDetailSearchEntity.getId());
		}
		if (budgetDetailSearchEntity.getBudgetGroupId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetGroup =:budgetGroup");
			paramValues.put("budgetGroup", budgetDetailSearchEntity.getBudgetGroupId());
		}
		if (budgetDetailSearchEntity.getBudgetId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budget =:budget");
			paramValues.put("budget", budgetDetailSearchEntity.getBudgetId());
		}
		if (budgetDetailSearchEntity.getOriginalAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("originalAmount =:originalAmount");
			paramValues.put("originalAmount", budgetDetailSearchEntity.getOriginalAmount());
		}
		if (budgetDetailSearchEntity.getApprovedAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("approvedAmount =:approvedAmount");
			paramValues.put("approvedAmount", budgetDetailSearchEntity.getApprovedAmount());
		}
		if (budgetDetailSearchEntity.getBudgetAvailable() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetAvailable =:budgetAvailable");
			paramValues.put("budgetAvailable", budgetDetailSearchEntity.getBudgetAvailable());
		}
		if (budgetDetailSearchEntity.getAnticipatoryAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("anticipatoryAmount =:anticipatoryAmount");
			paramValues.put("anticipatoryAmount", budgetDetailSearchEntity.getAnticipatoryAmount());
		}
		if (budgetDetailSearchEntity.getUsingDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("usingDepartment =:usingDepartment");
			paramValues.put("usingDepartment", budgetDetailSearchEntity.getUsingDepartmentId());
		}
		if (budgetDetailSearchEntity.getExecutingDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("executingDepartment =:executingDepartment");
			paramValues.put("executingDepartment", budgetDetailSearchEntity.getExecutingDepartmentId());
		}
		if (budgetDetailSearchEntity.getFunctionId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("function =:function");
			paramValues.put("function", budgetDetailSearchEntity.getFunctionId());
		}
		if (budgetDetailSearchEntity.getSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("scheme =:scheme");
			paramValues.put("scheme", budgetDetailSearchEntity.getSchemeId());
		}
		if (budgetDetailSearchEntity.getFundId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fund =:fund");
			paramValues.put("fund", budgetDetailSearchEntity.getFundId());
		}
		if (budgetDetailSearchEntity.getSubSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("subScheme =:subScheme");
			paramValues.put("subScheme", budgetDetailSearchEntity.getSubSchemeId());
		}
		if (budgetDetailSearchEntity.getFunctionaryId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("functionary =:functionary");
			paramValues.put("functionary", budgetDetailSearchEntity.getFunctionaryId());
		}
		if (budgetDetailSearchEntity.getBoundaryId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("boundary =:boundary");
			paramValues.put("boundary", budgetDetailSearchEntity.getBoundaryId());
		}
		if (budgetDetailSearchEntity.getMaterializedPath() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materializedPath =:materializedPath");
			paramValues.put("materializedPath", budgetDetailSearchEntity.getMaterializedPath());
		}
		 
		if (budgetDetailSearchEntity.getDocumentNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("documentNumber =:documentNumber");
			paramValues.put("documentNumber", budgetDetailSearchEntity.getDocumentNumber());
		}
		if (budgetDetailSearchEntity.getUniqueNo() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("uniqueNo =:uniqueNo");
			paramValues.put("uniqueNo", budgetDetailSearchEntity.getUniqueNo());
		}
		if (budgetDetailSearchEntity.getPlanningPercent() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("planningPercent =:planningPercent");
			paramValues.put("planningPercent", budgetDetailSearchEntity.getPlanningPercent());
		}
		if (budgetDetailSearchEntity.getStatusId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("status =:status");
			paramValues.put("status", budgetDetailSearchEntity.getStatusId());
		}

		Pagination<BudgetDetail> page = new Pagination<>();
		page.setOffSet(budgetDetailSearchEntity.getOffset());
		page.setPageSize(budgetDetailSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page, paramValues); 
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + budgetDetailSearchEntity.getPageSize() + " offset "
				+ budgetDetailSearchEntity.getOffset() * budgetDetailSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetDetailEntity.class);

		List<BudgetDetailEntity> budgetDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(budgetDetailEntities.size());

		List<BudgetDetail> budgetdetails = new ArrayList<BudgetDetail>();
		for (BudgetDetailEntity budgetDetailEntity : budgetDetailEntities) {

			budgetdetails.add(budgetDetailEntity.toDomain());
		}
		page.setPagedData(budgetdetails);

		return page;
	}

	public BudgetDetailEntity findById(BudgetDetailEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<BudgetDetailEntity> budgetdetails = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<BudgetDetailEntity>());
		if (budgetdetails.isEmpty()) {
			return null;
		} else {
			return budgetdetails.get(0);
		}

	}

}