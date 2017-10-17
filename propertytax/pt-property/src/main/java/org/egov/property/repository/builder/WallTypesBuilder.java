package org.egov.property.repository.builder;

/**
 * @author Yosadhara This Class contains INSERT, UPDATE and SELECT queries for
 *         WallTypes API's
 */
public class WallTypesBuilder {

	public static final String INSERT_WALLTYPES_QUERY = "INSERT INTO egpt_mstr_walltype"
			+ " ( tenantid, code, data, createdby," + " createdtime, lastmodifiedby, lastmodifiedtime)"
			+ " VALUES( ?, ?, ?, ?, ?, ?, ? )";

	public static final String UPDATE_WALLTYPES_QUERY = "UPDATE egpt_mstr_walltype"
			+ " SET tenantid = ?, code = ?, data =? ," + " lastmodifiedby = ?, lastmodifiedtime = ?" + " WHERE id = ?";

	public static final String SELECT_WALLTYPES_CREATETIME = "SELECT  createdTime From egpt_mstr_walltype WHERE id = ?";

}
