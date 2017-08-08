package org.egov.tradelicense.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.models.AuditDetails;
import org.egov.models.LicenseStatus;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.LicenseStatusQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
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
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create LicenseStatus in database
	 * 
	 * @param licenseStatus
	 * @return LicenseStatusId
	 */
	public Long createLicenseStatus(LicenseStatus licenseStatus) {

		String LicenseStatusInsert = LicenseStatusQueryBuilder.INSERT_LICENSE_STATUS_QUERY;
		AuditDetails auditDetails = licenseStatus.getAuditDetails();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(LicenseStatusInsert, new String[] { "id" });

				ps.setString(1, licenseStatus.getTenantId());
				ps.setString(2, licenseStatus.getName());
				ps.setString(3, licenseStatus.getCode());
				if (licenseStatus.getActive() != null) {
					ps.setBoolean(4, licenseStatus.getActive());
				} else {
					ps.setBoolean(4, true);
				}
				ps.setString(5, auditDetails.getCreatedBy());
				ps.setString(6, auditDetails.getLastModifiedBy());
				ps.setLong(7, auditDetails.getCreatedTime());
				ps.setLong(8, auditDetails.getLastModifiedTime());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will update LicenseStatus in database
	 * 
	 * @param LicenseStatus
	 * @return LicenseStatus
	 */
	public LicenseStatus updateLicenseStatus(LicenseStatus licenseStatus) {

		Long updatedTime = new Date().getTime();
		String LicenseStatusUpdateSql = LicenseStatusQueryBuilder.UPDATE_LICENSE_STATUS_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(LicenseStatusUpdateSql);

				ps.setString(1, licenseStatus.getTenantId());
				ps.setString(2, licenseStatus.getCode());
				ps.setString(3, licenseStatus.getName());
				if (licenseStatus.getActive() != null) {
					ps.setBoolean(4, licenseStatus.getActive());
				} else {
					ps.setBoolean(4, true);
				}
				ps.setString(5, licenseStatus.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, updatedTime);
				ps.setLong(7, licenseStatus.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
		return licenseStatus;
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
	 * @return List<LicenseStatus>
	 */
	public List<LicenseStatus> searchLicenseStatus(String tenantId, Integer[] ids, String name, String code,
			String active, Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<>();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String LicenseStatusSearchQuery = LicenseStatusQueryBuilder.buildSearchQuery(tenantId, ids, name, code, active,
				pageSize, offSet, preparedStatementValues);
		List<LicenseStatus> LicenseStatuslst = getLicenseStatusSearchQuery(LicenseStatusSearchQuery.toString(),
				preparedStatementValues);

		return LicenseStatuslst;
	}

	/**
	 * This method will execute the given query & will build the LicenseStatus
	 * objects
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link LicenseStatus} List of LicenseStatus
	 */
	private List<LicenseStatus> getLicenseStatusSearchQuery(String query, List<Object> preparedStatementValues) {

		List<LicenseStatus> LicenseStatuslst = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		for (Map<String, Object> row : rows) {

			LicenseStatus licenseStatus = new LicenseStatus();
			licenseStatus.setId(getLong(row.get("id")));
			licenseStatus.setTenantId(getString(row.get("tenantid")));
			licenseStatus.setCode(getString(row.get("code")));
			licenseStatus.setName(getString(row.get("name")));
			licenseStatus.setActive((Boolean) row.get("active"));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			licenseStatus.setAuditDetails(auditDetails);

			LicenseStatuslst.add(licenseStatus);
		}

		return LicenseStatuslst;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
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
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

}
