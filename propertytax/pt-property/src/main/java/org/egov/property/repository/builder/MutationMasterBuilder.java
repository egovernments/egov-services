package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;
import org.json.simple.JSONObject;

/**
 * This class will have the mutation master create,search & update Queries
 * 
 * @author Prasad
 *
 */
public class MutationMasterBuilder {

	public static final String INSERT_MUTATTION_QUERY = "insert into " + ConstantUtility.MUTATION_MASTER_TABLE_NAME
			+ " (tenantId,code,name,data,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?,?)";

	public static final String UPDATE_MUTATION_QUERY = "UPDATE " + ConstantUtility.MUTATION_MASTER_TABLE_NAME
			+ " SET tenantId=?,code=?," + "name=?,data=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_MUTATION_CREATETIME = "SELECT createdTime From "
			+ ConstantUtility.MUTATION_MASTER_TABLE_NAME + " WHERE id = ?";

	@SuppressWarnings("unchecked")
	public static String searchQuery(String tenantId, Integer[] ids, String code, String name, String nameLocal,
			Integer pageSize, Integer offset) {

		StringBuffer searchDepreciationQuery = new StringBuffer(
				"select * from " + ConstantUtility.MUTATION_MASTER_TABLE_NAME + " where tenantId ='" + tenantId + "'");

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

			if (nameLocal != null || name != null) {
				searchDepreciationQuery.append("AND data @> '");
			}
		}

		if (nameLocal != null && !nameLocal.isEmpty())
			dataSearch.put("nameLocal", nameLocal);

		if (name != null && !name.isEmpty())
			dataSearch.put("name", name);

		if (nameLocal != null || name != null)
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
