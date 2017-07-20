package org.egov.asset.repository.builder;

public class CurrentValueQueryBuilder {

	public static final String CURRENT_VALUE_BASE_QUERY = "SELECT * FROM egasset_current_value allrecordsvalue INNER join ("
			+ "SELECT assetid,max(createddate) AS createddate FROM egasset_current_value "
			
			+ "WHERE tenantid=? and assetid IN ? group by assetid) b "
			
			+ "ON a.assetid=b.assetid and a.createddate=b.createddate "
			
			+ "where a.tenantid=? and a.assetid IN ?";
}
