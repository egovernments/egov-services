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

	public static final String TABLE_NAME = "egw_abstractestimate estimate";
	public static final String DETAILS_SEARCH_EXTENTION = ", egw_abstractestimate_details details";
	
	@Autowired
	private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;
	
	@Autowired
	private AbstractEstimateSanctionDetailJdbcRepository abstractEstimateSanctionDetailJdbcRepository;
	
	@Autowired
	private AbstractEstimateAssetDetailJdbcRepository abstractEstimateAssetDetailJdbcRepository;
	
	public List<AbstractEstimate> search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
		
		String tableName = TABLE_NAME;
		
		if (abstractEstimateSearchContract.getNameOfWork() != null
				|| abstractEstimateSearchContract.getWorkIdentificationNumbers() != null)
			tableName += DETAILS_SEARCH_EXTENTION;
			

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (abstractEstimateSearchContract.getSortBy() != null && !abstractEstimateSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(abstractEstimateSearchContract.getSortBy());
			validateEntityFieldName(abstractEstimateSearchContract.getSortBy(), AbstractEstimateHelper.class);
		}

		String orderBy = "order by estimate.id";
		if (abstractEstimateSearchContract.getSortBy() != null && !abstractEstimateSearchContract.getSortBy().isEmpty()) {
			orderBy = "order by estimate." + abstractEstimateSearchContract.getSortBy();
		}

		if (abstractEstimateSearchContract.getTenantId() != null) {
			addAnd(params);
			params.append("estimate.tenantId =:tenantId");
			paramValues.put("tenantId", abstractEstimateSearchContract.getTenantId());
		}
		if (abstractEstimateSearchContract.getIds() != null) {
			addAnd(params);
			params.append("estimate.id in(:ids) ");
			paramValues.put("ids", abstractEstimateSearchContract.getIds());
		}
		if (abstractEstimateSearchContract.getAdminSanctionNumbers() != null && !abstractEstimateSearchContract.getAdminSanctionNumbers().isEmpty() && abstractEstimateSearchContract.getAdminSanctionNumbers().size() == 1) {
			addAnd(params);
			params.append("lower(estimate.adminSanctionNumber) like :adminSanctionNumbers");
			paramValues.put("adminSanctionNumbers", '%' + abstractEstimateSearchContract.getAdminSanctionNumbers().get(0).toLowerCase() + '%');
		} else if(abstractEstimateSearchContract.getAdminSanctionNumbers() != null) {
			addAnd(params);
			params.append("estimate.adminSanctionNumber in (:adminSanctionNumbers)");
			paramValues.put("adminSanctionNumbers", abstractEstimateSearchContract.getAdminSanctionNumbers());
		}
		if (abstractEstimateSearchContract.getDepartments() != null) {
			addAnd(params);
			params.append("estimate.department in (:departments)");
			paramValues.put("departments", abstractEstimateSearchContract.getDepartments());
		}
		if (abstractEstimateSearchContract.getFundCodes() != null) {
			addAnd(params);
			params.append("estimate.fund in (:funds)");
			paramValues.put("funds", abstractEstimateSearchContract.getFundCodes());
		}
		if (abstractEstimateSearchContract.getFunctionCodes() != null) {
			addAnd(params);
			params.append("estimate.function in (:functions)");
			paramValues.put("functions", abstractEstimateSearchContract.getFunctionCodes());
		}
		if (abstractEstimateSearchContract.getBudgetHeadCodes() != null) {
			addAnd(params);
			params.append("estimate.budgetHead in (:budgetHeads)");
			paramValues.put("budgetHeads", abstractEstimateSearchContract.getBudgetHeadCodes());
		}
		if (abstractEstimateSearchContract.getStatuses() != null) {
		    addAnd(params);
			params.append("estimate.status in (:statuses)");
			paramValues.put("statuses", abstractEstimateSearchContract.getStatuses());
		}
		if (abstractEstimateSearchContract.getNameOfWork() != null) {
			addAnd(params);
			params.append("estimate.id = details.abstractestimate and upper(details.nameofwork) like :nameOfWork and details.tenantId=:tenantId and details.deleted = false");
			paramValues.put("nameOfWork", '%' + abstractEstimateSearchContract.getNameOfWork().toUpperCase() + '%');
            paramValues.put("tenantId", abstractEstimateSearchContract.getTenantId());
		}
		if (abstractEstimateSearchContract.getAdminSanctionFromDate() != null) {
			addAnd(params);
			params.append("estimate.adminSanctionDate >=:adminSanctionFromDate");
			paramValues.put("adminSanctionFromDate", abstractEstimateSearchContract.getAdminSanctionFromDate());
		}
		if (abstractEstimateSearchContract.getAdminSanctionToDate() != null) {
			addAnd(params);
			params.append("estimate.adminSanctionDate <=:adminSanctionToDate");
			paramValues.put("adminSanctionToDate", abstractEstimateSearchContract.getAdminSanctionToDate());
		}
		if (abstractEstimateSearchContract.getSpillOverFlag() != null) {
			addAnd(params);
			params.append("estimate.spillOverFlag =:spillOverFlag");
			paramValues.put("spillOverFlag", abstractEstimateSearchContract.getSpillOverFlag());
		}
		if (abstractEstimateSearchContract.getCreatedBy() != null) {
			addAnd(params);
			params.append("upper(estimate.createdBy) =:createdBy");
			paramValues.put("createdBy", abstractEstimateSearchContract.getCreatedBy().toUpperCase());
		}
		if (abstractEstimateSearchContract.getAbstractEstimateNumbers() != null && abstractEstimateSearchContract.getAbstractEstimateNumbers().size() == 1 && !abstractEstimateSearchContract.getAbstractEstimateNumbers().isEmpty()) {
			addAnd(params);
			params.append("upper(estimate.abstractEstimateNumber) like :abstractEstimateNumbers");
			paramValues.put("abstractEstimateNumbers", '%' + abstractEstimateSearchContract.getAbstractEstimateNumbers().get(0).toUpperCase() + '%' );
		} else if(abstractEstimateSearchContract.getAbstractEstimateNumbers() != null && !abstractEstimateSearchContract.getAbstractEstimateNumbers().isEmpty()) {
			addAnd(params);
			params.append("estimate.abstractEstimateNumber in (:abstractEstimateNumbers)");
			paramValues.put("abstractEstimateNumbers", abstractEstimateSearchContract.getAbstractEstimateNumbers());
		}
		
		if (abstractEstimateSearchContract.getWorkIdentificationNumbers() != null) {
			addAnd(params);
			params.append("estimate.id = details.abstractEstimate and details.projectCode in (:workIdentificationNumbers) and details.tenantId=:tenantId and details.deleted = false");
			paramValues.put("workIdentificationNumbers", abstractEstimateSearchContract.getWorkIdentificationNumbers());
            paramValues.put("tenantId", abstractEstimateSearchContract.getTenantId());

		}
		
        if (abstractEstimateSearchContract.getCouncilSanctionNumbers() != null
                && !abstractEstimateSearchContract.getCouncilSanctionNumbers().isEmpty()
                && abstractEstimateSearchContract.getCouncilSanctionNumbers().size() == 1) {
            addAnd(params);
            params.append("lower(estimate.councilResolutionNumber) like :councilSanctionNumbers");
            paramValues.put("councilSanctionNumbers",
                    '%' + abstractEstimateSearchContract.getCouncilSanctionNumbers().get(0).toLowerCase() + '%');
        } else if (abstractEstimateSearchContract.getCouncilSanctionNumbers() != null) {
            addAnd(params);
            params.append("estimate.councilResolutionNumber in (:councilSanctionNumbers)");
            paramValues.put("councilSanctionNumbers", abstractEstimateSearchContract.getCouncilSanctionNumbers());
        }

        params.append(" and estimate.deleted = false");
		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else
			searchQuery = searchQuery.replace(":condition", "");
		
		searchQuery = searchQuery.replace(":tablename", tableName);

		searchQuery = searchQuery.replace(":selectfields", " estimate.* ");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(AbstractEstimateHelper.class);
		
		List<AbstractEstimateHelper> abstractEstimateEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		System.out.print("Search Query" + searchQuery.toString());
		System.out.print("paramValues" + paramValues.get("tenantId"));
		System.out.print("paramValues" + paramValues.get("abstractEstimateNumbers"));

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
