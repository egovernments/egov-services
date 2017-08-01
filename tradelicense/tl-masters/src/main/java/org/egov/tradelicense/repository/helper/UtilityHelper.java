package org.egov.tradelicense.repository.helper;

import java.util.Date;

import org.egov.models.AuditDetails;
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
	 * This will generate the audit details for the creation of master services
	 * 
	 * @param RequestInfo
	 * @return AuditDetails
	 */
	public AuditDetails getCreateMasterAuditDetals(RequestInfo requestInfo) {

		AuditDetails auditDetails = new AuditDetails();
		Long createdTime = new Date().getTime();
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);
		UserInfo userInfo = requestInfo.getUserInfo();

		if (userInfo != null) {
			if (userInfo.getId() != null) {
				auditDetails.setCreatedBy(userInfo.getId().toString());
				auditDetails.setLastModifiedBy(userInfo.getId().toString());
			}
		}

		return auditDetails;
	}

	/**
	 * This will check whether any record exists with the given tenantId & name
	 * in database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @return True / false if record exists / record does n't exists
	 */
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
	public Boolean checkWhetherRecordExitswithName(String tenantId, String name, String tableName, Long id,
			String applicationName) {

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