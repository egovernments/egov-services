package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad
 * 	 This class will have all the common queries which will be use
 *         in the master & property as well
 *
 */
public class UtilityBuilder {

	public static final String UNIQUE_TENATANT_CODE_CHECK_QUERY = "select count(*) from ?"
			+ " where code = ? AND tenantid = ? AND id != ?";

	public static String getUniqueTenantCodeQuery(String tableName, String code, String tenantId, Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where code = '" + code + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();

	}

}
