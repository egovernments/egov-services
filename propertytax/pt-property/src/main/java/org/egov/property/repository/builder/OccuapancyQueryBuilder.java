package org.egov.property.repository.builder;

public class OccuapancyQueryBuilder {

	public static final String INSERT_OCCUAPANCY_QUERY = "INSERT INTO egpt_mstr_occuapancy ( "
			+ "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime) "
			+ "VALUES( ?, ?, ?, ?, ?, ?,?)";

	public static final String UPDATE_OCCUAPANCY_QUERY = "UPDATE egpt_mstr_occuapancy SET tenantid = "
			+ "? ,code = ?, data=?, lastModifiedBy =? ," + "lastModifiedtime= ? WHERE id = ?";

	public static final String SELECT_OCCUAPANCY_CREATETIME = "SELECT createdTime  From egpt_mstr_occuapancy WHERE id = ?";

}
