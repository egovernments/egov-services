package org.egov.property.repository.builder;

public class DepartmentQueryBuilder {
	public static final String INSERT_DEPARTMENT_QUERY = "INSERT INTO egpt_mstr_department ("
			+"tenantId,code,data,createdBy, lastModifiedBy, createdTime,lastModifiedTime)"
			+"VALUES(?,?,?,?,?,?,?)";
	
	public static final String UPDATE_DEPARTMENT_QUERY = "UPDATE egpt_mstr_department SET tenantid = "
            + "? ,code = ?, data=?,lastModifiedBy =? ,"
            + "lastModifiedtime= ? WHERE id = ?";
	
}
