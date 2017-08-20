package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;

/**
 * This class have the Apartment Master creat, update and select Queries
 * 
 * @author Yosadhara
 *
 */
public class ApartmentBuilder {

	public static final String INSERT_APARTMENT_QUERY = "insert into " + ConstantUtility.APARTMENT_TABLE_NAME
			+ " (tenantId,code,name,data,createdby,lastmodifiedby,createdtime,"
			+ " lastmodifiedtime) VALUES (?,?,?,?,?,?,?,?)";

	public static final String UPDATE_APARTMENT_QUERY = "update " + ConstantUtility.APARTMENT_TABLE_NAME
			+ " SET tenantId=?, code=?, name=?, data=?, lastmodifiedby=?, lastmodifiedtime=? WHERE id = ?";
}
