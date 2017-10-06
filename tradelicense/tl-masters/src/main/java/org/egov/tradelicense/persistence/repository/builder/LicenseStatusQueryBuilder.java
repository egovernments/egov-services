package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class LicenseStatusQueryBuilder {

	private static final String licenseStatusTableName = ConstantUtility.LICENSE_STATUS_TABLE_NAME;

	public static final String INSERT_LICENSE_STATUS_QUERY = "INSERT INTO " + licenseStatusTableName
			+ " (tenantId, name, code, active, createdBy, moduleType, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId, :name, :code, :active, :createdBy, :moduleType, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_LICENSE_STATUS_QUERY = "UPDATE " + licenseStatusTableName
			+ " SET tenantId = :tenantId, code = :code, name = :name, active = :active,"
			+ " moduleType = :moduleType ,lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String[] codes,  String name, String moduleType ,String active,
			Integer pageSize, Integer offSet, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + licenseStatusTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId",tenantId);

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
		
		if (codes != null && codes.length > 0) {

			String serachCodes = "";
			int count = 1;
			for (String code : codes) {

				if (count < codes.length)
					serachCodes = serachCodes + "'" + code.toUpperCase() + "',";
				else
					serachCodes = serachCodes + "'" + code.toUpperCase() + "'";

				count++;
			}
			searchSql.append(" AND upper(code) IN (" + serachCodes + ") ");
		}
		
		
		if (moduleType != null && !moduleType.isEmpty()) {
			searchSql.append(" AND moduleType = :moduleType ");
			parameters.addValue("moduleType", moduleType);
		}

		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name = :name ");
			parameters.addValue("name",name);
		}

		if (active != null) {

			if (active.equalsIgnoreCase("False")) {
				searchSql.append(" AND active = :active ");
				parameters.addValue("active",false);
			}

			else if (active.equalsIgnoreCase("True")) {
				searchSql.append(" AND active = :active ");
				parameters.addValue("active",true);
			}

		}

		if (pageSize != null) {
			searchSql.append(" limit :pageSize ");
			parameters.addValue("pageSize" , pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset :offset ");
			parameters.addValue("offset" , offSet);
		}

		return searchSql.toString();

	}

}
