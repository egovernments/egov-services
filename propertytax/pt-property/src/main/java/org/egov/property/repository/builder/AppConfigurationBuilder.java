package org.egov.property.repository.builder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.property.utility.ConstantUtility;
import org.egov.property.utility.TimeStampUtil;

public class AppConfigurationBuilder {

	public static final String INSERT_APPCONFIGURATION_QUERY = "insert into " + ConstantUtility.CONFIGURATION_TABLE_NAME
			+ " (tenantId,keyName,description,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_APPCONFIGURATION_QUERY = "UPDATE " + ConstantUtility.CONFIGURATION_TABLE_NAME
			+ " SET tenantId=?,keyName=?,description=?," + "lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String INSERT_APPCONFIGURATION_VALUE_QUERY = "insert into "
			+ ConstantUtility.CONFIGURATION_VALUES_TABLE_NAME
			+ " (tenantId,keyId,value,effectiveFrom,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?,?)";

	public static final String UPDATE_APPCONFIGURATION_VALUE_QUERY = "UPDATE "
			+ ConstantUtility.CONFIGURATION_VALUES_TABLE_NAME
			+ " SET tenantId=?,value=?,effectiveFrom=?,lastmodifiedby=?," + "lastmodifiedtime=? WHERE keyId = ?";

	private static final String BASE_QUERY = "SELECT ck.*, cv.keyId, cv.value, cv.effectiveFrom" + " FROM "
			+ ConstantUtility.CONFIGURATION_TABLE_NAME + " ck JOIN " + ConstantUtility.CONFIGURATION_VALUES_TABLE_NAME
			+ " cv ON ck.id = cv.keyId ";

	public static String getAppConfigurationSearchQuery(String tenantId, Long[] ids, String keyName,
			String effectiveFrom, Integer pageSize, Integer offset, List<Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, tenantId, ids, keyName, effectiveFrom, pageSize, offset);
		System.out.println("Query : " + selectQuery);
		return selectQuery.toString();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addWhereClause(StringBuilder selectQuery, List preparedStatementValues, String tenantId,
			Long[] ids, String keyName, String effectiveFrom, Integer pageSize, Integer offset) {

		if (ids == null && effectiveFrom == null && keyName == null && tenantId == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (tenantId != null) {
			isAppendAndClause = true;
			selectQuery.append(" ck.tenantId = ?");
			preparedStatementValues.add(tenantId);
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" cv.tenantId = ?");
			preparedStatementValues.add(tenantId);
		}

		if (ids != null) {
			if (ids.length > 0) {

				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ck.id IN " + getIdQuery(ids));
			}
		}

		if (keyName != null) {
			if (!keyName.isEmpty()) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" LOWER(ck.keyName) = LOWER(?)");
				preparedStatementValues.add(keyName);
			}
		}

		if (effectiveFrom != null) {
			if (!effectiveFrom.isEmpty()) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				Date[] dates = new Date[2];
				dates = constructDateRange(TimeStampUtil.getTimeStamp(effectiveFrom),
						TimeStampUtil.getTimeStamp(effectiveFrom));
				selectQuery.append(
						" (cv.effectiveFrom < to_date(?,'dd/MM/yyyy') or cv.effectiveFrom between ? and ?) order by effectiveFrom asc");
				preparedStatementValues.add(effectiveFrom);
				preparedStatementValues.add(dates[0]);
				preparedStatementValues.add(dates[1]);
			}

		}

		selectQuery.append(" limit ? ");
		preparedStatementValues.add(pageSize);
		selectQuery.append("offset ? ");
		preparedStatementValues.add(offset);

	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	// looping long ids
	private static String getIdQuery(Long[] idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList != null) {
			query.append(idList[0].toString());
			for (int i = 1; i < idList.length; i++) {
				query.append(", " + idList[i]);
			}
		}
		return query.append(")").toString();
	}

	// date construction
	public static Date[] constructDateRange(Date fromDate, Date toDate) {
		Date[] dates = new Date[2];
		Calendar calfrom = Calendar.getInstance();
		calfrom.setTime(fromDate);
		calfrom.set(Calendar.HOUR, 0);
		calfrom.set(Calendar.MINUTE, 0);
		calfrom.set(Calendar.SECOND, 0);
		calfrom.set(Calendar.AM_PM, Calendar.AM);
		dates[0] = calfrom.getTime();
		Calendar calto = Calendar.getInstance();
		calto.setTime(toDate);
		calto.set(Calendar.HOUR, 0);
		calto.set(Calendar.MINUTE, 0);
		calto.set(Calendar.SECOND, 0);
		calto.add(Calendar.DAY_OF_MONTH, 1);
		dates[1] = calto.getTime();
		return dates;
	}

	public static final String DELETE_BY_VALUE = "delete from " + ConstantUtility.CONFIGURATION_VALUES_TABLE_NAME
			+ " where value= ? ";

	public static final String SELECT_BY_KEYID = "select value from " + ConstantUtility.CONFIGURATION_VALUES_TABLE_NAME
			+ " where keyid= ?";

}
