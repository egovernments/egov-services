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
			+ " fromValue, toValue, ratePercentage, taxFlatValue,usage,propertytype, createdby,"
			+ " lastmodifiedby, createdtime, lastmodifiedtime)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAXRATES_QUERY = "UPDATE egpt_mstr_taxrates"
			+ " SET tenantid = ?, taxHead = ?, dependentTaxHead = ?, fromdate = ?,"
			+ " todate = ?, fromValue = ?, toValue = ?, ratePercentage = ?,"
			+ " taxFlatValue = ?, usage = ?,propertytype = ?, lastmodifiedby = ?, lastmodifiedtime = ?"
			+ " WHERE id = ?";

	public static final String BASE_SEARCH_QUERY = "SELECT * FROM " + ConstantUtility.TAXRATE_TABLE_NAME
			+ " WHERE tenantId=?";

	public static String getTaxRatesSearchQuery(String tenantId, String taxHead, String validDate,
			Double validARVAmount, String parentTaxHead, String usage, String propertyType,
			List<Object> preparedStatementValues) {

		StringBuffer selectQuery = new StringBuffer();
		selectQuery.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		if (taxHead != null && !taxHead.isEmpty()) {
			selectQuery.append(" AND taxHead =?");
			preparedStatementValues.add(taxHead);
		}

		selectQuery.append(
				" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  CASE WHEN  todate::date IS NULL THEN fromdate::date ELSE todate::date END");
		preparedStatementValues.add(validDate);
		if (validARVAmount != null) {
			selectQuery.append(" AND  ?  BETWEEN fromValue AND toValue");
			preparedStatementValues.add(validARVAmount);
		}

		if (parentTaxHead != null && !parentTaxHead.isEmpty()) {
			selectQuery.append(" AND dependentTaxHead =?");
			preparedStatementValues.add(parentTaxHead);
		}

		if (usage != null && !usage.isEmpty()) {
			selectQuery.append(" AND usage =?");
			preparedStatementValues.add(usage);
		}

		if (propertyType != null && !propertyType.isEmpty()) {
			selectQuery.append(" AND propertytype= ?");
			preparedStatementValues.add(propertyType);
		}

		return selectQuery.toString();
	}

	public static String getTaxRatesByTenantAndDate(String tenantId, String validDate,
			List<Object> preparedStatementValues) {

		StringBuffer selectQuery = new StringBuffer();
		selectQuery.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		selectQuery.append(
				" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  CASE WHEN  todate::date IS NULL THEN fromdate::date ELSE todate::date END");
		preparedStatementValues.add(validDate);
		return selectQuery.toString();
	}
}
