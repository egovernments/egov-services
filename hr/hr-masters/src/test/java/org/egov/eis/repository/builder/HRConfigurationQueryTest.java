package org.egov.eis.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.HRConfigurationGetRequest;
import org.egov.eis.web.contract.HRStatusGetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(Parameterized.class)
public class HRConfigurationQueryTest {

	private static String[] expectedQueries;
	// Using static block to execute & initialise expectedQueries before testConditions() method executes
	// Can't initialise it in @BeforeClass method as @BeforeClass method executes after @Parameters method
	static {
		expectedQueries = getFileContents();
	}

	private String expectedQuery;
	private HRConfigurationGetRequest criteria;
	private List<String> expectedParams;
	
	public HRConfigurationQueryTest(HRConfigurationGetRequest criteria, String expectedQuery, List<String> expectedParams) {
		this.criteria = criteria;
		this.expectedQuery = expectedQuery;
		this.expectedParams = expectedParams;
	}

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private HRConfigurationQueryBuilder hrConfigurationQueryBuilder;

	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}


	/**
	 * This method have two dimensional array of three objects:
	 * 1. NomineeGetRequest	: to pass as input search criteria
	 * 2. Expected Query	: to compare it with the returned query from getQuery method
	 * 3. Expected Params	: to compare it with the returned parameters list
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
			HRConfigurationGetRequest.builder().tenantId("default").build(),
				expectedQueries[0],
				new ArrayList<>(Arrays.asList("default", "10", "0"))
		}, {
				// Test 2: Passing tenantId & list of ids in Search Criteria
			HRConfigurationGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).build(),
				expectedQueries[1],
				new ArrayList<>(Arrays.asList("default", "10", "0"))
		}, {
				// Test 4: Passing tenantId & name in Search Criteria
			HRConfigurationGetRequest.builder().tenantId("default").name("clerk").build(),
				expectedQueries[2],
				new ArrayList<>(Arrays.asList("default", "clerk", "10", "0"))
		}, {
				// Test 5: Passing tenantId & effective date in Search Criteria
			HRConfigurationGetRequest.builder().tenantId("default").effectiveFrom(new Date()).build(),
				expectedQueries[3],
				new ArrayList<>(Arrays.asList("default", new Date(), "10", "0"))
	    }, {
			// Test 6: Passing tenantId & sortBy in Search Criteria
	    	HRConfigurationGetRequest.builder().tenantId("default").sortBy("id").build(),
			expectedQueries[4],
			new ArrayList<>(Arrays.asList("default", "10", "0"))
	    }, {
			// Test 7: Passing tenantId & sortOrder in Search Criteria
	    	HRConfigurationGetRequest.builder().tenantId("default").sortOrder("DESC").build(),
			expectedQueries[5],
			new ArrayList<>(Arrays.asList("default", "10", "0"))
	    }, {
			// Test 11: Passing tenantId, list of ids, name, effective date in Search Criteria
	    	HRConfigurationGetRequest.builder().tenantId("default").id(Arrays.asList(1L, 6L)).name("clerk").effectiveFrom(new Date()).build(),
			expectedQueries[6],
			new ArrayList<>(Arrays.asList("default", "clerk", new Date(), "10", "0"))
		}};

		return Arrays.asList(conditions);
	}

	/**
	 * This test method will execute as many times as the parameters defined in the above testConditions() method.
	 * This will execute index based starting from 0 till the length - 1. So, it'll show indexes in test panel.
	 */
	@Test
	public void testGetQuery() {
		doReturn("10").when(applicationProperties).hrSearchPageSizeDefault();
		List<String> parameters = new ArrayList<String>();
		assertEquals(expectedQuery, hrConfigurationQueryBuilder.getQuery(criteria, parameters));
		assertEquals(expectedParams.toString(), parameters.toString());
	}

	/**
	 * This method reads the queries stored in a file & split & returns them as array of String
	 * @return String[]
	 */
	private static String[] getFileContents() {
		String[] expectedQueries = null;
		try {
			expectedQueries = new FileUtils()
					.getFileContents("org/egov/eis/repository/builder/HRConfigurationQueriesContainer.txt")
                    .split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedQueries;
	}
}
