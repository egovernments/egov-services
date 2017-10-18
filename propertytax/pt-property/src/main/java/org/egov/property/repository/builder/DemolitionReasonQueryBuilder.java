package org.egov.property.repository.builder;

import java.util.List;

import org.egov.models.DemolitionReasonSearchCriteria;

public class DemolitionReasonQueryBuilder {
	public static final String INSERT_DEMOLITIONREASON_QUERY = "INSERT INTO egpt_mstr_Demolition ("
			+ "tenantid,name,code,description,createdby, lastmodifiedby, createdtime,lastmodifiedtime)" + "VALUES(?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DEMOLITIONREASON_QUERY = "UPDATE egpt_mstr_Demolition SET tenantid = "
			+ "? ,name = ?, code=?,description=?,lastModifiedBy =? ," + "lastModifiedtime= ? WHERE id = ?";

	public static String getSearchQuery(DemolitionReasonSearchCriteria demolitionReasonSearchCriteria,
			List<Object> preparedStatementValues) {


		StringBuffer searchSql = new StringBuffer();
		searchSql.append("SELECT * FROM egpt_mstr_Demolition WHERE");

		searchSql.append(" LOWER(tenantId) =LOWER(?) ");
		preparedStatementValues.add(demolitionReasonSearchCriteria.getTenantId());

		if (demolitionReasonSearchCriteria.getName() != null) {
			searchSql.append(" AND LOWER(name) = LOWER(?) ");
			preparedStatementValues.add(demolitionReasonSearchCriteria.getName());
		}

		if (demolitionReasonSearchCriteria.getCode() != null) {
			searchSql.append(" AND LOWER(code) = LOWER(?) ");
			preparedStatementValues.add(demolitionReasonSearchCriteria.getCode());
		}
		

		if (demolitionReasonSearchCriteria.getOffSet() == null) {
			demolitionReasonSearchCriteria.setOffSet(0);
		}		
		
		searchSql.append("offset ? ");
		preparedStatementValues.add(demolitionReasonSearchCriteria.getOffSet());
		
		if (demolitionReasonSearchCriteria.getPageSize() == null) {
			demolitionReasonSearchCriteria.setPageSize(20);
		}	
		searchSql.append("limit ? ");
		preparedStatementValues.add(demolitionReasonSearchCriteria.getPageSize());


		return searchSql.toString();
	}
}
