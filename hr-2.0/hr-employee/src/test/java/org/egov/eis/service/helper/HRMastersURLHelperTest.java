package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HRMastersURLHelperTest {

	@InjectMocks
	private HRMastersURLHelper testingObject;

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
		String searchPositionsBaseURL = propertiesManager.getHrMastersServiceHostName()
				+ propertiesManager.getHrMastersServiceBasePath()
				+ propertiesManager.getHrMastersServicePositionsSearchPath();

		Mockito.when(searchPositionsBaseURL)
				.thenReturn("http://localhost:7777/hr-masters/positions/_search");
		Mockito.when(applicationProperties.empSearchPageSizeMax()).thenReturn("500");
/*
		PositionGetRequest positionGetRequest = getNewPositionGetRequest();
		String url = testingObject.searchURL(positionGetRequest);
		System.out.println(url);
		assertEquals("http://localhost:7777/hr-masters/positions/_search?tenantId=1&id=10,12,15,16&pageSize=500", url);
	}

	private PositionGetRequest getNewPositionGetRequest() {
		List<Long> positionIdList = new ArrayList<Long>();
		positionIdList.add(10L);
		positionIdList.add(12L);
		positionIdList.add(15L);
		positionIdList.add(16L);
		return PositionGetRequest.builder().id(positionIdList).tenantId("1").build();
*/
	}

}