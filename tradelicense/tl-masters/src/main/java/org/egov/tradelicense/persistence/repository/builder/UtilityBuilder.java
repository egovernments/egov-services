package org.egov.tradelicense.persistence.repository.builder;

/**
 * This class will have all the common queries which will be used in the
 * tradelicense master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public class UtilityBuilder {

	public static String getUniqueTenantCodeQuery(String tableName, String code, String tenantId, Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where LOWER(code) = '" + code.toLowerCase() + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();
	}

	public static String getUniqueTenantNameQuery(String tableName, String name, String tenantId, Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where LOWER(name) = '" + name.toLowerCase() + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();
	}

	public static String getCategoryParentValidationQuery(String tableName, Long parentId) {

		StringBuffer categoryParentValidationQuery = new StringBuffer("select count(*) from " + tableName);
		categoryParentValidationQuery.append(" where id = '" + parentId + "'");

		return categoryParentValidationQuery.toString();
	}

	public static String getCategoryDetailValidationQuery(String tableName, Long categoryId, String feeType,
			String rateType, Long id) {

		StringBuffer categoryDetailValidationQuery = new StringBuffer(
				"select count(*) from " + tableName + " where 1=1 ");

		if (categoryId != null) {
			categoryDetailValidationQuery.append(" AND categoryId = '" + categoryId + "'");
		}

		if (feeType != null && !feeType.isEmpty()) {
			categoryDetailValidationQuery.append(" AND feeType = '" + feeType + "'");
		}

		// if (rateType != null && !rateType.isEmpty()) {
		// categoryDetailValidationQuery.append(" AND rateType = '" + rateType +
		// "'");
		// }

		if (id != null) {
			categoryDetailValidationQuery.append(" AND id !=" + id);
		}

		return categoryDetailValidationQuery.toString();
	}

	public static String getFeeMatrixValidationQuery(String tableName, String tenantId, String applicationType,
			Long categoryId, Long subCategoryId, String financialYear, Long id) {

		StringBuffer feeMatrixValidationQuery = new StringBuffer("select count(*) from " + tableName + " where 1=1 ");

		if (tenantId != null) {
			feeMatrixValidationQuery.append(" AND tenantId = '" + tenantId + "'");
		}
		if (applicationType != null && !applicationType.isEmpty()) {
			feeMatrixValidationQuery.append(" AND applicationType = '" + applicationType + "'");
		}
		if (categoryId != null) {
			feeMatrixValidationQuery.append(" AND categoryId = '" + categoryId + "'");
		}
		if (subCategoryId != null) {
			feeMatrixValidationQuery.append(" AND subCategoryId = '" + subCategoryId + "'");
		}
		if (financialYear != null) {
			feeMatrixValidationQuery.append(" AND financialYear = '" + financialYear + "'");
		}
		if (id != null) {
			feeMatrixValidationQuery.append(" AND id !=" + id);
		}

		return feeMatrixValidationQuery.toString();
	}

	public static String getUomValidationQuery(String tableName, Long uomId) {

		StringBuffer uomValidationQuery = new StringBuffer("select count(*) from " + tableName);
		uomValidationQuery.append(" where id = '" + uomId + "'");

		return uomValidationQuery.toString();
	}

	public static String getUniqueTenantCodeQuerywithName(String tableName, String name, String tenantId,
			String applicationType, Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where LOWER(name) = '" + name.toLowerCase() + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		uniqueQuery.append(" AND applicationType = '" + applicationType + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();
	}

	public static String getCategoryIdValidationQuery(Long categoryId, String tableName) {

		StringBuffer categoryValidationQuery = new StringBuffer("select count(*) from " + tableName);
		categoryValidationQuery.append(" where id = '" + categoryId + "'");

		return categoryValidationQuery.toString();
	}

	public static String getUniqueLicenseStatusValidationQuery(String tenantId, String name, String code, Long id,
			String tableName) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where LOWER(name) = '" + name.toLowerCase() + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		uniqueQuery.append(" AND LOWER(code) = '" + code.toLowerCase() + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();
	}

	public static String getUniqueTenantNameCodeQuery(String tableName, String code, String name, String tenantId,
			Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where LOWER(name) = '" + name.toLowerCase() + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		uniqueQuery.append(" AND LOWER(code) = '" + code.toLowerCase() + "'");
		if (id != null) {
			uniqueQuery.append(" AND id !=" + id);
		}

		return uniqueQuery.toString();
	}
}