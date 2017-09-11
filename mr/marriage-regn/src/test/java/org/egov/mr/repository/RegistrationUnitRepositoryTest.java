package org.egov.mr.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Location;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.querybuilder.RegistrationUnitQueryBuilder;
import org.egov.mr.repository.rowmapper.RegistrationUnitRowMapper;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationUnitRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private RegistrationUnitRowMapper rowMapper;

	@Mock
	private RegistrationUnitQueryBuilder registrationUnitQueryBuilder;

	@InjectMocks
	private RegistrationUnitRepository registrationUnitRepository;

	@Test
	public void testCreateRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();
		when(registrationUnitQueryBuilder.getCreateQuery()).thenReturn(getQueryGenerator());

		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.create(registrationUnit);
	}

	/**
	 * no exception is thrown
	 */
	@Test
	public void testExceptionForCreateRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();
		when(registrationUnitQueryBuilder.getCreateQuery()).thenReturn(getQueryGenerator());
		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.create(registrationUnit);
	}

	@Test
	public void testSearchRegistrationUnit() {
		RegistrationUnitSearchCriteria registrationUnitSearchCriteria = new RegistrationUnitSearchCriteria();
		registrationUnitSearchCriteria.setId(Long.valueOf("2"));
		registrationUnitSearchCriteria.setTenantId("ap.kurnool");

		List<Object> preparedStatementValues = new ArrayList<Object>();

		when(registrationUnitQueryBuilder.getSelectQuery(any(RegistrationUnitSearchCriteria.class)))
				.thenReturn(getBASEQUERY());

		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.search(registrationUnitSearchCriteria);
	}

	@Test
	public void testExceptionForSearchRegistrationUnit() {
		RegistrationUnitSearchCriteria registrationUnitSearchCriteria = new RegistrationUnitSearchCriteria();
		registrationUnitSearchCriteria.setId(Long.valueOf("2"));
		registrationUnitSearchCriteria.setTenantId("ap.kurnool");

		when(registrationUnitQueryBuilder.getSelectQuery(any(RegistrationUnitSearchCriteria.class)))
				.thenReturn(getBASEQUERY());
		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.search(registrationUnitSearchCriteria);
	}

	@Test
	public void testUpdateRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();
		when(registrationUnitQueryBuilder.getUpdateQuery()).thenReturn(getQueryGenerator());
		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.update(registrationUnit);
	}

	@Test
	public void testExceptionForUpdateRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();
		when(registrationUnitQueryBuilder.getUpdateQuery()).thenReturn(getQueryGenerator());
		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		registrationUnitRepository.update(registrationUnit);
	}

	@Test
	public void testNextIdValueForRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();

		when(registrationUnitQueryBuilder.getIdNextValForRegnUnit()).thenReturn(getQueryGenerator());
		when(jdbcTemplate.queryForObject(Matchers.anyString(), Matchers.<Class<Long>>any()))
				.thenReturn(Long.valueOf("6"));
		registrationUnitRepository.getIdNextVal();
	}

	@Test
	public void testcheckIdsAndTenantIdsFromDBForRegistrationUnit() {
		RegistrationUnit registrationUnit = getRegistrationUnit();

		when(registrationUnitQueryBuilder.getSelectQuery(any(RegistrationUnitSearchCriteria.class)))
				.thenReturn(getQueryGenerator());
		when(jdbcTemplate.queryForObject(any(String.class), any(), any(RegistrationUnitRowMapper.class)))
				.thenReturn(getRegistrationUnit());
		registrationUnitRepository.checkIdsAndTenantIdsFromDB(registrationUnit);
	}

	/**
	 * @Helper_Methods
	 * 
	 * @return
	 */
	private String getQueryGenerator() {
		StringBuilder query = new StringBuilder("insert into table_name(id) values(24)");
		return query.toString();
	}

	private String getBASEQUERY() {
		StringBuilder query = new StringBuilder(registrationUnitQueryBuilder.BASEQUERY);
		return query.toString();
	}

	private RegistrationUnit getRegistrationUnit() {
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedTime(Long.valueOf("1593234786"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedTime(Long.valueOf("1593234786"));

		Location location = new Location();
		location.setDoorNo("No.10");
		location.setBlock(Long.valueOf("123"));
		location.setElectionWard(Long.valueOf("123"));
		location.setLocality(Long.valueOf("123"));
		location.setPinCode(Integer.valueOf("123"));
		location.setRevenueWard(Long.valueOf("123"));
		location.setStreet(Long.valueOf("123"));
		location.setZone(Long.valueOf("123"));

		RegistrationUnit registrationUnit = new RegistrationUnit();
		registrationUnit.setIsActive(true);
		registrationUnit.setName("Bangalore");
		registrationUnit.setTenantId("ap.kurnool");
		registrationUnit.setIsMainRegistrationUnit(true);
		registrationUnit.setAuditDetails(auditDetails);
		registrationUnit.setAddress(location);

		return registrationUnit;
	}
}
