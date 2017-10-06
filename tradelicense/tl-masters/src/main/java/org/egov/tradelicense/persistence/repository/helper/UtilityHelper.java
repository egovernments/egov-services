package org.egov.tradelicense.persistence.repository.helper;

import java.util.Date;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UtilityHelper {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * This will check whether any record exists with the given tenantId & code
	 * in database or not
	 * 
	 * @param tenantId
	 * @param code
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherDuplicateRecordExits(String tenantId, String code, String name, String tableName,
			Long id, String categorySubCategoryType) {

		Boolean isExists = Boolean.TRUE;
		String query;
		int count = 0;

		if (code != null) {
			query = UtilityBuilder.getUniqueTenantCodeQuery(tableName, code, tenantId, id);

			if (categorySubCategoryType != null && !categorySubCategoryType.isEmpty()) {
				if (categorySubCategoryType.equals(ConstantUtility.SUB_CATEGORY_TYPE)) {
					query = query + " AND parent IS NOT NULL";
				} else if (categorySubCategoryType.equals(ConstantUtility.CATEGORY_TYPE)) {
					query = query + " AND parent IS NULL";
				}
			} 

			try {

				MapSqlParameterSource parameters = new MapSqlParameterSource();
				count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

			} catch (Exception e) {
				log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
			}

			if (count == 0) {
				isExists = Boolean.FALSE;
			}
		}
		if (name != null) {

			query = UtilityBuilder.getUniqueTenantNameQuery(tableName, name, tenantId, id);

			if (categorySubCategoryType != null && !categorySubCategoryType.isEmpty()) {
				if (categorySubCategoryType.equals(ConstantUtility.SUB_CATEGORY_TYPE)) {
					query = query + " AND parent IS NOT NULL";
				} else if (categorySubCategoryType.equals(ConstantUtility.CATEGORY_TYPE)) {
					query = query + " AND parent IS NULL";
				}
			}

			isExists = Boolean.TRUE;
			try {

				MapSqlParameterSource parameters = new MapSqlParameterSource();
				count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

			} catch (Exception e) {
				log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
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
		if (auditDetails == null) {
			auditDetails = new AuditDetails();
		}
		auditDetails.setLastModifiedTime(updatedTime);
		UserInfo userInfo = requestInfo.getUserInfo();

		if (userInfo != null && userInfo.getId() != null) {
			auditDetails.setLastModifiedBy(userInfo.getId().toString());
		}

		return auditDetails;
	}

	/**
	 * This will check whether any record exists with the given tenantId , name
	 * & applicationName in database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @param tableName
	 * @param id
	 * @param applicationName
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkDocumentTypeDuplicate(String tenantId, String name, String tableName, Long id,
			String applicationName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueTenantCodeQuerywithName(tableName, name, tenantId, applicationName, id);

		int count = 0;

		try {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {

			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

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

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
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
	public Boolean checkWhetherLicenseStatusExists(String tenantId, String name, String code, Long id,
			String tableName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueLicenseStatusValidationQuery(tenantId, name, code, id, tableName);

		int count = 0;

		try {

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {

			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

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
	public Boolean checkWhetherLicenseStatusDuplicateWithModuleType(String tenantId, String code, String moduleType,
			String tableName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueLicenseStatusValidationQuerywithModuleType(tenantId, code, moduleType,
				tableName);

		int count = 0;

		try {

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {

			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}

	/**
	 * This will check whether any record exists with the given tenantId ,code &
	 * name exists in database or not
	 * 
	 * @param tenantId
	 * @param code
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherDuplicateUomRecordExits(String tenantId, String code, String name, String tableName,
			Long id) {

		Boolean isExists = Boolean.TRUE;
		String query;
		int count = 0;

		query = UtilityBuilder.getUniqueTenantNameCodeQuery(tableName, code, name, tenantId, id);

		try {

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	public boolean checkIfSubCategoryInActive(Long id, String tableName) {

		boolean isSubCategoryInactive = true;

		String query;
		int count = 0;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		query = UtilityBuilder.getQueryTocheckSubCatInactive(id, tableName, parameters);
		try {
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			isSubCategoryInactive = false;
		}

		return isSubCategoryInactive;
	}
}