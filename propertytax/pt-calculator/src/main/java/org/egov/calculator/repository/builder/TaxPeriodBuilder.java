package org.egov.calculator.repository.builder;

import java.util.List;

import org.egov.calculator.utility.ConstantUtility;

/**
 * <h1>TaxPeriodBuilder</h1>This class contains all the queries related to the
 * tax period
 * 
 * @author Prasad
 *
 */
public class TaxPeriodBuilder {

	public static final String INSERT_TAX_PERIOD_QUERY = "INSERT INTO egpt_mstr_taxperiods "
			+ "(tenantid,fromdate,todate,code,periodType,financialYear,createdby,lastmodifiedby,"
			+ "createdtime,lastmodifiedtime) VALUES(?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAX_PERIOD_QUERY = "UPDATE egpt_mstr_taxperiods SET tenantid=?,fromdate=?"
			+ ",todate=?,code=?,periodType=?,financialYear=?,createdby=?,lastmodifiedby=?,createdtime=?,"
			+ "lastmodifiedtime=? WHERE tenantid =?";

	public static final String BASE_SEARCH_QUERY = "SELECT * FROM " + ConstantUtility.TAXPERIODS_TABLE_NAME
			+ " WHERE tenantId =?";

	public static String getSearchQuery(String tenantId, String validDate, String code, String fromDate, String toDate,
			String sortTaxPeriod, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);

		if (code != null) {
			if (!code.isEmpty()) {
				searchSql.append(" AND code =?");
				preparedStatementValues.add(code);
			}
		}

		if (validDate != null) {
			if (!validDate.isEmpty()) {
				searchSql.append(" AND (to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  todate::date )");
				preparedStatementValues.add(validDate);
			}
		}
		if (fromDate != null && toDate != null) {
			searchSql
					.append(" AND (fromdate::date >= (SELECT fromdate::date FROM egpt_mstr_taxperiods WHERE tenantId =? "
							+ "AND (to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  todate::date)) and todate::date<="
							+ "(SELECT todate::date FROM egpt_mstr_taxperiods WHERE tenantId = ? AND (to_date(?,'dd/MM/yyyy') "
							+ "BETWEEN fromdate::date AND  todate::date)))");
			preparedStatementValues.add(tenantId);
			preparedStatementValues.add(fromDate);
			preparedStatementValues.add(tenantId);
			preparedStatementValues.add(toDate);
		}
		if (sortTaxPeriod != null) {
			searchSql.append(" ORDER BY " + sortTaxPeriod);
		} else {
			searchSql.append(" ORDER BY fromdate ASC");
		}

		return searchSql.toString();
	}

	public static String getTaxperiodsByDateAndTenantId(String tenantId, String fromDate, String toDate,
			List<Object> preparedStatementValues) {
		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		searchSql.append(" AND (fromdate::date>= to_date(?,'dd/MM/yyy') And  todate::date<= to_date(?,'dd/MM/yyy'))");
		preparedStatementValues.add(fromDate);
		preparedStatementValues.add(toDate);
		return searchSql.toString();
	}

	public static String getToDateForTaxCalculation(String tenantId, String date,
			List<Object> preparedStatementValues) {
		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		searchSql.append(" and todate::date  >= to_date(?,'dd/MM/yyyy') ORDER BY todate ASC LIMIT 1");
		preparedStatementValues.add(date);
		return searchSql.toString();

	}

	public static String getFromDateForTaxCalculation(String tenantId, String date,
			List<Object> preparedStatementValues) {
		StringBuffer searchSql = new StringBuffer();
		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		searchSql.append("and fromdate::date  <= to_date(?,'dd/MM/yyyy') ORDER BY todate DESC LIMIT 1");
		preparedStatementValues.add(date);

		return searchSql.toString();

	}
}
