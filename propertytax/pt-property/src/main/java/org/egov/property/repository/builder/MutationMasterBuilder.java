package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;

/**
 * This class will have the mutation master create,search & update Queries
 * 
 * @author Prasad
 *
 */
public class MutationMasterBuilder {

	public static final String INSERT_MUTATTION_QUERY = "insert into " + ConstantUtility.MUTATION_MASTER_TABLE_NAME
			+ " (tenantId,code,name,data,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?,?)";

	public static final String UPDATE_MUTATION_QUERY = "UPDATE " + ConstantUtility.MUTATION_MASTER_TABLE_NAME
			+ " SET tenantId=?,code=?," + "name=?,data=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_MUTATION_CREATETIME = "SELECT createdTime From "
			+ ConstantUtility.MUTATION_MASTER_TABLE_NAME + " WHERE id = ?";

	public static final String CHECK_UNIQUE_CODE = "select count(*) from " + ConstantUtility.MUTATION_MASTER_TABLE_NAME
			+ " where code = ?";

}
