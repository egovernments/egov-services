package org.egov.calculator.repository.builder;

import java.util.List;

import org.egov.calculator.utility.ConstantUtility;

/**
 * This Class will have all the queries which are used in the
 * TransferFeeRateBuilder API's
 * 
 * @author Yosadhara
 *
 */
public class TransferFeeRateBuilder {

	public static final String INSERT_TRANSFERFEERATES_QUERY = "INSERT INTO "
			+ ConstantUtility.TRANSFERFEERATES_TABLE_NAME
			+ " (tenantid, feefactor, fromdate, todate, fromvalue, tovalue, feepercentage, flatvalue,"
			+ " createdby,lastmodifiedby, createdtime, lastmodifiedtime)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TRANSFERFEERATES_QUERY = "UPDATE " + ConstantUtility.TRANSFERFEERATES_TABLE_NAME
			+ " SET tenantid = ?, feefactor = ?, fromdate = ?, todate = ?, "
			+ " fromvalue = ?, tovalue = ?, feepercentage = ?, flatvalue = ?, "
			+ " lastmodifiedby = ?, lastmodifiedtime = ?" + " WHERE id = ? ";

	public static final String BASE_SEARCH_QUERY = "SELECT * FROM " + ConstantUtility.TRANSFERFEERATES_TABLE_NAME
			+ " WHERE tenantid=?";
	
	public static final String getTransferFeeRates(String tenantId, String feeFactor, String validDate,
			Double validValue, List<Object> preparedStatementValues) {
		StringBuffer selectQuery = new StringBuffer();		
		selectQuery.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);		
		selectQuery.append(" AND LOWER(feefactor) = LOWER(?)");
		preparedStatementValues.add(feeFactor);		
		selectQuery.append(
				" AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND  CASE WHEN  todate::date IS NULL THEN fromdate::date ELSE todate::date END");
		preparedStatementValues.add(validDate);		
		selectQuery.append(" AND  ?  BETWEEN fromvalue AND tovalue");
		preparedStatementValues.add(validValue);		
		return selectQuery.toString();
	};
	
	public static String getUniqueAndOverlappingSlabQuery(String tenantId, String feeFactor, Double fromValue, Double toValue,
			String fromDate, String toDate, String tableName, Long id) {
		StringBuffer uniqueQuery = new StringBuffer("SELECT COUNT(*) FROM " + tableName);
		uniqueQuery.append(" WHERE tenantid = '" + tenantId + "' AND LOWER(feefactor) = LOWER('" + feeFactor + "')");
		
		if (id != null)
			uniqueQuery.append(" AND id != "+id );
		
		if (fromValue != null && toValue != null) {
			uniqueQuery.append(" AND " + fromValue + " BETWEEN fromvalue AND tovalue");
			uniqueQuery.append(" AND " + toValue + " BETWEEN fromvalue and tovalue");
		}
		if (fromDate != null && toDate != null) {
			uniqueQuery.append(" AND to_date('" + fromDate + "','dd/MM/yyyy') ");
			uniqueQuery.append(
					" BETWEEN fromdate::date AND  CASE WHEN  todate::date IS NULL THEN fromdate::date ELSE todate::date END");
		}
		return uniqueQuery.toString();
	}
}
