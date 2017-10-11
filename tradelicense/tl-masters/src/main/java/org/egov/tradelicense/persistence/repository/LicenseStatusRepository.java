package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.LicenseStatusQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search LicenseStatus master
 * 
 * @author Shubham pratap Singh
 *
 */

@Repository
public class LicenseStatusRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create LicenseStatus in database
	 * 
	 * @param statuses
	 * @return LicenseStatusId
	 */
	public Long createLicenseStatus(LicenseStatus statuses) {

		final KeyHolder holder = new GeneratedKeyHolder();
		String insertQueryLicenseStatus = LicenseStatusQueryBuilder.INSERT_LICENSE_STATUS_QUERY;
		AuditDetails auditDetails = statuses.getAuditDetails();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", statuses.getTenantId());
		parameters.addValue("name", statuses.getName());
		parameters.addValue("code", statuses.getCode());
		parameters.addValue("active", statuses.getActive() == null ? true : statuses.getActive());
		parameters.addValue("moduleType", statuses.getModuleType());
		parameters.addValue("createdBy", auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime", auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());

		namedParameterJdbcTemplate.update(insertQueryLicenseStatus, parameters, holder, new String[] { "id" });

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will update LicenseStatus in database
	 * 
	 * @param LicenseStatus
	 * @return LicenseStatus
	 */
	public LicenseStatus updateLicenseStatus(LicenseStatus statuses) {

		Long updatedTime = new Date().getTime();
		String updateQueryLicense = LicenseStatusQueryBuilder.UPDATE_LICENSE_STATUS_QUERY;
		AuditDetails auditDetails = statuses.getAuditDetails();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", statuses.getTenantId());
		parameters.addValue("code", statuses.getCode());
		parameters.addValue("name", statuses.getName());
		parameters.addValue("moduleType", statuses.getModuleType());
		parameters.addValue("active", statuses.getActive() == null ? true : statuses.getActive());
		parameters.addValue("lastModifiedBy", auditDetails == null ? null : auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime", updatedTime);
		parameters.addValue("id", statuses.getId());
		namedParameterJdbcTemplate.update(updateQueryLicense, parameters);

		return statuses;

	}

	/**
	 * Description : This method for search LicenseStatus
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * 
	 * @return List<LicenseStatus>
	 */
	public List<LicenseStatus> searchLicenseStatus(String tenantId, Integer[] ids, String[] codes, String name, 
			String moduleType, String active, Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String searchQueryLicense = LicenseStatusQueryBuilder.buildSearchQuery(tenantId, ids, codes, name,  moduleType, active,
				pageSize, offSet, parameters);
		List<LicenseStatus> licenseStatuses = getLicenseStatusSearchQuery(searchQueryLicense.toString(), parameters);

		return licenseStatuses;
	}

	/**
	 * This method will execute the given query & will build the LicenseStatus
	 * objects
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link LicenseStatus} List of LicenseStatus
	 */
	private List<LicenseStatus> getLicenseStatusSearchQuery(String query, MapSqlParameterSource parameter) {

		List<LicenseStatus> statuses = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameter);
		for (Map<String, Object> row : rows) {

			LicenseStatus status = new LicenseStatus();
			status.setId(getLong(row.get("id")));
			status.setTenantId(getString(row.get("tenantid")));
			status.setCode(getString(row.get("code")));
			status.setName(getString(row.get("name")));
			status.setActive((Boolean) row.get("active"));
			status.setModuleType(getString(row.get("moduleType")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			status.setAuditDetails(auditDetails);

			statuses.add(status);
		}

		return statuses;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? null : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}

}
