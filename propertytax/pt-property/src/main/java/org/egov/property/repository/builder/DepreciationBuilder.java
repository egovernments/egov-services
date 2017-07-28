package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;

/**
 * 
 * @author Prasad This class will have the Depreciation CRU queries
 *
 */
public class DepreciationBuilder {

	public static final String INSERT_DEPRECIATION_QUERY = "insert into " + ConstantUtility.DEPRECIATION_TABLE_NAME
			+ " (tenantId,code," + "data,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_DEPRECIATION_QUERY = "UPDATE " + ConstantUtility.DEPRECIATION_TABLE_NAME
			+ " SET tenantId=?,code=?," + "data=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_DEPRECIATION_CREATETIME = "SELECT  createdTime From "
			+ ConstantUtility.DEPRECIATION_TABLE_NAME + " WHERE id = ?";

}
