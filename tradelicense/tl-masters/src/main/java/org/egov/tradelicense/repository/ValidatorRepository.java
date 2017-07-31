package org.egov.tradelicense.repository;

import org.egov.models.DocumentType;
import org.egov.tradelicense.repository.builder.UtilityBuilder;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ValidatorRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * This will check whether any record exists with the given tenantId & code
	 * in database or not
	 * 
	 * @param tenantId
	 * @param code
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherRecordExits(String tenantId, String code, String tableName, Long id) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueTenantCodeQuery(tableName, code, tenantId, id);

		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}

	/*public Boolean checkWhetherCategoryExists(SubCategory subCategory) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.CATEGORY_TABLE_NAME;
		Long categoryId = subCategory.getCategoryId();
		String query = UtilityBuilder.getCategoryValidationQuery(tableName, categoryId);
		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;
	}

	public Boolean checkWhetherUomExists(SubCategoryDetail subCategoryDetail) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.UOM_TABLE_NAME;
		String uomId = subCategoryDetail.getUomId();
		String query = UtilityBuilder.getUomValidationQuery(tableName, uomId);
		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;
	}*/

/*	*//**
	 * This will check whether any record exists with the given tenantId & name
	 * in database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @return True / false if record exists / record does n't exists
	 *//*
	public Boolean checkWhetherRecordExitswithName(String tenantId, String name, String tableName, Long id, String applicationName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueTenantCodeQuerywithName(tableName, name, tenantId, applicationName, id);

		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}


	public Boolean checkWhetherDocumentTypeExists(DocumentType documentType) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.DOCUMENT_TYPE_TABLE_NAME;
		String name = documentType.getName();
		String tantentId = documentType.getTenantId();
		String applicationName = documentType.getApplicationType().toString();
		String query = UtilityBuilder.getDocumentTypeValidationQuery(tantentId, name, applicationName, tableName);
		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;
	}*/
}