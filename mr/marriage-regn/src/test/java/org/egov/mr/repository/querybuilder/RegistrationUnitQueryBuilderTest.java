package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.TestConfiguration;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationUnitQueryBuilderTest {

	@InjectMocks
	private RegistrationUnitQueryBuilder registrationUnitQueryBuilder;

	@Mock
	private RegistrationUnitSearchCriteria registrationUnitSearchCriteria;

	@Mock
	private KafkaTemplate<String, Object> kafkaTemplate;

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithTenantIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithTenantId = registrationUnitSearchCriteria.builder()
				.tenantId("1").build();
		String selectQueryWithTenantId = "SELECT * FROM egmr_registration_unit WHERE tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQuery() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithTenantId = registrationUnitSearchCriteria.builder().build();
		String selectQueryWithTenantId = "SELECT * FROM egmr_registration_unit ";
		assertEquals(selectQueryWithTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithTenantId, preparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithNameTest() {

		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithName = registrationUnitSearchCriteria.builder().tenantId("1")
				.name("Bangalore").build();
		String selectQueryWithName = "SELECT * FROM egmr_registration_unit WHERE name=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithName,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithName, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("Bangalore");
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithNameTestWithZero() {

		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithName = registrationUnitSearchCriteria.builder().tenantId("1")
				.name("").build();
		String selectQueryWithName = "SELECT * FROM egmr_registration_unit WHERE tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithName,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithName, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithLocalityTest() {

		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithLocality = registrationUnitSearchCriteria.builder()
				.tenantId("1").locality(12L).build();
		String selectQueryWithLocality = "SELECT * FROM egmr_registration_unit WHERE locality=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithLocality,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithLocality, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(12L);
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithLocalityTestWithZero() {

		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithLocality = registrationUnitSearchCriteria.builder()
				.tenantId("1").locality(0L).build();
		String selectQueryWithLocality = "SELECT * FROM egmr_registration_unit WHERE tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithLocality,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithLocality, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithZoneTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithZone = registrationUnitSearchCriteria.builder().zone(123L)
				.tenantId("1").build();
		String selectQueryWithZone = "SELECT * FROM egmr_registration_unit WHERE zone=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithZone,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithZone, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(123L);
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithZoneTestwithZero() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithZone = registrationUnitSearchCriteria.builder().zone(0L)
				.tenantId("1").build();
		String selectQueryWithZone = "SELECT * FROM egmr_registration_unit WHERE tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithZone,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithZone, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIsActiveTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithIsActive = registrationUnitSearchCriteria.builder()
				.tenantId("1").isActive(true).build();
		String selectQueryWithIsActive = "SELECT * FROM egmr_registration_unit WHERE isActive=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIsActive,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIsActive, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(true);
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIdAndTenantIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithIdAndTenant = registrationUnitSearchCriteria.builder().id(1L)
				.tenantId("1").build();
		String selectQueryWithIdAndTenantId = "SELECT * FROM egmr_registration_unit WHERE id=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIdAndTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIdAndTenant, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(1L);
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIdAndTenantIdTestWithZero() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchWithIdAndTenant = registrationUnitSearchCriteria.builder().id(0L)
				.tenantId("1").build();
		String selectQueryWithIdAndTenantId = "SELECT * FROM egmr_registration_unit WHERE tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIdAndTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIdAndTenant, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryCriteriaTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		RegistrationUnitSearchCriteria regnUnitSearchCriteria = registrationUnitSearchCriteria.builder().id(1L)
				.name("Belandur").locality(12L).zone(123L).isActive(true).tenantId("1").build();
		String selectQueryCriterias = "SELECT * FROM egmr_registration_unit " + "WHERE id=? AND name=? AND "
				+ "locality=? AND zone=? AND " + "isActive=? AND tenantId=? ORDER BY createdTime ASC ;";
		assertEquals(selectQueryCriterias,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchCriteria, preparedStatementValues));
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(1L);
		expectedPreparedStatementValues.add("Belandur");
		expectedPreparedStatementValues.add(12L);
		expectedPreparedStatementValues.add(123L);
		expectedPreparedStatementValues.add(true);
		expectedPreparedStatementValues.add("1");
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void testInsert() {
		String insertQuery = "INSERT INTO egmr_registration_unit(id,code,name,isactive,tenantid,locality,zone,revenueWard,block,street,electionWard,doorNo,pinCode,createdBy,lastModifiedBy,createdTime,lastModifiedTime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
		assertEquals(insertQuery, registrationUnitQueryBuilder.getCreateQuery());
	}

	@Test
	public void testGetNextValId() {
		String query = "SELECT NEXTVAL('seq_registartion_unit') ;";
		assertEquals(query, registrationUnitQueryBuilder.getIdNextValForRegnUnit());
	}

	@Test
	public void testGetUpdateQuery() {
		String updateQuery = "UPDATE egmr_registration_unit SET code=?, name=?,isactive=?,locality=?,zone=?,revenueWard=?,block=?,street=?,electionWard=?,doorNo=?,pinCode=?,createdBy=?,lastModifiedBy=?,createdTime=?,lastModifiedTime=?WHERE id=? AND tenantId=? ;";
		assertEquals(updateQuery, registrationUnitQueryBuilder.getUpdateQuery());
	}
}