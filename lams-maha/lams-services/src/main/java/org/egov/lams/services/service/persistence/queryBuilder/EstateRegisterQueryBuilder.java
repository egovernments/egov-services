package org.egov.lams.services.service.persistence.queryBuilder;

import java.util.Set;

import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class EstateRegisterQueryBuilder {

	private final String BASE_QUERY=" select rg.id AS rg_id, rg.tenantid AS rg_tenantid,"
			+ " rg.registername AS rg_registername, rg.subregistername AS rg_subregistername,"
			+ " rg.regionaloffice AS rg_regionaloffice, rg.location AS rg_location, rg.propertyname AS rg_propertyname,"
			+ " rg.propertytype AS rg_propertytype, rg.propertyaddress AS rg_propertyaddress, rg.surveyno AS rg_surveyno,"
			+ " rg.gattno AS rg_gattno, rg.builduparea AS rg_builduparea, rg.purpose AS rg_purpose,"
			+ " rg.modeofacquisition AS rg_modeofacquisition, rg.carpetarea AS rg_carpetarea, rg.holdingtype AS rg_holdingtype,"
			+ " rg.landid AS rg_landid, rg.constructionstartdate As rg_constructionstartdate,"
			+ " rg.constructionenddate AS rg_constructionenddate, rg.proposedbuildingbudget AS rg_proposedbuildingbudget,"
			+ " rg.actualbuildingexpense AS rg_actualbuildingexpense, rg.latitude AS rg_latitude, rg.longitude AS rg_longitude,"
			+ " rg.stateid AS rg_stateid, rg.comments AS rg_comments, rg.createdby AS rg_createdby, rg.createdtime AS rg_createdtime,"
			+ " rg.lastmodifiedby AS rg_lastmodifiedby, rg.lastmodifiedtime As rg_lastmodifiedtime,"
			+ " fl.id AS fl_id, fl.tenantid AS fl_tenantid, fl.estateregistrationid AS fl_estateregistrationid, fl.floorno AS fl_floorno,"
			+ " fl.floorarea As fl_floorarea, fl.noofunits AS fl_noofunits, fl.createdby As fl_createdby, fl.createdtime As fl_createdtime,"
			+ " fl.lastmodifiedby AS fl_lastmodifiedby, fl.lastmodifiedtime As fl_lastmodifiedtime,"
			+ " ud.id AS ud_id, ud.tenantid AS ud_tenantid, ud.floor AS ud_floor, ud.usagetype AS ud_usagetype,"
			+ " ud.previousunitno As ud_previousunitno, ud.builtuparea AS ud_builtuparea, ud.holdingtype AS ud_holdingtype,"
			+ " ud.departmentname AS ud_departmentname, ud.description AS ud_description, ud.createdby AS ud_createdby,"
			+ " ud.createdtime AS ud_createdtime, ud.lastmodifiedby AS ud_lastmodifiedby, ud.lastmodifiedtime AS ud_lastmodifiedtime"
			+ " FROM eglams_estateregistration rg"
			+ " LEFT OUTER JOIN eglams_floordetails fl ON fl.estateregistrationid=rg.id"
			+ " LEFT OUTER JOIN eglams_unitdetails ud ON ud.floor=fl.id where rg.tenantid = '";
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
				selectQuery.append(" AND rg.id="+estateSearchCriteria.getEstateRegisterId());
			if(estateSearchCriteria.getGattNo()!=null)
				selectQuery.append(" AND rg.gattno='"+estateSearchCriteria.getGattNo()+"'");
			if(estateSearchCriteria.getLocation()!=null)
				selectQuery.append(" AND rg.location='"+estateSearchCriteria.getLocation()+"'");
			if(estateSearchCriteria.getPropertyType()!=null)
				selectQuery.append(" AND rg.propertytype='"+estateSearchCriteria.getPropertyType()+"'");
			if(estateSearchCriteria.getRegionalOffice()!= null)
				selectQuery.append(" AND rg.regionaloffice='"+estateSearchCriteria.getRegionalOffice()+"'");
			if(estateSearchCriteria.getRegisterName()!=null)
				selectQuery.append(" AND rg.registername='"+estateSearchCriteria.getRegisterName()+"'");
			if(estateSearchCriteria.getSubRegisterName()!=null)
				selectQuery.append(" AND rg.subregistername='"+estateSearchCriteria.getSubRegisterName()+"'");
			if(estateSearchCriteria.getSurveyNo()!=null)
				selectQuery.append(" AND rg.surveyno='"+estateSearchCriteria.getSurveyNo()+"'");
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
