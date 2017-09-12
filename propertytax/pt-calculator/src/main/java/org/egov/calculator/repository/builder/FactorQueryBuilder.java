package org.egov.calculator.repository.builder;

import java.util.List;

import org.egov.calculator.utility.ConstantUtility;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 *         This Class will have all the queries which are used in the factor
 *         API's
 *
 */
public class FactorQueryBuilder {

	public static final String FACTOR_CREATE_QUERY = "INSERT into egpt_mstr_factor "
			+ "(tenantId, factorCode, factorType, factorValue, fromdate, todate, createdBy, lastModifiedBy, createdTime, lastModifiedTime) "
			+ "values(?,?,?,?,?,?,?,?,?,?)";

	public static final String FACTOR_UPDATE_QUERY = "UPDATE egpt_mstr_factor"
			+ " SET tenantId = ?, factorCode = ?, factorType = ?, factorValue = ?, fromdate = ?, todate = ?,"
			+ " lastModifiedBy = ?, lastModifiedtime = ? WHERE id = ?";

	public static final String BASE_SEARCH_QUERY = "select * from " + ConstantUtility.FACTOR_TABLE_NAME
			+ " WHERE tenantId=?";

	public static String getFactorSearchQuery(String tenantId, String factorType, String validDate, String code,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);

		searchSql.append(" AND factorType =?");
		preparedStatementValues.add(factorType);

		if (code != null && !code.isEmpty()) {
			searchSql.append(" AND factorCode =?");
			preparedStatementValues.add(code);
		}

		searchSql.append(" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date  AND todate::date");
		preparedStatementValues.add(validDate);

		return searchSql.toString();
	}

	public static String getFactorSearchQueryByTenantIdAndValidDate(String tenantId, String validDate,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		searchSql.append(" AND ( to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND todate::date)");
		preparedStatementValues.add(validDate);

		return searchSql.toString();
	}

	public static String getCreatedAuditDetails(String tableName, Long id) {
		return "select createdby, createdtime from " + tableName + "  where id=" + id;
	}

    public static String getFactorsForTaxCalculation(String factorType, Integer value, String tenantId, String validDate,
            List<Object> preparedStatementValues) {
        StringBuffer searchSql = new StringBuffer();
        searchSql.append(BASE_SEARCH_QUERY);
        preparedStatementValues.add(tenantId);
        searchSql.append("And factortype=?");
        preparedStatementValues.add(factorType);
        searchSql.append("And factorcode=?");
        preparedStatementValues.add(value.toString());
        searchSql.append(" AND ( to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND todate::date)");
        preparedStatementValues.add(validDate);
        return searchSql.toString();
    
    }
}
