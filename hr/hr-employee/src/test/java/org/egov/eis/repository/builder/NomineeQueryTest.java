package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class NomineeQueryTest {

	private static String[] expectedQueries;
	// Using static block to execute & initialise expectedQueries before testConditions() method executes
	// Can't initialise it in @BeforeClass method as @BeforeClass method executes after @Parameters method
	static {
		expectedQueries = getFileContents();
	}

	private String expectedQuery;
	private NomineeGetRequest criteria;
	private Map<String, Object> expectedParams;

	public NomineeQueryTest(NomineeGetRequest criteria, String expectedQuery, Map<String, Object> expectedParams) {
		this.criteria = criteria;
		this.expectedQuery = expectedQuery;
		this.expectedParams = expectedParams;
	}

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private NomineeQueryBuilder nomineeQueryBuilder;

	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This method have two dimensional array of three objects:
	 * 1. NomineeGetRequest	: to pass as input search criteria
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
				// Test 0: Passing only tenantId in Search Criteria
				NomineeGetRequest.builder().tenantId("default").build(),
				expectedQueries[0],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 1: Passing tenantId & empty list of ids in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(new ArrayList<>()).build(),
				expectedQueries[1],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 2: Passing tenantId & list of ids in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).build(),
				expectedQueries[2],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 3: Passing tenantId & list of employeeIds in Search Criteria
				NomineeGetRequest.builder().tenantId("default").employeeId(Arrays.asList(4L, 5L)).build(),
				expectedQueries[3],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("employeeIds", Arrays.asList(4L, 5L));
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 4: Passing tenantId & nominee name in Search Criteria
				NomineeGetRequest.builder().tenantId("default").name("nominee").build(),
				expectedQueries[4],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 5: Passing tenantId & nominated in Search Criteria
				NomineeGetRequest.builder().tenantId("default").nominated(true).build(),
				expectedQueries[5],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("nominated", true);
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 6: Passing tenantId & sortBy in Search Criteria
				NomineeGetRequest.builder().tenantId("default").sortBy("n.id").build(),
				expectedQueries[6],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 7: Passing tenantId & sortOrder in Search Criteria
				NomineeGetRequest.builder().tenantId("default").sortOrder("DESC").build(),
				expectedQueries[7],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 8: Passing tenantId, sortBy & sortOrder in Search Criteria
				NomineeGetRequest.builder().tenantId("default").sortBy("n.id").sortOrder("DESC").build(),
				expectedQueries[8],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 9: Passing tenantId, list of ids & list of employeeIds in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).employeeId(Arrays.asList(4L, 5L)).build(),
				expectedQueries[9],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("employeeIds", Arrays.asList(4L, 5L));
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 10: Passing tenantId, nominated & nominee name in Search Criteria
				NomineeGetRequest.builder().tenantId("default").name("nominee").nominated(true).build(),
				expectedQueries[10],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("nominated", true);
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 11: Passing tenantId, list of ids, nominated & nominee name in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).name("nominee").nominated(true).build(),
				expectedQueries[11],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("nominated", true);
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 12: Passing tenantId, list of ids, list of employeeIds, nominated & nominee name in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).employeeId(Arrays.asList(4L, 5L)).name("nominee").nominated(true).build(),
				expectedQueries[12],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("employeeIds", Arrays.asList(4L, 5L));
					put("nominated", true);
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 13: Passing tenantId, empty list of ids & empty list of employeeIds in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(new ArrayList<>()).employeeId(new ArrayList<>()).build(),
				expectedQueries[13],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 14: Passing tenantId, list of ids & sortBy n.id in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).sortBy("n.id").build(),
				expectedQueries[14],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 15: Passing tenantId, nominated & sortOrder DESC in Search Criteria
				NomineeGetRequest.builder().tenantId("default").nominated(true).sortOrder("DESC").build(),
				expectedQueries[15],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("nominated", true);
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 16: Passing tenantId, nominee name, sortBy n.id & sortOrder DESC in Search Criteria
				NomineeGetRequest.builder().tenantId("default").name("nominee").sortBy("n.id").sortOrder("DESC").build(),
				expectedQueries[16],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}, {
				// Test 17: Passing tenantId, list of ids, list of employeeIds, nominated, nominee name, sortBy n.id & sortOrder DESC in Search Criteria
				NomineeGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).employeeId(Arrays.asList(4L, 5L)).name("nominee").nominated(true).sortBy("n.id").sortOrder("DESC").build(),
				expectedQueries[17],
				new HashMap<String, Object>() {{
					put("tenantId", "default");
					put("ids", Arrays.asList(1L, 6L));
					put("employeeIds", Arrays.asList(4L, 5L));
					put("nominated", true);
					put("name", "nominee");
					put("pageSize", 10);
					put("pageNumber", 0);
				}}
		}};

		return Arrays.asList(conditions);
	}

	/**
	 * This test method will execute as many times as the parameters defined in the above testConditions() method.
	 * This will execute index based starting from 0 till the length - 1. So, it'll show indexes in test panel.
	 */
	@Test
	public void testGetQuery() {
		doReturn("10").when(applicationProperties).empSearchPageSizeDefault();
		Map<String, Object> namedParameters = new HashMap<>();
		assertEquals(expectedQuery, nomineeQueryBuilder.getQuery(criteria, namedParameters));
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
					.getFileContents("org/egov/eis/repository/builder/NomineeQueriesContainer.txt")
                    .split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedQueries;
	}
}
