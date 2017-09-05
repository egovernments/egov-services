package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.domain.model.ChartOfAccountDetailSearch;
import org.egov.egf.master.persistence.entity.ChartOfAccountDetailEntity;
import org.egov.egf.master.persistence.entity.ChartOfAccountDetailSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountDetailJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountDetailJdbcRepository.class);

	static {
		LOG.debug("init chartOfAccountDetail");
		init(ChartOfAccountDetailEntity.class);
		LOG.debug("end init chartOfAccountDetail");
	}

	public ChartOfAccountDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public ChartOfAccountDetailEntity create(ChartOfAccountDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	public ChartOfAccountDetailEntity update(ChartOfAccountDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<ChartOfAccountDetail> search(ChartOfAccountDetailSearch domain) {
		ChartOfAccountDetailSearchEntity chartOfAccountDetailSearchEntity = new ChartOfAccountDetailSearchEntity();
		chartOfAccountDetailSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (chartOfAccountDetailSearchEntity.getSortBy() != null
				&& !chartOfAccountDetailSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(chartOfAccountDetailSearchEntity.getSortBy());
			validateEntityFieldName(chartOfAccountDetailSearchEntity.getSortBy(), ChartOfAccountDetailEntity.class);
		}

		String orderBy = "order by chartOfAccountId";
		if (chartOfAccountDetailSearchEntity.getSortBy() != null
				&& !chartOfAccountDetailSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + chartOfAccountDetailSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", ChartOfAccountDetailEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (chartOfAccountDetailSearchEntity.getTenantId() != null) {
                    if (params.length() > 0) {
                        params.append(" and ");
                    }
                    params.append("tenantId =:tenantId");
                    paramValues.put("tenantId", chartOfAccountDetailSearchEntity.getTenantId());
                }
		if (chartOfAccountDetailSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", chartOfAccountDetailSearchEntity.getId());
		}
	        if (chartOfAccountDetailSearchEntity.getIds() != null) {
	                  if (params.length() > 0) {
	                          params.append(" and ");
	                  }
	                  params.append("id in(:ids) ");
	                  paramValues.put("ids", new ArrayList<String>(Arrays.asList(chartOfAccountDetailSearchEntity.getIds().split(","))));
	        }
		if (chartOfAccountDetailSearchEntity.getChartOfAccountId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("chartOfAccountId =:chartOfAccount");
			paramValues.put("chartOfAccount", chartOfAccountDetailSearchEntity.getChartOfAccountId());
		}
		if (chartOfAccountDetailSearchEntity.getAccountDetailTypeId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("accountDetailTypeId =:accountDetailType");
			paramValues.put("accountDetailType", chartOfAccountDetailSearchEntity.getAccountDetailTypeId());
		}

		Pagination<ChartOfAccountDetail> page = new Pagination<>();
		if (chartOfAccountDetailSearchEntity.getOffset() != null) {
			page.setOffset(chartOfAccountDetailSearchEntity.getOffset());
		}
		if (chartOfAccountDetailSearchEntity.getPageSize() != null) {
			page.setPageSize(chartOfAccountDetailSearchEntity.getPageSize());
		}

		
		if (params.length() > 0) {
		
		searchQuery = searchQuery.replace(":condition", " where " +
		params.toString());
		
		} else 
		
		searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<ChartOfAccountDetail>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ChartOfAccountDetailEntity.class);

		List<ChartOfAccountDetailEntity> chartOfAccountDetailEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(chartOfAccountDetailEntities.size());

		List<ChartOfAccountDetail> chartofaccountdetails = new ArrayList<>();
		for (ChartOfAccountDetailEntity chartOfAccountDetailEntity : chartOfAccountDetailEntities) {

			chartofaccountdetails.add(chartOfAccountDetailEntity.toDomain());
		}
		page.setPagedData(chartofaccountdetails);

		return page;
	}

	public ChartOfAccountDetailEntity findById(ChartOfAccountDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<ChartOfAccountDetailEntity> chartofaccountdetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(ChartOfAccountDetailEntity.class));
		if (chartofaccountdetails.isEmpty()) {
			return null;
		} else {
			return chartofaccountdetails.get(0);
		}

	}

}