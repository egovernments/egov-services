package org.egov.lams.services.service.persistence.queryBuilder;

import org.egov.lams.common.web.contract.LandAcquisitionSearchCriteria;
import org.springframework.stereotype.Component;
@Component
public class LandAcquisitionQueryBuilder {
	
	public String buildSearchQuery(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {

		StringBuilder query = new StringBuilder("SELECT * FROM eg_lams_landacquisition WHERE tenantId = :tenantId");
		
		if(!landAcquisitionSearchCriteria.isLandAcquisitionIdAbsent())
            addWhereClauseWithAnd(query,"id","landAcquisitionId");

        if(!landAcquisitionSearchCriteria.isLandAquisitionNumberAbsent())
        	addWhereClauseWithAnd(query, "landAcquisitionNumber", "landAcquisitionNumber");

        if(!landAcquisitionSearchCriteria.isLandOwnerNameAbsent())
        	addWhereClauseWithAnd(query, "landOwnerName", "landOwnerName");
        
        if(!landAcquisitionSearchCriteria.isSurveyNoAbsent())
        	addWhereClauseWithAnd(query, "surveyNo", "surveyNo");
        
        if(!landAcquisitionSearchCriteria.isCtsNumberAbsent())
        	addWhereClauseWithAnd(query, "ctsNumber", "ctsNumber");
        
        if(!landAcquisitionSearchCriteria.isAdvocateNameAbsent())
        	addWhereClauseWithAnd(query, "advocateName", "advocateName");
        
        if(!landAcquisitionSearchCriteria.isLandTypeAbsent())
        	addWhereClauseWithAnd(query, "landType", "landType");
        
        if(!landAcquisitionSearchCriteria.isOrganizationNameAbsent())
        	addWhereClauseWithAnd(query, "organizationName", "organizationName");

        return query.toString();
	}
	
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }

}
