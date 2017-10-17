package org.egov.property.repository.builder;

import java.util.List;

import org.egov.property.utility.ConstantUtility;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Prasad
 * 
 *         This Class will from the search query for all the master services
 *         ,based on the given parameters
 *
 */

public class SearchMasterBuilder {

	@Autowired
	Environment environment;

	/**
	 * <p>
	 * This method will form the search query based on the given parameters
	 * </p>
	 * 
	 * @param tableName
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param nameLocal
	 * @param code
	 * @param active
	 * @param isResidential
	 * @param orderNumber
	 * @param category
	 * @param pageSize
	 * @param offSet
	 * @param preparedStatementValues
	 * @param fromYear
	 * @param toYear
	 * @param parent
	 * @return {@link String} search query
	 */
	@SuppressWarnings("unchecked")
	public static String buildSearchQuery(String tableName, String tenantId, Integer[] ids, String name,
			String nameLocal, String code, Boolean active, Boolean isResidential, Integer orderNumber, String category,
			Integer pageSize, Integer offSet, List<Object> preparedStatementValues, Integer fromYear, Integer toYear,
			Integer year, String parent, String[] service) {

		StringBuffer searchSql = new StringBuffer();
		String defaultService = "common";// TODO read from property file

		searchSql.append("select * from " + tableName + " where ");

		searchSql.append("tenantId =? ");
		preparedStatementValues.add(tenantId);

		if (ids != null && ids.length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : ids) {

				if (count < ids.length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			searchSql.append("AND id IN (" + searchIds + ") ");
		}

		if (code != null && !code.isEmpty()) {
			searchSql.append("AND code =? ");
			preparedStatementValues.add(code);
		}

		if (parent != null && !parent.isEmpty()) {
			searchSql.append("AND parent =? ");
			preparedStatementValues.add(parent);
		} else {
			if (tableName.equalsIgnoreCase(ConstantUtility.USAGE_TYPE_TABLE_NAME)
					|| tableName.equalsIgnoreCase(ConstantUtility.PROPERTY_TYPE_TABLE_NAME)) {
				searchSql.append("AND parent IS NULL ");
			}
		}

		if (tableName.equalsIgnoreCase(ConstantUtility.USAGE_TYPE_TABLE_NAME)) {
			String queryForService = getSearchQueryForStrings(service);
			searchSql.append("AND lower(service) IN (" + queryForService);
		}

		JSONObject dataSearch = new JSONObject();

		if (name != null || nameLocal != null || active != null || isResidential != null || orderNumber != null
				|| category != null || fromYear != null || toYear != null)
			searchSql.append("AND data @> ?::jsonb ");

		if (name != null && !name.isEmpty())
			dataSearch.put("name", name);

		if (nameLocal != null && !nameLocal.isEmpty())
			dataSearch.put("nameLocal", nameLocal);

		if (active != null)
			dataSearch.put("active", active);

		if (isResidential != null)
			dataSearch.put("isResidential", isResidential);

		if (orderNumber != null)
			dataSearch.put("orderNumber", orderNumber);

		if (category != null && !category.isEmpty())
			dataSearch.put("category", category);

		if (year != null) {

			preparedStatementValues.add(year);
			searchSql.append(" AND ?  BETWEEN (data::json->>'fromYear')::int AND (data::json->>'toyear')::int ");
		} else {
			if (fromYear != null)
				dataSearch.put("fromYear", fromYear);

			if (toYear != null)
				dataSearch.put("toyear", toYear);
		}

		if (name != null || nameLocal != null || active != null || isResidential != null || orderNumber != null
				|| category != null || fromYear != null || toYear != null)
			preparedStatementValues.add(dataSearch.toJSONString());

		if (pageSize == null)
			pageSize = 30;

		searchSql.append("limit ? ");
		preparedStatementValues.add(pageSize);

		if (offSet == null)
			offSet = 0;

		searchSql.append("offset ? ");
		preparedStatementValues.add(offSet);

		return searchSql.toString();

	}

	private static String getSearchQueryForStrings(String[] service) {

		StringBuilder query = new StringBuilder();
		if (service != null) {

			String[] list = service;
			query.append("'" + list[0].toLowerCase() + "'");
			for (int i = 1; i < service.length; i++) {
				query.append("," + "'" + list[i].toLowerCase() + "'");
			}
		}
		return query.append(")").toString();
	}

	@SuppressWarnings("unchecked")
	public static String apartmentSearchQuery(String tableName, String tenantId, Integer[] ids, String name,
			String code, Boolean liftFacility, Boolean powerBackUp, Boolean parkingFacility, Integer pageSize,
			Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + tableName + " where ");
		searchSql.append(" tenantId = ? ");
		preparedStatementValues.add(tenantId);

		if (ids != null && ids.length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : ids) {

				if (count < ids.length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;
				count++;
			}
			searchSql.append(" AND id IN (" + searchIds + ") ");
		}

		if (code != null && !code.isEmpty()) {
			searchSql.append(" AND code =? ");
			preparedStatementValues.add(code);
		}
		JSONObject dataSearch = new JSONObject();

		if (name != null || liftFacility != null || powerBackUp != null || parkingFacility != null)
			searchSql.append(" AND data @> ?::jsonb");
		if (name != null && !name.isEmpty())
			dataSearch.put("name", name);
		if (liftFacility != null)
			dataSearch.put("liftFacility", liftFacility);
		if (powerBackUp != null)
			dataSearch.put("powerBackUp", powerBackUp);
		if (parkingFacility != null)
			dataSearch.put("parkingFacility", parkingFacility);
		if (name != null || liftFacility != null || powerBackUp != null || parkingFacility != null)
			preparedStatementValues.add(dataSearch.toJSONString());
		if (pageSize == null)
			pageSize = 30;
		searchSql.append(" limit ? ");
		preparedStatementValues.add(pageSize);

		if (offSet == null)
			offSet = 0;

		searchSql.append(" offset ? ");
		preparedStatementValues.add(offSet);

		return searchSql.toString();
	}
}
