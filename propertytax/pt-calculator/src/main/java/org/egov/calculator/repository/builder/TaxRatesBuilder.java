package org.egov.calculator.repository.builder;

import java.util.List;

import org.egov.calculator.utility.ConstantUtility;

/**
 * 
 * @author Yosodhara P This Class will have all the queries which are used in
 *         the taxrates API's
 *
 */
public class TaxRatesBuilder {

	public static final String INSERT_TAXRATES_QUERY = "INSERT INTO egpt_mstr_taxrates"
			+ " (tenantid, taxHead, dependentTaxHead, fromdate, todate,"
			+ " fromValue, toValue, ratePercentage, taxFlatValue, createdby,"
			+ " lastmodifiedby, createdtime, lastmodifiedtime)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAXRATES_QUERY = "UPDATE egpt_mstr_taxrates"
			+ " SET tenantid = ?, taxHead = ?, dependentTaxHead = ?, fromdate = ?,"
			+ " todate = ?, fromValue = ?, toValue = ?, ratePercentage = ?,"
			+ " taxFlatValue = ?, lastmodifiedby = ?, lastmodifiedtime = ?" + " WHERE id = ?";

	public static final String BASE_SEARCH_QUERY = "SELECT * FROM " + ConstantUtility.TAXRATE_TABLE_NAME
			+ " WHERE tenantId=?";

	public static String getTaxRatesSearchQuery(String tenantId, String taxHead, String validDate,
			Double validARVAmount, String parentTaxHead, List<Object> preparedStatementValues) {

		StringBuffer selectQuery = new StringBuffer();
		selectQuery.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);

		selectQuery.append(" AND taxHead =?");
		preparedStatementValues.add(taxHead);
		selectQuery.append(" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  todate::date");
		preparedStatementValues.add(validDate);

		selectQuery.append(" AND  ?  BETWEEN fromValue AND toValue");
		preparedStatementValues.add(validARVAmount);

		if (parentTaxHead != null && !parentTaxHead.isEmpty()) {
			selectQuery.append(" AND dependentTaxHead =?");
			preparedStatementValues.add(parentTaxHead);
		}

		return selectQuery.toString();
	}

	public static String getTaxRatesByTenantAndDate(String tenantId, String validDate,
			List<Object> preparedStatementValues) {

		StringBuffer selectQuery = new StringBuffer();
		selectQuery.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		selectQuery.append(" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  todate::date");
		preparedStatementValues.add(validDate);
		return selectQuery.toString();
	}
}
