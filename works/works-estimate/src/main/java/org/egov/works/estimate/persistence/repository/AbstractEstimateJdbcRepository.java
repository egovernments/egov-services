package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.AbstractEstimateHelper;
import org.egov.works.estimate.web.contract.AbstractEstimate;
import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetailSearchContract;
import org.egov.works.estimate.web.contract.AbstractEstimateDetailsSearchContract;
import org.egov.works.estimate.web.contract.AbstractEstimateSanctionSearchContract;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class AbstractEstimateJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_abstractestimate";
	
	@Autowired
	private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;
	
	@Autowired
	private AbstractEstimateSanctionDetailJdbcRepository abstractEstimateSanctionDetailJdbcRepository;
	
	@Autowired
	private AbstractEstimateAssetDetailJdbcRepository abstractEstimateAssetDetailJdbcRepository;
	
	public List<AbstractEstimate> search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (abstractEstimateSearchContract.getSortBy() != null && !abstractEstimateSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(abstractEstimateSearchContract.getSortBy());
			validateEntityFieldName(abstractEstimateSearchContract.getSortBy(), AbstractEstimate.class);
		}

		String orderBy = "order by id";
		if (abstractEstimateSearchContract.getSortBy() != null && !abstractEstimateSearchContract.getSortBy().isEmpty()) {
			orderBy = "order by " + abstractEstimateSearchContract.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (abstractEstimateSearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", abstractEstimateSearchContract.getTenantId());
		}
		if (abstractEstimateSearchContract.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in(:ids) ");
			paramValues.put("ids", abstractEstimateSearchContract.getIds());
		}
		if (abstractEstimateSearchContract.getAdminSanctionNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("adminSanctionNumber in (:adminSanctionNumbers)");
			paramValues.put("adminSanctionNumbers", abstractEstimateSearchContract.getAdminSanctionNumbers());
		}
		if (abstractEstimateSearchContract.getDepartmentCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("department in (:departments)");
			paramValues.put("departments", abstractEstimateSearchContract.getDepartmentCodes());
		}
		if (abstractEstimateSearchContract.getFundCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("fund in (:funds)");
			paramValues.put("funds", abstractEstimateSearchContract.getFundCodes());
		}
		if (abstractEstimateSearchContract.getFunctionCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("function in (:functions)");
			paramValues.put("functions", abstractEstimateSearchContract.getFunctionCodes());
		}
		if (abstractEstimateSearchContract.getBudgetHeadCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("budgetHead in (:budgetHeads)");
			paramValues.put("budgetHeads", abstractEstimateSearchContract.getBudgetHeadCodes());
		}
		if (abstractEstimateSearchContract.getStatuses() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("status in (:statuses)");
			paramValues.put("statuses", abstractEstimateSearchContract.getStatuses());
		}
		if (abstractEstimateSearchContract.getNameOfWork() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in (select abstractestimate from egw_abstractestimate_details where nameofwork = :nameOfWork)");
			paramValues.put("nameOfWork", abstractEstimateSearchContract.getNameOfWork());
		}
		if (abstractEstimateSearchContract.getAdminSanctionFromDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("adminSanctionDate >=:adminSanctionFromDate");
			paramValues.put("adminSanctionFromDate", abstractEstimateSearchContract.getAdminSanctionFromDate());
		}
		if (abstractEstimateSearchContract.getAdminSanctionToDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("adminSanctionDate <=:adminSanctionToDate");
			paramValues.put("adminSanctionToDate", abstractEstimateSearchContract.getAdminSanctionToDate());
		}
		if (abstractEstimateSearchContract.getSpillOverFlag() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("spillOverFlag =:spillOverFlag");
			paramValues.put("spillOverFlag", abstractEstimateSearchContract.getSpillOverFlag());
		}
		if (abstractEstimateSearchContract.getCreatedBy() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("createdBy =:createdBy");
			paramValues.put("createdBy", abstractEstimateSearchContract.getCreatedBy());
		}
		if (abstractEstimateSearchContract.getAbstractEstimateNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("abstractEstimateNumber in (:abstractEstimateNumbers)");
			paramValues.put("abstractEstimateNumbers", abstractEstimateSearchContract.getAbstractEstimateNumbers());
		}
		if (abstractEstimateSearchContract.getWorkIdentificationNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in (select abstractEstimate from egw_abstractestimate_details where projectCode in (select code from egw_projectcode where code in (:workIdentificationNumbers)))");
			paramValues.put("workIdentificationNumbers", abstractEstimateSearchContract.getWorkIdentificationNumbers());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(AbstractEstimateHelper.class);
		
		List<AbstractEstimateHelper> abstractEstimateEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		
		List<AbstractEstimate> abstractEstimates = new ArrayList<>();
		
		AbstractEstimate abstractEstimate;
		AbstractEstimateDetailsSearchContract abstractEstimateDetailsSearchContract;
		AbstractEstimateSanctionSearchContract abstractEstimateSanctionSearchContract;
		AbstractEstimateAssetDetailSearchContract abstractEstimateAssetDetailSearchContract;
		
		for(AbstractEstimateHelper abstractEstimateEntity : abstractEstimateEntities) {
			abstractEstimate = abstractEstimateEntity.toDomain();
			abstractEstimateDetailsSearchContract = new AbstractEstimateDetailsSearchContract();
			abstractEstimateDetailsSearchContract.setTenantId(abstractEstimateEntity.getTenantId());
			abstractEstimateDetailsSearchContract.setAbstractEstimateIds(Arrays.asList(abstractEstimateEntity.getId()));
			abstractEstimate.setAbstractEstimateDetails(abstractEstimateDetailsJdbcRepository.search(abstractEstimateDetailsSearchContract));
			
			abstractEstimateSanctionSearchContract = new AbstractEstimateSanctionSearchContract();
			abstractEstimateSanctionSearchContract.setTenantId(abstractEstimateEntity.getTenantId());
			abstractEstimateSanctionSearchContract.setAbstractEstimateIds(Arrays.asList(abstractEstimateEntity.getId()));
			abstractEstimate.setSanctionDetails(abstractEstimateSanctionDetailJdbcRepository.search(abstractEstimateSanctionSearchContract));
			
			abstractEstimateAssetDetailSearchContract = new AbstractEstimateAssetDetailSearchContract();
			abstractEstimateAssetDetailSearchContract.setTenantId(abstractEstimateEntity.getTenantId());
			abstractEstimateAssetDetailSearchContract.setAbstractEstimateIds(Arrays.asList(abstractEstimateEntity.getId()));
			abstractEstimate.setAssetDetails(abstractEstimateAssetDetailJdbcRepository.search(abstractEstimateAssetDetailSearchContract));
			
			abstractEstimates.add(abstractEstimate);
		}
		return abstractEstimates;
	}

}
