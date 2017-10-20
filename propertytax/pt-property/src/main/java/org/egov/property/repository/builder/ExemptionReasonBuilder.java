package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;

public class ExemptionReasonBuilder {

	public static final String INSERT_TAXEXEMPTIONREASON_QUERY = "insert into "
			+ ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME
			+ " (tenantid,name,code,description,active,percentagerate,data,"
			+ " createdby,lastmodifiedby,createdtime,lastmodifiedtime) "
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAXEXEMPTIONREASON_QUERY = "UPDATE "
			+ ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME
			+ " SET tenantid=?, name=?, code=?, description = ?, active=?, "
			+ " percentagerate=?, data=?,lastmodifiedby=?,lastmodifiedtime=?" 
			+ " WHERE id = ?";

	public static final String SELECT_EXEMPTIONREASON_CREATETIME = "SELECT createdTime From "
			+ ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME + " WHERE id = ?";

	public static final String CHECK_UNIQUE_CODE = "select count(*) from "
			+ ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME + " where code = ?";
}
