package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Location;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.querybuilder.MarriageDocumentTypeQueryBuilder;
import org.egov.mr.repository.querybuilder.RegistrationUnitQueryBuilder;
import org.egov.mr.repository.rowmapper.RegistrationUnitRowMapper;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrationUnitRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(MarriageDocumentTypeQueryBuilder.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private RegistrationUnitRowMapper rowMapper;

	@Autowired
	private RegistrationUnitQueryBuilder registrationUnitQueryBuilder;

	public void create(RegistrationUnit registrationUnit) {

		LOGGER.info(registrationUnit.toString());

		Location location = registrationUnit.getAddress();
		AuditDetails auditDetails = registrationUnit.getAuditDetails();
		/**
		 * @Object created according to the registration_unit table structure
		 */
		Object[] obj = new Object[] { registrationUnit.getId(), registrationUnit.getName(),
				registrationUnit.getIsActive(), registrationUnit.getTenantId(), location.getLocality(),
				location.getZone(), location.getRevenueWard(), location.getBlock(), location.getStreet(),
				location.getElectionWard(), location.getDoorNo(), location.getPinCode(),
				registrationUnit.getIsMainRegistrationUnit(), auditDetails.getCreatedBy(),
				auditDetails.getLastModifiedBy(), auditDetails.getCreatedTime(), auditDetails.getLastModifiedTime() };
		try {
			/**
			 * @RegistrationUnit & Location are saved into SingleTable with No
			 *                   address column
			 */
			jdbcTemplate.update(registrationUnitQueryBuilder.getCreateQuery(), obj);
		} catch (DataAccessException e) {
			jdbcTemplate.execute("ROLLBACK");
			e.printStackTrace();
		}
	}

	public List<RegistrationUnit> search(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		LOGGER.info(regnUnitSearchCriteria.toString());
		/**
		 * @search method
		 */
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String selectQuery = registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchCriteria,
				preparedStatementValues);
		List<RegistrationUnit> registrationUnitList = new ArrayList<RegistrationUnit>();
		registrationUnitList = jdbcTemplate.query(selectQuery, preparedStatementValues.toArray(), rowMapper);
		return registrationUnitList;
	}

	public void update(RegistrationUnit registrationUnit) {
		LOGGER.info(registrationUnit.toString());

		Location location = registrationUnit.getAddress();
		AuditDetails auditDetails = registrationUnit.getAuditDetails();
		/**
		 * @Object created according to the registration_unit table structure
		 */
		Object[] obj = new Object[] { registrationUnit.getName(), registrationUnit.getIsActive(),
				location.getLocality(), location.getZone(), location.getRevenueWard(), location.getBlock(),
				location.getStreet(), location.getElectionWard(), location.getDoorNo(), location.getPinCode(),
				registrationUnit.getIsMainRegistrationUnit(), auditDetails.getCreatedBy(),
				auditDetails.getLastModifiedBy(), auditDetails.getCreatedTime(), auditDetails.getLastModifiedTime(),
				registrationUnit.getId(), registrationUnit.getTenantId() };
		try {
			jdbcTemplate.update(registrationUnitQueryBuilder.getUpdateQuery(), obj);
		} catch (DataAccessException e) {
			jdbcTemplate.execute("ROLLBACK");
			e.printStackTrace();
		}
	}

	/**
	 * ****** @new_Id ******
	 * 
	 * @return
	 */
	public Long getIdNextVal() {
		return jdbcTemplate.queryForObject(registrationUnitQueryBuilder.getIdNextValForRegnUnit(), Long.class);
	}

	public Boolean checkIdsAndTenantIdsFromDB(RegistrationUnit registrationUnit) {

		RegistrationUnitSearchCriteria regnUnitSearchIdAndTenantId = new RegistrationUnitSearchCriteria();
		regnUnitSearchIdAndTenantId.setId(registrationUnit.getId());
		regnUnitSearchIdAndTenantId.setTenantId(registrationUnit.getTenantId());

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String query = registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchIdAndTenantId,
				preparedStatementValues);

		try {
			@SuppressWarnings("unused")
			RegistrationUnit regnUnit = jdbcTemplate.queryForObject(query, preparedStatementValues.toArray(),
					rowMapper);
		} catch (DataAccessException e) {
			jdbcTemplate.execute("ROLLBACK");
			return false;
		}
		return true;
	}
}