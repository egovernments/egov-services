package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;
import org.egov.egf.master.persistence.entity.ChartOfAccountEntity;
import org.egov.egf.master.persistence.entity.ChartOfAccountSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountJdbcRepository.class);

	static {
		LOG.debug("init chartOfAccount");
		init(ChartOfAccountEntity.class);
		LOG.debug("end init chartOfAccount");
	}

	public ChartOfAccountEntity create(ChartOfAccountEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public ChartOfAccountEntity update(ChartOfAccountEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<ChartOfAccount> search(ChartOfAccountSearch domain) {
		ChartOfAccountSearchEntity chartOfAccountSearchEntity = new ChartOfAccountSearchEntity();
		chartOfAccountSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", ChartOfAccountEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (chartOfAccountSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", chartOfAccountSearchEntity.getId());
		}
		if (chartOfAccountSearchEntity.getGlcode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("glcode =:glcode");
			paramValues.put("glcode", chartOfAccountSearchEntity.getGlcode());
		}
		if (chartOfAccountSearchEntity.getName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("name =:name");
			paramValues.put("name", chartOfAccountSearchEntity.getName());
		}
		if (chartOfAccountSearchEntity.getAccountCodePurposeId() != null) {
			if (params.length() > 0)
				params.append(" and ");

			if (chartOfAccountSearchEntity.getGlcode() == null) {
				List<String> s = new ArrayList<>();
				String params1 = "select * from " + ChartOfAccountEntity.TABLE_NAME
						+ " where accountCodePurposeId=:accountCodePurpose";
				String params2 = "select * from " + ChartOfAccountEntity.TABLE_NAME + " where glcode like :glcodeLike";
				paramValues.put("accountCodePurpose", chartOfAccountSearchEntity.getAccountCodePurposeId());
				List<ChartOfAccountEntity> chartOfAccountEntities = namedParameterJdbcTemplate.query(params1,
						paramValues, new BeanPropertyRowMapper(ChartOfAccountEntity.class));
				for (ChartOfAccountEntity entity : chartOfAccountEntities) {
					paramValues.put("glcodeLike", entity.getGlcode() + "%");
					List<ChartOfAccountEntity> entities = namedParameterJdbcTemplate.query(params2, paramValues,
							new BeanPropertyRowMapper(ChartOfAccountEntity.class));
					for (ChartOfAccountEntity e : entities) {
						s.add(e.getGlcode());
					}

				}
				params.append("glcode in( :glcodeIn )");
				paramValues.put("glcodeIn", Arrays.asList(s.toArray()));
				paramValues.remove("accountCodePurpose");
				paramValues.remove("glcodeLike");

			} else {
				params.append("accountCodePurposeId =:accountCodePurpose");
				paramValues.put("accountCodePurpose", chartOfAccountSearchEntity.getAccountCodePurposeId());
			}
		}
		if (chartOfAccountSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", chartOfAccountSearchEntity.getDescription());
		}
		if (chartOfAccountSearchEntity.getIsActiveForPosting() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("isActiveForPosting =:isActiveForPosting");
			paramValues.put("isActiveForPosting", chartOfAccountSearchEntity.getIsActiveForPosting());
		}
		if (chartOfAccountSearchEntity.getParentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("parentId =:parentId");
			paramValues.put("parentId", chartOfAccountSearchEntity.getParentId());
		}
		if (chartOfAccountSearchEntity.getType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("type =:type");
			paramValues.put("type", chartOfAccountSearchEntity.getType());
		}
		if (chartOfAccountSearchEntity.getClassification() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("classification =:classification");
			paramValues.put("classification", chartOfAccountSearchEntity.getClassification());
		}
		if (chartOfAccountSearchEntity.getFunctionRequired() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("functionRequired =:functionRequired");
			paramValues.put("functionRequired", chartOfAccountSearchEntity.getFunctionRequired());
		}
		if (chartOfAccountSearchEntity.getBudgetCheckRequired() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetCheckRequired =:budgetCheckRequired");
			paramValues.put("budgetCheckRequired", chartOfAccountSearchEntity.getBudgetCheckRequired());
		}
		if (chartOfAccountSearchEntity.getMajorCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("majorCode =:majorCode");
			paramValues.put("majorCode", chartOfAccountSearchEntity.getMajorCode());
		}
		if (chartOfAccountSearchEntity.getIsSubLedger() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("isSubLedger =:isSubLedger");
			paramValues.put("isSubLedger", chartOfAccountSearchEntity.getIsSubLedger());
		}

		Pagination<ChartOfAccount> page = new Pagination<>();
		if (chartOfAccountSearchEntity.getOffset() != null)
			page.setOffset(chartOfAccountSearchEntity.getOffset());
		if (chartOfAccountSearchEntity.getPageSize() != null)
			page.setPageSize(chartOfAccountSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ChartOfAccountEntity.class);

		List<ChartOfAccountEntity> chartOfAccountEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(chartOfAccountEntities.size());

		List<ChartOfAccount> chartofaccounts = new ArrayList<ChartOfAccount>();
		for (ChartOfAccountEntity chartOfAccountEntity : chartOfAccountEntities) {

			chartofaccounts.add(chartOfAccountEntity.toDomain());
		}
		page.setPagedData(chartofaccounts);

		return page;
	}

	public ChartOfAccountEntity findById(ChartOfAccountEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<ChartOfAccountEntity> chartofaccounts = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(ChartOfAccountEntity.class));
		if (chartofaccounts.isEmpty()) {
			return null;
		} else {
			return chartofaccounts.get(0);
		}

	}

	public ChartOfAccountEntity findByAccountCodePurposeId(ChartOfAccountEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<ChartOfAccountEntity> chartofaccounts = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(ChartOfAccountEntity.class));
		if (chartofaccounts.isEmpty()) {
			return null;
		} else {
			return chartofaccounts.get(0);
		}

	}

}