package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.AssignmentGetRequest;
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
public class AssignmentGetQueryTest {

	private static String[] expectedQueries;
	// Using static block to execute & initialise expectedQueries before testConditions() method executes
	// Can't initialise it in @BeforeClass method as @BeforeClass method executes after @Parameters method
	static {
		expectedQueries = getFileContents();
	}

	private String expectedQuery;
	private AssignmentGetRequest criteria;
	private Map<String, Object> expectedParams;

	public AssignmentGetQueryTest(AssignmentGetRequest criteria, String expectedQuery, Map<String, Object> expectedParams) {
		this.criteria = criteria;
		this.expectedQuery = expectedQuery;
		this.expectedParams = expectedParams;
	}

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private AssignmentQueryBuilder assignmentQueryBuilder;

	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This method have two dimensional array of three objects:
	 * 1. AssignmentGetRequest	: to pass as input search criteria
	 * 2. Expected Query		: to compare it with the returned query from getQuery method
	 * 3. Expected Params		: to compare it with the returned namedParameters map
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
				AssignmentGetRequest.builder().tenantId("default").build(),
				expectedQueries[0],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 1: Passing tenantId, isPrimary in Search Criteria
				AssignmentGetRequest.builder().tenantId("default").isPrimary(true).build(),
				expectedQueries[1],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("isPrimary", true);
				}}
		}, {
				// Test 2: Passing tenantId, asOnDate in Search Criteria
				AssignmentGetRequest.builder().tenantId("default").asOnDate(new Date()).build(),
				expectedQueries[2],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("asOnDate", new Date());
				}}
		}, {
				// Test 3: Passing tenantId, sortBy in Search Criteria
				AssignmentGetRequest.builder().tenantId("default").sortBy("a.id").build(),
				expectedQueries[3],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 4: Passing tenantId, sortOrder in Search Criteria
				AssignmentGetRequest.builder().tenantId("default").sortOrder("ASC").build(),
				expectedQueries[4],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
				}}
		}, {
				// Test 5: Passing tenantId, departmentId, asOnDate, sortBy, sortOrder in Search Criteria
				AssignmentGetRequest.builder().tenantId("default").isPrimary(true).asOnDate(new Date()).sortBy("a.id")
						.sortOrder("ASC").build(),
				expectedQueries[5],
				new HashMap<String, Object>() {{
					put("employeeId", 1L);
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 2L));
					put("isPrimary", true);
					put("asOnDate", new Date());
				}}
		}};

		return Arrays.asList(conditions);
	}

	@Test
	public void testGetQuery() {
		doReturn("10").when(applicationProperties).empSearchPageSizeDefault();
		Long employeeId = 1L;
		Map<String, Object> namedParameters = new HashMap<>();
		assertEquals(expectedQuery, assignmentQueryBuilder.getQuery(employeeId, criteria, namedParameters,
				Arrays.asList(1L, 2L)));
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
					.getFileContents("org/egov/eis/repository/builder/AssignmentGetQueriesContainer.txt")
                    .split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedQueries;
	}
}
