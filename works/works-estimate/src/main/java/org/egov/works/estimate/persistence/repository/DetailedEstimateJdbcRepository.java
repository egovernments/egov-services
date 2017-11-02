package org.egov.works.estimate.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.model.DetailedEstimate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class DetailedEstimateJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_detailedestimate";

	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (detailedEstimateSearchContract.getSortBy() != null
				&& !detailedEstimateSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(detailedEstimateSearchContract.getSortBy());
			validateEntityFieldName(detailedEstimateSearchContract.getSortBy(), DetailedEstimate.class);
		}

		String orderBy = "order by id";
		if (detailedEstimateSearchContract.getSortBy() != null
				&& !detailedEstimateSearchContract.getSortBy().isEmpty()) {
			orderBy = "order by " + detailedEstimateSearchContract.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (detailedEstimateSearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", detailedEstimateSearchContract.getTenantId());
		}
		if (detailedEstimateSearchContract.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in(:ids) ");
			paramValues.put("ids", detailedEstimateSearchContract.getIds());
		}
		if (detailedEstimateSearchContract.getDetailedEstimateNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("estimateNumber in(:estimateNumbers)");
			paramValues.put("estimateNumbers", detailedEstimateSearchContract.getDetailedEstimateNumbers());
		}
		if (detailedEstimateSearchContract.getDepartment() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("department =:department");
			paramValues.put("department", detailedEstimateSearchContract.getDepartment());
		}
		if (detailedEstimateSearchContract.getTypeOfWork() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("typeofwork in(:typeofwork)");
			paramValues.put("typeofwork", detailedEstimateSearchContract.getTypeOfWork());
		}
		if (detailedEstimateSearchContract.getSubTypeOfWork() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("subtypeofwork in(:subtypeofwork)");
			paramValues.put("subtypeofwork", detailedEstimateSearchContract.getSubTypeOfWork());
		}
		if (detailedEstimateSearchContract.getStatuses() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("status in(:status)");
			paramValues.put("status", detailedEstimateSearchContract.getStatuses());
		}
		if (detailedEstimateSearchContract.getFromDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("estimatedate >=:estimatedate");
			paramValues.put("estimatedate", detailedEstimateSearchContract.getFromDate());
		}
		if (detailedEstimateSearchContract.getToDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("estimatedate <=:estimatedate");
			paramValues.put("estimatedate", detailedEstimateSearchContract.getToDate());
		}
		if (detailedEstimateSearchContract.getFromAmount() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("estimateValue >=:estimateValue");
			paramValues.put("estimateValue", detailedEstimateSearchContract.getFromAmount());
		}
		if (detailedEstimateSearchContract.getToAmount() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("estimateValue <=:estimateValue");
			paramValues.put("estimateValue", detailedEstimateSearchContract.getToAmount());
		}
		if (detailedEstimateSearchContract.getSpillOverFlag() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("spillOverFlag =:spillOverFlag");
			paramValues.put("spillOverFlag", detailedEstimateSearchContract.getSpillOverFlag());
		}
		if (detailedEstimateSearchContract.getCreatedBy() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("createdBy =:createdBy");
			paramValues.put("createdBy", detailedEstimateSearchContract.getCreatedBy());
		}
		if (detailedEstimateSearchContract.getWorkIdentificationNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append(
					"id in (select id from egw_detailedestimate where projectCode in (select id from egw_projectcode where code in (:workIdentificationNumbers)))");
			paramValues.put("workIdentificationNumbers", detailedEstimateSearchContract.getWorkIdentificationNumbers());
			paramValues.put("workIdentificationNumbers", detailedEstimateSearchContract.getWorkIdentificationNumbers());
		}

		if (detailedEstimateSearchContract.getNameOfWork() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nameOfWork =:nameOfWork");
			paramValues.put("nameOfWork", detailedEstimateSearchContract.getNameOfWork());
		}

		if (detailedEstimateSearchContract.getWards() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("ward =:ward");
			paramValues.put("ward", detailedEstimateSearchContract.getWards());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(DetailedEstimate.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}
