package org.egov.egf.budget.persistence.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BudgetReAppropriationJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(BudgetReAppropriationJdbcRepository.class);

	static {
		LOG.debug("init budgetReAppropriation");
		init(BudgetReAppropriationEntity.class);
		LOG.debug("end init budgetReAppropriation");
	}

	public BudgetReAppropriationJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public BudgetReAppropriationEntity create(BudgetReAppropriationEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BudgetReAppropriationEntity update(BudgetReAppropriationEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearch domain) {
		BudgetReAppropriationSearchEntity budgetReAppropriationSearchEntity = new BudgetReAppropriationSearchEntity();
		budgetReAppropriationSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (budgetReAppropriationSearchEntity.getSortBy() != null
				&& !budgetReAppropriationSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(budgetReAppropriationSearchEntity.getSortBy());
			validateEntityFieldName(budgetReAppropriationSearchEntity.getSortBy(), BudgetEntity.class);
		}

		String orderBy = "order by id";
		if (budgetReAppropriationSearchEntity.getSortBy() != null
				&& !budgetReAppropriationSearchEntity.getSortBy().isEmpty())
			orderBy = "order by " + budgetReAppropriationSearchEntity.getSortBy();

		searchQuery = searchQuery.replace(":tablename", BudgetReAppropriationEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (budgetReAppropriationSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", budgetReAppropriationSearchEntity.getId());
		}
		if (budgetReAppropriationSearchEntity.getBudgetDetailId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetDetailid =:budgetDetail");
			paramValues.put("budgetDetail", budgetReAppropriationSearchEntity.getBudgetDetailId());
		}
		if (budgetReAppropriationSearchEntity.getAdditionAmount() != null
				&& 0 != budgetReAppropriationSearchEntity.getAdditionAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("additionAmount =:additionAmount");
			paramValues.put("additionAmount", budgetReAppropriationSearchEntity.getAdditionAmount());
		}
		if (budgetReAppropriationSearchEntity.getDeductionAmount() != null
				&& 0 != budgetReAppropriationSearchEntity.getDeductionAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("deductionAmount =:deductionAmount");
			paramValues.put("deductionAmount", budgetReAppropriationSearchEntity.getDeductionAmount());
		}
		if (budgetReAppropriationSearchEntity.getOriginalAdditionAmount() != null
				&& 0 != budgetReAppropriationSearchEntity.getOriginalAdditionAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("originalAdditionAmount =:originalAdditionAmount");
			paramValues.put("originalAdditionAmount", budgetReAppropriationSearchEntity.getOriginalAdditionAmount());
		}
		if (budgetReAppropriationSearchEntity.getOriginalDeductionAmount() != null
				&& 0 != budgetReAppropriationSearchEntity.getOriginalDeductionAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("originalDeductionAmount =:originalDeductionAmount");
			paramValues.put("originalDeductionAmount", budgetReAppropriationSearchEntity.getOriginalDeductionAmount());
		}
		if (budgetReAppropriationSearchEntity.getAnticipatoryAmount() != null
				&& 0 != budgetReAppropriationSearchEntity.getAnticipatoryAmount().compareTo(BigDecimal.ZERO)) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("anticipatoryAmount =:anticipatoryAmount");
			paramValues.put("anticipatoryAmount", budgetReAppropriationSearchEntity.getAnticipatoryAmount());
		}
		if (budgetReAppropriationSearchEntity.getStatusId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("statusid =:status");
			paramValues.put("status", budgetReAppropriationSearchEntity.getStatusId());
		}
		if (budgetReAppropriationSearchEntity.getAsOnDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("asOnDate =:asOnDate");
			paramValues.put("asOnDate", budgetReAppropriationSearchEntity.getAsOnDate());
		}

		Pagination<BudgetReAppropriation> page = new Pagination<>();
		if (budgetReAppropriationSearchEntity.getOffset() != null)
			page.setOffset(budgetReAppropriationSearchEntity.getOffset());
		if (budgetReAppropriationSearchEntity.getPageSize() != null)
			page.setPageSize(budgetReAppropriationSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<BudgetReAppropriation>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetReAppropriationEntity.class);

		List<BudgetReAppropriationEntity> budgetReAppropriationEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(budgetReAppropriationEntities.size());

		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<BudgetReAppropriation>();
		for (BudgetReAppropriationEntity budgetReAppropriationEntity : budgetReAppropriationEntities) {

			budgetreappropriations.add(budgetReAppropriationEntity.toDomain());
		}
		page.setPagedData(budgetreappropriations);

		return page;
	}

	public BudgetReAppropriationEntity findById(BudgetReAppropriationEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<BudgetReAppropriationEntity> budgetreappropriations = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(BudgetReAppropriationEntity.class));
		if (budgetreappropriations.isEmpty()) {
			return null;
		} else {
			return budgetreappropriations.get(0);
		}

	}

}