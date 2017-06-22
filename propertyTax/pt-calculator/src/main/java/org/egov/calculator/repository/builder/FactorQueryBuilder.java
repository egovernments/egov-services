package org.egov.calculator.repository.builder;
/**
 * 
 * @author Pavan Kumar Kamma 
 * 
 * This Class will have all the queries which are used in the factor API's
 *
 */
public class FactorQueryBuilder {

	public static final String FACTOR_CREATE_QUERY = "INSERT into egpt_mstr_factor "
			+ "(tenantId, factorCode, factorType, factorValue, fromdate, todate, createdBy, lastModifiedBy, createdTime, lastModifiedTime) "
			+ "values(?,?,?,?,?,?,?,?,?,?)";

	public static final String FACTOR_UPDATE_QUERY = "UPDATE egpt_mstr_factor"
			+ " SET tenantId = ?, factorCode = ?, factorType = ?, factorValue = ?, fromdate = ?, todate = ?,"
			+ " lastModifiedtime = ? WHERE id = ?";

	public static String getFactorSearchQuery(String tenantId,
			String factorType, String validDate, String code) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("SELECT * FROM egpt_mstr_factor WHERE tenantId = '"
				+ tenantId + "' AND factorType = '" + factorType
				+ "' AND factorCode = '" + code + "' AND (fromdate < '"
				+ validDate + "' AND todate > '" + validDate + "')");

		return searchSql.toString();
	}

	public static String getFactorSearchQueryByTenantIdAndValidDate(
			String tenantId, String validDate) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("SELECT * FROM egpt_mstr_factor WHERE tenantId = '"
				+ tenantId + "' AND (fromdate < '" + validDate
				+ "' AND todate > '" + validDate + "')");

		return searchSql.toString();
	}

}
