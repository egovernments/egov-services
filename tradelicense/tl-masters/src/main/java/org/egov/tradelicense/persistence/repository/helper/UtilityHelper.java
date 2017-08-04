package org.egov.tradelicense.persistence.repository.helper;

import java.util.Date;

import org.egov.models.AuditDetails;
import org.egov.models.RequestInfo;
import org.egov.models.UserInfo;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
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
	public Boolean checkWhetherDuplicateRecordExits(String tenantId, String code, String name, String tableName, Long id) {

		Boolean isExists = Boolean.TRUE;
		String query ;
		int count = 0;
		
		if( code != null){
			query= UtilityBuilder.getUniqueTenantCodeQuery(tableName, code, tenantId, id);
			

			try {
				count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			if (count == 0) {
				isExists = Boolean.FALSE;
			}
		}
		if(  name != null){
			
			query = UtilityBuilder.getUniqueTenantNameQuery(tableName, name, tenantId, id);
			isExists = Boolean.TRUE;
			try {
				count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			if (count == 0) {
				isExists = Boolean.FALSE;
			}
		}

		return isExists;
	}
	/**
	 * This will generate the audit details for the creation of master services
	 * 
	 * @param RequestInfo
	 * @return AuditDetails
	 */
	public AuditDetails getCreateMasterAuditDetails(RequestInfo requestInfo) {

		AuditDetails auditDetails = new AuditDetails();
		Long createdTime = new Date().getTime();
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);
		UserInfo userInfo = requestInfo.getUserInfo();

		if (userInfo != null && userInfo.getId() != null) {
			auditDetails.setCreatedBy(userInfo.getId().toString());
			auditDetails.setLastModifiedBy(userInfo.getId().toString());
		}

		return auditDetails;
	}

	/**
	 * This will generate the audit details for the update master services
	 * 
	 * @param AuditDetails
	 * @param RequestInfo
	 * @return AuditDetails
	 */
	public AuditDetails getUpdateMasterAuditDetails(AuditDetails auditDetails, RequestInfo requestInfo) {

		Long updatedTime = new Date().getTime();
		auditDetails.setLastModifiedTime(updatedTime);
		UserInfo userInfo = requestInfo.getUserInfo();

		if (userInfo != null && userInfo.getId() != null) {
			auditDetails.setLastModifiedBy(userInfo.getId().toString());
		}

		return auditDetails;
	}

	/**
	 * This will check whether any duplicate fee Matrix record exists or not
	 * 
	 * @param tenantId
	 * @param applicationType
	 * @param categoryId
	 * @param subCategoryId
	 * @param financialYear
	 * @param feeMatrixTableName
	 * @param id
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
	public Boolean checkWhetherLicenseStatusExists(String tenantId, String name, String code, Long id, String tableName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueLicenseStatusValidationQuery(tenantId, name, code, id, tableName);

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
}