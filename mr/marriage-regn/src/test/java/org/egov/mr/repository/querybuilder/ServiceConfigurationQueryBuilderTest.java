package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.TestConfiguration;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceConfigurationQueryBuilderTest {

	@Mock
	private ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria;

	@InjectMocks
	private ServiceConfigurationQueryBuilder serviceConfigurationQueryBuilder;

	@Test
	public void testBaseQuery() {
		String query = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId ";
		assertEquals(query, serviceConfigurationQueryBuilder.BASEQUERY);
	}

	@Test
	public void testSelectQuery() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId ";

		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder
				.getSelectQuery(ServiceConfigurationSearchCriteria.builder().build(), preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithTenantId() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().tenantId("ap.kurnool")
				.build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithName() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? AND keyName IN ('serviceConfigValues') ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

	

		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().tenantId("ap.kurnool")
				.name("serviceConfigValues").build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithNameAsEmpty() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? AND keyName IN ('serviceConfigValues') ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		List<String> namesList = new ArrayList<>();
		namesList.add("serviceConfigValues");
		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().tenantId("ap.kurnool")
				.name("serviceConfigValues").build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithId() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? AND ck.id IN (501) ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		List<Integer> idsList = new ArrayList<>();
		idsList.add(501);
		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().ids(idsList)
				.tenantId("ap.kurnool").build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithIdAsEmpty() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		List<Integer> idsList = new ArrayList<>();
		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().ids(idsList)
				.tenantId("ap.kurnool").build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithEffectiveFrom() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? AND cv.effectivefrom=? ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().tenantId("ap.kurnool")
				.effectiveFrom(Long.valueOf("247862546564")).build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("247862546564"));

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSelectQueryWithNotNullValues() {
		String expectedQuery = "SELECT ck.keyName as keyName, cv.value as value FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId WHERE ck.tenantId=? AND keyName IN ('serviceConfigValues') AND ck.id IN (501) AND cv.effectivefrom=? ORDER BY ck.keyName ASC,cv.effectivefrom DESC;";

		List<Integer> idsList = new ArrayList<>();
		idsList.add(501);
		List<String> namesList = new ArrayList<>();
		namesList.add("serviceConfigValues");
		serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria.builder().ids(idsList)
				.tenantId("ap.kurnool").effectiveFrom(Long.valueOf("247862546564")).name("serviceConfigValues").build();
		List<Object> preparedStatementValues = new ArrayList<>();
		String actualQuery = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("247862546564"));

		assertEquals(preparedStatementValues, expectedPreparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}
}
