package org.egov.egf.budget.persistence.repository;

import java.math.BigDecimal;
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
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BudgetDetailJdbcRepository extends JdbcRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(BudgetDetailJdbcRepository.class);

	static {
		LOG.debug("init budgetDetail");
		init(BudgetDetailEntity.class);
		LOG.debug("end init budgetDetail");
	}

	public BudgetDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		if (budgetDetailSearchEntity.getSortBy() != null && !budgetDetailSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(budgetDetailSearchEntity.getSortBy());
			validateEntityFieldName(budgetDetailSearchEntity.getSortBy(), BudgetEntity.class);
		}

		String orderBy = "order by id";
		if (budgetDetailSearchEntity.getSortBy() != null && !budgetDetailSearchEntity.getSortBy().isEmpty())
			orderBy = "order by " + budgetDetailSearchEntity.getSortBy();

		searchQuery = searchQuery.replace(":tablename", BudgetDetailEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (budgetDetailSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", budgetDetailSearchEntity.getId());
		}
		if (budgetDetailSearchEntity.getBudgetId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetid =:budget");
			paramValues.put("budget", budgetDetailSearchEntity.getBudgetId());
		}
		if (budgetDetailSearchEntity.getBudgetGroupId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetGroupid =:budgetGroup");
			paramValues.put("budgetGroup", budgetDetailSearchEntity.getBudgetGroupId());
		}
		if (budgetDetailSearchEntity.getUsingDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("usingDepartmentid =:usingDepartment");
			paramValues.put("usingDepartment", budgetDetailSearchEntity.getUsingDepartmentId());
		}
		if (budgetDetailSearchEntity.getExecutingDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("executingDepartmentid =:executingDepartment");
			paramValues.put("executingDepartment", budgetDetailSearchEntity.getExecutingDepartmentId());
		}
		if (budgetDetailSearchEntity.getFundId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fundid =:fund");
			paramValues.put("fund", budgetDetailSearchEntity.getFundId());
		}
		if (budgetDetailSearchEntity.getFunctionId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("functionid =:function");
			paramValues.put("function", budgetDetailSearchEntity.getFunctionId());
		}
		if (budgetDetailSearchEntity.getSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("schemeid =:scheme");
			paramValues.put("scheme", budgetDetailSearchEntity.getSchemeId());
		}
		if (budgetDetailSearchEntity.getSubSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("subSchemeid =:subScheme");
			paramValues.put("subScheme", budgetDetailSearchEntity.getSubSchemeId());
		}
		if (budgetDetailSearchEntity.getFunctionaryId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("functionaryid =:functionary");
			paramValues.put("functionary", budgetDetailSearchEntity.getFunctionaryId());
		}
		if (budgetDetailSearchEntity.getBoundaryId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("boundaryid =:boundary");
			paramValues.put("boundary", budgetDetailSearchEntity.getBoundaryId());
		}
		if (budgetDetailSearchEntity.getOriginalAmount() != null
				&& 0 != budgetDetailSearchEntity.getOriginalAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("originalAmount =:originalAmount");
			paramValues.put("originalAmount", budgetDetailSearchEntity.getOriginalAmount());
		}
		if (budgetDetailSearchEntity.getApprovedAmount() != null
				&& 0 != budgetDetailSearchEntity.getApprovedAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("approvedAmount =:approvedAmount");
			paramValues.put("approvedAmount", budgetDetailSearchEntity.getApprovedAmount());
		}
		if (budgetDetailSearchEntity.getBudgetAvailable() != null
				&& 0 != budgetDetailSearchEntity.getBudgetAvailable().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetAvailable =:budgetAvailable");
			paramValues.put("budgetAvailable", budgetDetailSearchEntity.getBudgetAvailable());
		}
		if (budgetDetailSearchEntity.getAnticipatoryAmount() != null
				&& 0 != budgetDetailSearchEntity.getAnticipatoryAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("anticipatoryAmount =:anticipatoryAmount");
			paramValues.put("anticipatoryAmount", budgetDetailSearchEntity.getAnticipatoryAmount());
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
			params.append("statusid =:status");
			paramValues.put("status", budgetDetailSearchEntity.getStatusId());
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
		if (budgetDetailSearchEntity.getMaterializedPath() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materializedPath =:materializedPath");
			paramValues.put("materializedPath", budgetDetailSearchEntity.getMaterializedPath());
		}

		Pagination<BudgetDetail> page = new Pagination<>();
		if (budgetDetailSearchEntity.getOffset() != null)
			page.setOffset(budgetDetailSearchEntity.getOffset());
		if (budgetDetailSearchEntity.getPageSize() != null)
			page.setPageSize(budgetDetailSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<BudgetDetail>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

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
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<BudgetDetailEntity> budgetdetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(BudgetDetailEntity.class));
		if (budgetdetails.isEmpty()) {
			return null;
		} else {
			return budgetdetails.get(0);
		}

	}

}