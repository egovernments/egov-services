package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad This Class will have all the queries which are used in the
 *         roof type API's
 *
 */
public class RoofTypeBuilder {

	public static final String INSERT_ROOF_TYPE = "INSERT INTO egpt_mstr_rooftype ( "
			+ "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedTime) "
			+ " VALUES( ?, ?, ?, ?, ?,?,?)";

	public static final String UPDATE_ROOF_QUERY = "UPDATE egpt_mstr_rooftype SET tenantid = "
			+ "? ,code = ?, data=?, lastModifiedBy =? ," + "lastModifiedtime= ? WHERE id = ?";

	public static final String SELECT_ROOF_CREATETIME = "SELECT  createdTime From egpt_mstr_rooftype WHERE id = ?";

}
