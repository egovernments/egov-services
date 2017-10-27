package org.egov.lams.services.service.persistence.queryBuilder;

import java.util.Set;

import org.egov.lams.common.web.contract.LandAcquisitionSearchCriteria;
import org.springframework.stereotype.Component;
@Component
public class LandAcquisitionQueryBuilder {
	
	StringBuilder BASE_QUERY = new StringBuilder("SELECT * FROM eg_lams_landacquisition lacq WHERE tenantId = :tenantId");

	public String getQuery(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		buildSearchQuery(selectQuery, landAcquisitionSearchCriteria);
		addPagingClause(selectQuery, landAcquisitionSearchCriteria);
		return selectQuery.toString();
	}
	public String buildSearchQuery(StringBuilder selectQuery,LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {

		
		if(!landAcquisitionSearchCriteria.isLandAcquisitionIdAbsent())
            addWhereClauseWithAnd(selectQuery,"id","landAcquisitionId");

        if(!landAcquisitionSearchCriteria.isLandAquisitionNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "landAcquisitionNumber", "landAcquisitionNumber");

        if(!landAcquisitionSearchCriteria.isLandOwnerNameAbsent())
        	addWhereClauseWithAnd(selectQuery, "landOwnerName", "landOwnerName");
        
        if(!landAcquisitionSearchCriteria.isSurveyNoAbsent())
        	addWhereClauseWithAnd(selectQuery, "surveyNo", "surveyNo");
        
        if(!landAcquisitionSearchCriteria.isCtsNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "ctsNumber", "ctsNumber");
        
        if(!landAcquisitionSearchCriteria.isAdvocateNameAbsent())
        	addWhereClauseWithAnd(selectQuery, "advocateName", "advocateName");
        
        if(!landAcquisitionSearchCriteria.isLandTypeAbsent())
        	addWhereClauseWithAnd(selectQuery, "landType", "landType");
        
        if(!landAcquisitionSearchCriteria.isOrganizationNameAbsent())
        	addWhereClauseWithAnd(selectQuery, "organizationName", "organizationName");

        return selectQuery.toString();
	}
	
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
    
    private void addPagingClause(final StringBuilder selectQuery,final LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {
		if(landAcquisitionSearchCriteria.getSort() != null && !landAcquisitionSearchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY "+getOrderByQuery(landAcquisitionSearchCriteria.getSort()));
		else 
			selectQuery.append(" ORDER BY lacq.id ASC");

		long pageSize = 500 ;
		if (landAcquisitionSearchCriteria.getPageSize() != null)
			pageSize = landAcquisitionSearchCriteria.getPageSize();
		selectQuery.append(" LIMIT "+pageSize);

		// handle offset here
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (landAcquisitionSearchCriteria.getPageNumber() != null)
			pageNumber = landAcquisitionSearchCriteria.getPageNumber() - 1;
		selectQuery.append(" OFFSET "+pageNumber);
															// pageNo * pageSize
	}
	
	private String getOrderByQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append(list[0]+" ASC");
			for (int i = 1; i < idList.size(); i++)
				query.append("," +list[i]+" ASC");
		}
		return query.toString();
	}

}
