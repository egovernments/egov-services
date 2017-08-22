package org.egov.property.repository.builder;

/**
 * @author Yosadhara This Class contains INSERT & UPDATE queries for UsageMaster
 *         API's
 */
public class UsageMasterBuilder {

	public static final String INSERT_USAGEMASTER_QUERY = "INSERT INTO egpt_mstr_usage ("
			+ " tenantid, code,parent, data, service, createdby, lastmodifiedby, createdtime, lastmodifiedtime) "
			+ " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String UPDATE_USAGEMASTER_QUERY = "UPDATE egpt_mstr_usage" + " SET tenantid = ?, code = ?,"
			+ "parent = ?, data= ?, service = ?, lastmodifiedby = ?, lastmodifiedtime = ?" + " WHERE id = ?";

	public static final String SELECT_USAGEMASTER_CREATETIME = "SELECT  createdTime From egpt_mstr_usage WHERE id = ?";

}
