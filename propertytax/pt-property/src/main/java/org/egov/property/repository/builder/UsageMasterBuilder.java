package org.egov.property.repository.builder;

/**
 * @author Yosadhara This Class contains INSERT, UPDATE and SELECT queries for
 *         UsageMaster API's
 */
public class UsageMasterBuilder {

	public static final String INSERT_USAGEMASTER_QUERY = "INSERT INTO egpt_mstr_usage ("
			+ " tenantid, code, data, createdby, lastmodifiedby, createdtime, lastmodifiedtime) "
			+ " VALUES( ?, ?, ?, ?, ?, ?, ?)";

	public static final String UPDATE_USAGEMASTER_QUERY = "UPDATE egpt_mstr_usage" + " SET tenantid = ?, code = ?,"
			+ " data= ?, lastmodifiedby = ?, lastmodifiedtime = ?" + " WHERE id = ?";

	// public static final String SELECT_USAGEMASTER_QUERY
}
