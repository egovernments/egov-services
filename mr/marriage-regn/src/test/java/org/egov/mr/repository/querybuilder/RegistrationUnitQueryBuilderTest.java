package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

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

		RegistrationUnitSearchCriteria regnUnitSearchWithTenantId = registrationUnitSearchCriteria.builder()
				.tenantId("1").build();
		String selectQueryWithTenantId = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithTenantId, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithTenantId));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQuery() {

		RegistrationUnitSearchCriteria regnUnitSearchWithTenantId = registrationUnitSearchCriteria.builder().build();
		String selectQueryWithTenantId = "SELECT * FROM egmr_registration_unit ";
		assertEquals(selectQueryWithTenantId, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithTenantId));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithNameTest() {

		RegistrationUnitSearchCriteria regnUnitSearchWithName = registrationUnitSearchCriteria.builder().tenantId("1")
				.name("Bangalore").build();
		String selectQueryWithName = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' AND name='Bangalore' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithName, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithName));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithNameTestWithZero() {

		RegistrationUnitSearchCriteria regnUnitSearchWithName = registrationUnitSearchCriteria.builder().tenantId("1")
				.name("").build();
		String selectQueryWithName = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithName, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithName));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithLocalityTest() {

		RegistrationUnitSearchCriteria regnUnitSearchWithLocality = registrationUnitSearchCriteria.builder()
				.tenantId("1").locality(12L).build();
		String selectQueryWithLocality = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' AND locality='12' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithLocality, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithLocality));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithLocalityTestWithZero() {

		RegistrationUnitSearchCriteria regnUnitSearchWithLocality = registrationUnitSearchCriteria.builder()
				.tenantId("1").locality(0L).build();
		String selectQueryWithLocality = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithLocality, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithLocality));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithZoneTest() {

		RegistrationUnitSearchCriteria regnUnitSearchWithZone = registrationUnitSearchCriteria.builder().zone(123L)
				.tenantId("1").build();
		String selectQueryWithZone = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' AND zone='123' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithZone, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithZone));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithZoneTestwithZero() {

		RegistrationUnitSearchCriteria regnUnitSearchWithZone = registrationUnitSearchCriteria.builder().zone(0L)
				.tenantId("1").build();
		String selectQueryWithZone = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithZone, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithZone));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIsActiveTest() {

		RegistrationUnitSearchCriteria regnUnitSearchWithIsActive = registrationUnitSearchCriteria.builder()
				.tenantId("1").isActive(true).build();
		String selectQueryWithIsActive = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' AND isActive='true' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIsActive, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIsActive));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIdAndTenantIdTest() {

		RegistrationUnitSearchCriteria regnUnitSearchWithIdAndTenant = registrationUnitSearchCriteria.builder().id(1L)
				.tenantId("1").build();
		String selectQueryWithIdAndTenantId = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' AND id='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIdAndTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIdAndTenant));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryWithIdAndTenantIdTestWithZero() {

		RegistrationUnitSearchCriteria regnUnitSearchWithIdAndTenant = registrationUnitSearchCriteria.builder().id(0L)
				.tenantId("1").build();
		String selectQueryWithIdAndTenantId = "SELECT * FROM egmr_registration_unit WHERE tenantId='1' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryWithIdAndTenantId,
				registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchWithIdAndTenant));
	}

	@SuppressWarnings({ "static-access" })
	@Test
	public void getSelectQueryCriteriaTest() {

		RegistrationUnitSearchCriteria regnUnitSearchCriteria = registrationUnitSearchCriteria.builder().id(1L)
				.name("Belandur").locality(12L).zone(123L).isActive(true).tenantId("1").build();
		String selectQueryCriterias = "SELECT * FROM egmr_registration_unit "
				+ "WHERE tenantId='1' AND id='1' AND name='Belandur' AND locality='12' AND zone='123' AND isActive='true' ORDER BY createdTime ASC ;";
		assertEquals(selectQueryCriterias, registrationUnitQueryBuilder.getSelectQuery(regnUnitSearchCriteria));
	}

	@Test
	public void testInsert() {
		String insertQuery = "INSERT INTO egmr_registration_unit(id,name,isactive,tenantid,locality,zone,revenueWard,block,street,electionWard,doorNo,pinCode,isMainRegistrationUnit,createdBy,lastModifiedBy,createdTime,lastModifiedTime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
		assertEquals(insertQuery, registrationUnitQueryBuilder.getCreateQuery());
	}

	@Test
	public void testGetNextValId() {
		String query = "SELECT NEXTVAL('seq_registartion_unit') ;";
		assertEquals(query, registrationUnitQueryBuilder.getIdNextValForRegnUnit());
	}

	@Test
	public void testGetUpdateQuery() {
		String updateQuery = "UPDATE egmr_registration_unit SET name=?,isactive=?,locality=?,zone=?,revenueWard=?,block=?,street=?,electionWard=?,doorNo=?,pinCode=?,isMainRegistrationUnit=?,createdBy=?,lastModifiedBy=?,createdTime=?,lastModifiedTime=?WHERE id=? AND tenantId=? ;";
		assertEquals(updateQuery, registrationUnitQueryBuilder.getUpdateQuery());
	}
}