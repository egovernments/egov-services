package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserSearchURLHelperTest {
		
	@InjectMocks
    private UserSearchURLHelper testingObject;
	
	@Mock
	private ApplicationProperties applicationProperties;
	
	@Mock
	private PropertiesManager propertiesManager;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testGetBlankQuery() {
		String searchUsersBaseURL = propertiesManager.getUsersServiceHostName()
				+ propertiesManager.getUsersServiceUsersBasePath() + propertiesManager.getUsersServiceUsersSearchPath();

		Mockito.when(searchUsersBaseURL)
			.thenReturn("http://localhost:8080/v1/users/_search");
		Mockito.when(applicationProperties.empSearchPageSizeMax()).thenReturn("500");
/*
		EmployeeGetRequest employeeGetRequest = getNewEmployeeGetRequest();
		String url = testingObject.searchURL(employeeGetRequest);
		System.out.println(url);
		assertEquals("http://localhost:8080/v1/users/_search?tenantId=1&id=10,12,15,16&pageSize=500&sortBy=id&sortOrder=ASC", url);
	}

	private EmployeeGetRequest getNewEmployeeGetRequest() {
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(10L);
		userIdList.add(12L);
		userIdList.add(15L);
		userIdList.add(16L);
		return EmployeeGetRequest.builder().id(userIdList).tenantId("1").build();
*/
	}
}
