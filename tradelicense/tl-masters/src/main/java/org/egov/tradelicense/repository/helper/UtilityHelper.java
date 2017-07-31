package org.egov.tradelicense.repository.helper;

import java.util.Date;

import org.egov.models.AuditDetails;
import org.egov.models.CategoryDetail;
import org.egov.models.DocumentType;
import org.egov.models.RequestInfo;
import org.egov.models.UserInfo;
import org.egov.tradelicense.repository.builder.UtilityBuilder;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UtilityHelper {

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
	public Boolean checkWhetherDuplicateRecordExits(String tenantId, String code, String tableName, Long id) {

		Boolean isExists = Boolean.TRUE;
		String query = UtilityBuilder.getUniqueTenantCodeQuery(tableName, code, tenantId, id);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	/**
	 * This will check whether any record exists with the given parentId in
	 * database or not
	 * 
	 * @param tenantId
	 * @param parentId
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherParentRecordExits(Long parentId, String tableName) {

		Boolean isExists = Boolean.TRUE;
		String query = UtilityBuilder.getCategoryParentValidationQuery(tableName, parentId);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	public Boolean checkWhetherDuplicateCategoryDetailRecordExits(CategoryDetail categoryDetail, String tableName,
			Long id) {

		Boolean isExists = Boolean.TRUE;
		Long categoryId = categoryDetail.getCategoryId();
		String feeType = categoryDetail.getFeeType().toString();
		String rateType = categoryDetail.getRateType().toString();
		String query = UtilityBuilder.getCategoryDetailValidationQuery(tableName, categoryId, feeType, rateType, id);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	public Boolean checkWhetherUomExists(CategoryDetail categoryDetail) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.UOM_TABLE_NAME;
		Long uomId = categoryDetail.getUomId();
		String query = UtilityBuilder.getUomValidationQuery(tableName, uomId);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count > 0) {
			isExists = Boolean.TRUE;
		}

		return isExists;
	}

	public AuditDetails getCreateMasterAuditDetals(RequestInfo requestInfo) {

		AuditDetails auditDetails = new AuditDetails();
		Long createdTime = new Date().getTime();
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);
		UserInfo userInfo = requestInfo.getUserInfo();

		if (userInfo != null) {
			auditDetails.setCreatedBy(userInfo.getUsername());
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUsername());
		}

		return auditDetails;
	}

	public Boolean checkWhetherDuplicateFeeMatrixRecordExits(String tenantId, String applicationType, Long categoryId,
			Long subCategoryId, String financialYear, String feeMatrixTableName, Long id) {

		Boolean isExists = Boolean.TRUE;
		String query = UtilityBuilder.getFeeMatrixValidationQuery(feeMatrixTableName, tenantId, applicationType,
				categoryId, subCategoryId, financialYear, id);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}
	
	
	/**
	 * This will check whether any record exists with the given tenantId & name
	 * in database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @return True / false if record exists / record does n't exists
	 */
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
	}
}