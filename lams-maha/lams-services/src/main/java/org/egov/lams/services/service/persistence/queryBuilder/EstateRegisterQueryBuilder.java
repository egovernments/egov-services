package org.egov.lams.services.service.persistence.queryBuilder;

import java.util.Set;

import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class EstateRegisterQueryBuilder {

	private final String BASE_QUERY=" select * from eglams_estateregistration rg "
			+ "left outer join eglams_floordetails fl on fl.estateregistrationid=rg.id "
			+ "Left outer join eglams_unitdetails ud ON ud.floor=fl.id where rg.tenantid = '";
	public String getQuery(EstateSearchCriteria estateSearchCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		selectQuery.append( estateSearchCriteria.getTenantId() + "'");
		addWhereClause(selectQuery, estateSearchCriteria);
		addPagingClause(selectQuery, estateSearchCriteria);
		return selectQuery.toString();
	}
	
	private void addWhereClause(StringBuilder selectQuery, EstateSearchCriteria estateSearchCriteria) {

		if (estateSearchCriteria.getEstateRegisterId() == null && estateSearchCriteria.getGattNo() == null &&
				estateSearchCriteria.getLocation() == null && estateSearchCriteria.getPropertyType() == null &&
				estateSearchCriteria.getRegionalOffice() == null && estateSearchCriteria.getRegisterName() == null)
			return;

			if(estateSearchCriteria.getEstateRegisterId()!=null)
				selectQuery.append(" rg.id="+estateSearchCriteria.getEstateRegisterId());
			if(estateSearchCriteria.getGattNo()!=null)
				selectQuery.append(" rg.gattno='"+estateSearchCriteria.getGattNo()+"'");
			if(estateSearchCriteria.getLocation()!=null)
				selectQuery.append(" rg.location='"+estateSearchCriteria.getLocation()+"'");
			if(estateSearchCriteria.getPropertyType()!=null)
				selectQuery.append(" rg.propertytype='"+estateSearchCriteria.getPropertyType()+"'");
			if(estateSearchCriteria.getRegionalOffice()!= null)
				selectQuery.append(" rg.regionaloffice='"+estateSearchCriteria.getRegionalOffice()+"'");
			if(estateSearchCriteria.getRegisterName()!=null)
				selectQuery.append(" rg.registername='"+estateSearchCriteria.getRegisterName()+"'");
			if(estateSearchCriteria.getSubRegisterName()!=null)
				selectQuery.append(" rg.subregistername='"+estateSearchCriteria.getSubRegisterName()+"'");
			if(estateSearchCriteria.getSurveyNo()!=null)
				selectQuery.append(" rg.surveyno='"+estateSearchCriteria.getSurveyNo()+"'");
	}	
	
	private void addPagingClause(final StringBuilder selectQuery,final EstateSearchCriteria estateSearchCriteria) {
		if(estateSearchCriteria.getSort() != null && !estateSearchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY "+getOrderByQuery(estateSearchCriteria.getSort()));
		else 
			selectQuery.append(" ORDER BY rg.id ASC");

		long pageSize = 500 ;
		if (estateSearchCriteria.getPageSize() != null)
			pageSize = estateSearchCriteria.getPageSize();
		selectQuery.append(" LIMIT "+pageSize);

		// handle offset here
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (estateSearchCriteria.getPageNumber() != null)
			pageNumber = estateSearchCriteria.getPageNumber() - 1;
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
