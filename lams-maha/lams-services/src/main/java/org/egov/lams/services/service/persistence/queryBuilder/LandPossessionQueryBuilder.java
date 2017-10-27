package org.egov.lams.services.service.persistence.queryBuilder;

import java.util.Set;

import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.springframework.stereotype.Component;
@Component
public class LandPossessionQueryBuilder {
	
	StringBuilder BASE_QUERY = new StringBuilder("SELECT * FROM eg_lams_landpossession lacq WHERE lacq.tenantId = :tenantId");

	public String getQuery(LandPossessionSearchCriteria landPossessionSearchCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		buildSearchQuery(selectQuery, landPossessionSearchCriteria);
		addPagingClause(selectQuery, landPossessionSearchCriteria);
		return selectQuery.toString();
	}
	public String buildSearchQuery(StringBuilder selectQuery,LandPossessionSearchCriteria landPossessionSearchCriteria) {
		
		if(!landPossessionSearchCriteria.isLandPossessionIdAbsent())
            addWhereClauseWithAnd(selectQuery,"id","landPossessionId");

        if(!landPossessionSearchCriteria.isLandAquisitionNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "lacq.landacquisitionid", "landAquisitionNumber");

        if(!landPossessionSearchCriteria.isPossessionNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "possessionNumber", "possessionNumber");
        
        if(!landPossessionSearchCriteria.isProposalNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "proposalNumber", "proposalNumber");
        
        if(!landPossessionSearchCriteria.isPossessionDateAbsent())
        	addWhereClauseWithAnd(selectQuery, "possessionDate", "possessionDate");
        
        if(!landPossessionSearchCriteria.isLandTypeAbsent())
        	addWhereClauseWithAnd(selectQuery, "landType", "landType");

        return selectQuery.toString();
	}
	
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
    
    private void addPagingClause(final StringBuilder selectQuery,final LandPossessionSearchCriteria landPossessionSearchCriteria) {
		if(landPossessionSearchCriteria.getSort() != null && !landPossessionSearchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY "+getOrderByQuery(landPossessionSearchCriteria.getSort()));
		else 
			selectQuery.append(" ORDER BY lacq.id ASC");

		long pageSize = 500 ;
		if (landPossessionSearchCriteria.getPageSize() != null)
			pageSize = landPossessionSearchCriteria.getPageSize();
		selectQuery.append(" LIMIT "+pageSize);

		// handle offset here
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (landPossessionSearchCriteria.getPageNumber() != null)
			pageNumber = landPossessionSearchCriteria.getPageNumber() - 1;
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
