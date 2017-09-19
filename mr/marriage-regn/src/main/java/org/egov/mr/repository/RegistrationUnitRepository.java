package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.PreparedStatementSetter;
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
		try {
			/**
			 * @RegistrationUnit_and_Location_are_saved_into
			 * @SingleTable @with_No_address_column
			 */
			jdbcTemplate.update(registrationUnitQueryBuilder.getCreateQuery(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					/**
					 * @Object_created_according_to_the
					 * @Registration_unit_table_structure
					 */
					ps.setLong(1, registrationUnit.getId());
					ps.setString(2, registrationUnit.getName());
					ps.setBoolean(3, registrationUnit.getIsActive());
					ps.setString(4, registrationUnit.getTenantId());
					ps.setLong(5, location.getLocality());
					ps.setLong(6, location.getZone());
					ps.setLong(7, location.getRevenueWard());
					ps.setLong(8, location.getBlock());
					ps.setLong(9, location.getStreet());
					ps.setLong(10, location.getElectionWard());
					ps.setString(11, location.getDoorNo());
					ps.setLong(12, location.getPinCode());
					ps.setBoolean(13, registrationUnit.getIsMainRegistrationUnit());
					ps.setString(14, auditDetails.getCreatedBy());
					ps.setString(15, auditDetails.getLastModifiedBy());
					ps.setLong(16, auditDetails.getCreatedTime());
					ps.setLong(17, auditDetails.getLastModifiedTime());
				}
			});
		} catch (DataAccessException e) {
			jdbcTemplate.execute("ROLLBACK");
			e.printStackTrace();
		}
	}

	public List<RegistrationUnit> search(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		LOGGER.info(regnUnitSearchCriteria.toString());
		/**
		 * @Search_method
		 */
		String selectQuery = registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchCriteria);
		List<RegistrationUnit> registrationUnitList = new ArrayList<RegistrationUnit>();
		registrationUnitList = jdbcTemplate.query(selectQuery, rowMapper);
		return registrationUnitList;
	}

	public void update(RegistrationUnit registrationUnit) {
		LOGGER.info(registrationUnit.toString());

		Location location = registrationUnit.getAddress();
		AuditDetails auditDetails = registrationUnit.getAuditDetails();
		/**
		 * @RegistrationUnit_and_Location_are_saved_into
		 * @SingleTable @with_No_address_column
		 */
		try {
			jdbcTemplate.update(registrationUnitQueryBuilder.getUpdateQuery(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					/**
					 * @Object_created_according_to_the
					 * @Registration_unit_table_structure
					 */
					ps.setString(1, registrationUnit.getName());
					ps.setBoolean(2, registrationUnit.getIsActive());
					ps.setLong(3, location.getLocality());
					ps.setLong(4, location.getZone());
					ps.setLong(5, location.getRevenueWard());
					ps.setLong(6, location.getBlock());
					ps.setLong(7, location.getStreet());
					ps.setLong(8, location.getElectionWard());
					ps.setString(9, location.getDoorNo());
					ps.setLong(10, location.getPinCode());
					ps.setBoolean(11, registrationUnit.getIsMainRegistrationUnit());
					ps.setString(12, auditDetails.getCreatedBy());
					ps.setString(13, auditDetails.getLastModifiedBy());
					ps.setLong(14, auditDetails.getCreatedTime());
					ps.setLong(15, auditDetails.getLastModifiedTime());
					ps.setLong(16, registrationUnit.getId());
					ps.setString(17, registrationUnit.getTenantId());
				}
			});
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

		String query = registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchIdAndTenantId);

		try {
			@SuppressWarnings("unused")
			RegistrationUnit regnUnit = jdbcTemplate.queryForObject(query, rowMapper);
		} catch (DataAccessException e) {
			jdbcTemplate.execute("ROLLBACK");
			return false;
		}
		return true;
	}
}