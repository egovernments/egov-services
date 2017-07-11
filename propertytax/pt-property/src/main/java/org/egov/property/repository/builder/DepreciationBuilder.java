package org.egov.property.repository.builder;

import org.json.simple.JSONObject;

/**
 * 
 * @author Prasad 
 * This class will have the Depreciation CRU queries
 *
 */
public class DepreciationBuilder {

	public static final String INSERT_DEPRECIATION_QUERY = "insert into egpt_depreciation (tenantId,code,"
			+ "data,createdby,lastmodifiedby,createdtime," + "lastmodifiedtime) VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_DEPRECIATION_QUERY = "UPDATE egpt_depreciation SET tenantId=?,code=?,"
			+ "data=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_DEPRECIATION_CREATETIME = "SELECT  createdTime From egpt_depreciation WHERE id = ?";

	@SuppressWarnings("unchecked")
	public static String searchQuery(String tenantId, Integer[] ids, Integer fromYear, Integer toYear, String code,
			String nameLocal, Integer pageSize, Integer offset) {

		StringBuffer searchDepreciationQuery = new StringBuffer(
				"select * from egpt_depreciation where tenantId ='" + tenantId + "'");

		JSONObject dataSearch = new JSONObject();
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
			searchDepreciationQuery.append(" AND id IN (" + searchIds + ")");

			if (code != null && !code.isEmpty())
				searchDepreciationQuery.append(" AND code = '" + code + "'");

			if (fromYear != null || toYear != null || nameLocal != null) {
				searchDepreciationQuery.append("AND data @> '");
			}
		}

		if (fromYear != null)
			dataSearch.put("fromYear", fromYear);

		if (toYear != null)
			dataSearch.put("toyear", toYear);

		if (nameLocal != null && !nameLocal.isEmpty())
			dataSearch.put("nameLocal", nameLocal);

		if (fromYear != null || toYear != null || nameLocal != null)
			searchDepreciationQuery.append(dataSearch.toJSONString() + "'");

		if (pageSize == null)
			pageSize = 20;

		searchDepreciationQuery.append(" LIMIT " + pageSize);

		if (offset == null)
			offset = 0;

		searchDepreciationQuery.append(" OFFSET " + offset);

		return searchDepreciationQuery.toString();

	}

}
