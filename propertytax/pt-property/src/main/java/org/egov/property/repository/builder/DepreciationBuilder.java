package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad 
 * This class will have the Depreciation CRU queries
 *
 */
public class DepreciationBuilder {

	public static final String INSERT_DEPRECIATION_QUERY = "insert into egpt_depreciation (tenantId,code,"
			+ "data,createdby,lastmodifiedby,createdtime," + "lastmodifiedtime) VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_DEPRECIATION_QUERY = "UPDATE egpt_depreciation SET tenantId=?,code=?,"
			+ "data=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_DEPRECIATION_CREATETIME = "SELECT  createdTime From egpt_depreciation WHERE id = ?";
	
}
