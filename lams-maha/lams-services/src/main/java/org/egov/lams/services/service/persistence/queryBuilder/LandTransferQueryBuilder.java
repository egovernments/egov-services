package org.egov.lams.services.service.persistence.queryBuilder;

import java.util.Set;

import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.common.web.contract.LandTransferSearchCriteria;
import org.springframework.stereotype.Component;
@Component
public class LandTransferQueryBuilder {
	

	StringBuilder BASE_QUERY = new StringBuilder("SELECT * FROM eg_lams_landtransfer lndtrns WHERE lndtrns.tenantId = :tenantId");

	public String getQuery(LandTransferSearchCriteria landTransferSearchCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		buildSearchQuery(selectQuery, landTransferSearchCriteria);
		addPagingClause(selectQuery, landTransferSearchCriteria);
		return selectQuery.toString();
	}
	public String buildSearchQuery(StringBuilder selectQuery,LandTransferSearchCriteria landTransferSearchCriteria) {
		
		if(!landTransferSearchCriteria.isTransferNumberAbsent())
            addWhereClauseWithAnd(selectQuery,"lndtrns.landtransfernumber","transferNumber");

        if(!landTransferSearchCriteria.isLandAquisitionNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "lndtrf.landacquisitionid", "landAquisitionNumber");

        if(!landTransferSearchCriteria.isResolutionNumberAbsent())
        	addWhereClauseWithAnd(selectQuery, "lndtrns.resolutionNumber", "resolutionNumber");
        
        if(!landTransferSearchCriteria.isTransferFromAbsent())
        	addWhereClauseWithAnd(selectQuery, "lndtrns.transferFrom", "transferFrom");
        
        if(!landTransferSearchCriteria.isTransferedToDateAbsent())
        	addWhereClauseWithAnd(selectQuery, "lndtrns.transferedToDate", "transferedToDate");

        return selectQuery.toString();
	}
	
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
    
    private void addPagingClause(final StringBuilder selectQuery,final LandTransferSearchCriteria landTransferSearchCriteria) {
		if(landTransferSearchCriteria.getSort() != null && !landTransferSearchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY "+getOrderByQuery(landTransferSearchCriteria.getSort()));
		else 
			selectQuery.append(" ORDER BY lndtrns.id ASC");

		long pageSize = 500 ;
		if (landTransferSearchCriteria.getPageSize() != null)
			pageSize = landTransferSearchCriteria.getPageSize();
		selectQuery.append(" LIMIT "+pageSize);

		// handle offset here
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (landTransferSearchCriteria.getPageNumber() != null)
			pageNumber = landTransferSearchCriteria.getPageNumber() - 1;
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
