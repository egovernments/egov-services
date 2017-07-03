package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad This Class will have all the queries which are used in the
 *         wood type API's
 *
 */
public class WoodTypeBuilder {

	public static final String INSERT_WOOD_QUERY = "INSERT INTO egpt_mstr_woodtype ( "
			+ "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime) "
			+ "VALUES( ?, ?, ?, ?, ?, ?,?)";

	public static final String UPDATE_WOOD_QUERY = "UPDATE egpt_mstr_woodtype SET tenantid = "
			+ "? ,code = ?, data=?,lastModifiedBy =?," + "lastModifiedtime= ? WHERE id = ?";

	public static final String SELECT_WOOD_CREATETIME = "SELECT createdTime From egpt_mstr_woodtype WHERE id = ?";

}
