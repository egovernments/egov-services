package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;
import static org.mockito.Mockito.doReturn;

@RunWith(Parameterized.class)
public class EmployeeGetQueryTest {

	private static String[] expectedQueries;
	// Using static block to execute & initialise expectedQueries before testConditions() method executes
	// Can't initialise it in @BeforeClass method as @BeforeClass method executes after @Parameters method
	static {
		expectedQueries = getFileContents();
	}

	private String expectedQuery;
	private EmployeeCriteria criteria;
	private Map<String, Object> expectedParams;

	public EmployeeGetQueryTest(EmployeeCriteria criteria, String expectedQuery, Map<String, Object> expectedParams) {
		this.criteria = criteria;
		this.expectedQuery = expectedQuery;
		this.expectedParams = expectedParams;
	}

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private EmployeeQueryBuilder employeeQueryBuilder;

	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This method have two dimensional array of three objects:
	 * 1. EmployeeCriteria	: to pass as input search criteria
	 * 2. Expected Query	: to compare it with the returned query from getQuery method
	 * 3. Expected Params	: to compare it with the returned namedParameters map
	 *
	 * This approach of testing is known as Parameterized Testing. In this type of testing, jUnit runs the single
	 * test as many times as the specified conditions. For this, jUnit initialises every instance variable with
	 * the respective objects defined in the array using parameterised constructor every time the test runs.
	 * Then we can use these instance variables in our test function to either compare with output or pass as input.
	 *
	 * Since this test runs as many times as the array size without even touching the actual test method,
	 * if you want to add more conditions or simply update a particular condition, you just have to update this array.
	 *
	 * @return Collection
	 */
	@Parameters
	public static Collection testConditions() {
		Object[][] conditions = {{
				// Test 0: Passing tenantId in Search Criteria
				EmployeeCriteria.builder().tenantId("default").build(),
				expectedQueries[0],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 1: Passing tenantId, empty list of ids in Search Criteria
				EmployeeCriteria.builder().tenantId("default").id(new ArrayList<>()).build(),
				expectedQueries[1],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 2: Passing tenantId, id in Search Criteria
				EmployeeCriteria.builder().tenantId("default").id(Arrays.asList(1L, 2L)).build(),
				expectedQueries[2],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 3: Passing tenantId, code in Search Criteria
				EmployeeCriteria.builder().tenantId("default").code("EMP01").build(),
				expectedQueries[3],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("code", "EMP01");
				}}
		}, {
				// Test 4: Passing tenantId, departmentId in Search Criteria
				EmployeeCriteria.builder().tenantId("default").departmentId("1").build(),
				expectedQueries[4],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("departmentId", 1L);
				}}
		}, {
				// Test 5: Passing tenantId, designationId in Search Criteria
				EmployeeCriteria.builder().tenantId("default").designationId("1").build(),
				expectedQueries[5],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("designationId", 1L);
				}}
		}, {
				// Test 6: Passing tenantId, positionId in Search Criteria
				EmployeeCriteria.builder().tenantId("default").positionId(1L).build(),
				expectedQueries[6],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("positionId", 1L);
				}}
		}, {
				// Test 7: Passing tenantId, asOnDate in Search Criteria
				EmployeeCriteria.builder().tenantId("default").asOnDate(new Date()).build(),
				expectedQueries[7],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("asOnDate", new Date());
				}}
		}, {
				// Test 8: Passing tenantId, isPrimary in Search Criteria
				EmployeeCriteria.builder().tenantId("default").isPrimary(true).build(),
				expectedQueries[8],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("isPrimary", true);
				}}
		}, {
				// Test 9: Passing tenantId, empty list of employeeStatus in Search Criteria
				EmployeeCriteria.builder().tenantId("default").employeeStatus(new ArrayList<>()).build(),
				expectedQueries[9],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 10: Passing tenantId, employeeStatus in Search Criteria
				EmployeeCriteria.builder().tenantId("default").employeeStatus(Arrays.asList(1L)).build(),
				expectedQueries[10],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("employeeStatuses", Arrays.asList(1L));
				}}
		}, {
				// Test 11: Passing tenantId, empty list of employeeTypes in Search Criteria
				EmployeeCriteria.builder().tenantId("default").employeeType(new ArrayList<>()).build(),
				expectedQueries[11],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 12: Passing tenantId, employeeTypes in Search Criteria
				EmployeeCriteria.builder().tenantId("default").employeeType(Arrays.asList(1L)).build(),
				expectedQueries[12],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("employeeTypes", Arrays.asList(1L));
				}}
		}, {
				// Test 13: Passing tenantId, familyParticularsPresent in Search Criteria
				EmployeeCriteria.builder().tenantId("default").familyParticularsPresent(true).build(),
				expectedQueries[13],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 14: Passing tenantId, familyParticularsPresent in Search Criteria
				EmployeeCriteria.builder().tenantId("default").familyParticularsPresent(false).build(),
				expectedQueries[14],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 15: Passing tenantId, sortBy in Search Criteria
				EmployeeCriteria.builder().tenantId("default").sortBy("e.code").build(),
				expectedQueries[15],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 16: Passing tenantId, sortOrder in Search Criteria
				EmployeeCriteria.builder().tenantId("default").sortOrder("DESC").build(),
				expectedQueries[16],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 17: Passing tenantId, sortBy, sortOrder in Search Criteria
				EmployeeCriteria.builder().tenantId("default").sortBy("e.code").sortOrder("DESC").build(),
				expectedQueries[17],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 18: Passing tenantId, ids, code, departmentId, designationId, positionId, asOnDate, isPrimary,
				// employeeStatuses, employeeTypes, familyParticularsPresent in Search Criteria
				EmployeeCriteria.builder().tenantId("default").id(Arrays.asList(1L, 2L)).code("EMP01")
						.departmentId("1").designationId("1").positionId(1L).asOnDate(new Date()).isPrimary(true)
						.employeeStatus(Arrays.asList(1L)).employeeType(Arrays.asList(1L))
						.familyParticularsPresent(true).build(),
				expectedQueries[18],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("code", "EMP01");
					put("departmentId", "1");
					put("designationId", "1");
					put("positionId", 1L);
					put("asOnDate", new Date());
					put("isPrimary", true);
					put("employeeStatuses", Arrays.asList(1L));
					put("employeeTypes", Arrays.asList(1L));
				}}
		}, {
				// Test 19: Passing tenantId, ids, code, departmentId, designationId, positionId, asOnDate, isPrimary,
				// employeeStatuses, employeeTypes, familyParticularsPresent in Search Criteria
				EmployeeCriteria.builder().tenantId("default").id(Arrays.asList(1L, 2L)).code("EMP01")
						.departmentId("1").designationId("1").positionId(1L).asOnDate(new Date()).isPrimary(true)
						.employeeStatus(Arrays.asList(1L)).employeeType(Arrays.asList(1L))
						.familyParticularsPresent(false).build(),
				expectedQueries[19],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("code", "EMP01");
					put("departmentId", "1");
					put("designationId", "1");
					put("positionId", 1L);
					put("asOnDate", new Date());
					put("isPrimary", true);
					put("employeeStatuses", Arrays.asList(1L));
					put("employeeTypes", Arrays.asList(1L));
				}}
		}, {
				// Test 20: Passing tenantId, ids, code, departmentId, designationId, positionId, asOnDate, isPrimary,
				// employeeStatuses, employeeTypes, familyParticularsPresent, sortBy, sortOrder in Search Criteria
				EmployeeCriteria.builder().tenantId("default").id(Arrays.asList(1L, 2L)).code("EMP01")
						.departmentId("1").designationId("1").positionId(1L).asOnDate(new Date()).isPrimary(true)
						.employeeStatus(Arrays.asList(1L)).employeeType(Arrays.asList(1L))
						.familyParticularsPresent(true).sortBy("e.code").sortOrder("DESC").build(),
				expectedQueries[20],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("code", "EMP01");
					put("departmentId", "1");
					put("designationId", "1");
					put("positionId", 1L);
					put("asOnDate", new Date());
					put("isPrimary", true);
					put("employeeStatuses", Arrays.asList(1L));
					put("employeeTypes", Arrays.asList(1L));
				}}
		}};

		return Arrays.asList(conditions);
	}

	@Test
	public void testGetQuery() {
		doReturn("10").when(applicationProperties).empSearchPageSizeDefault();
		Map<String, Object> namedParameters = new HashMap<>();
		assertEquals(expectedQuery, employeeQueryBuilder.getQuery(criteria, namedParameters, Arrays.asList(1L, 2L)));
		assertEquals(expectedParams.toString(), namedParameters.toString());
	}

	/**
	 * This method reads the queries stored in a file & split & returns them as array of String
	 * @return String[]
	 */
	private static String[] getFileContents() {
		String[] expectedQueries = null;
		try {
			expectedQueries = new FileUtils()
					.getFileContents("org/egov/eis/repository/builder/EmployeeGetQueriesContainer.txt")
                    .split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedQueries;
	}
}
