package org.egov.eis.service.helper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.PositionGetRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PositionSearchURLHelperTest {
		
	@InjectMocks
    private PositionSearchURLHelper testingObject;
	
	@Mock
	private ApplicationProperties applicationProperties;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testGetBlankQuery() {
		Mockito.when(applicationProperties.empServicesHrMastersServiceGetPositionsHostname())
			.thenReturn("http://localhost:8080/v1/positions/_search");
		Mockito.when(applicationProperties.empSearchPageSizeMax()).thenReturn("500");
		PositionGetRequest positionGetRequest = getNewPositionGetRequest();
		String url = testingObject.searchURL(positionGetRequest);
		System.out.println(url);
		assertEquals("http://localhost:8080/v1/positions/_search?tenantId=1&id=10,12,15,16&pageSize=500", url);
	}

	private PositionGetRequest getNewPositionGetRequest() {
		List<Long> positionIdList = new ArrayList<Long>();
		positionIdList.add(10L);
		positionIdList.add(12L);
		positionIdList.add(15L);
		positionIdList.add(16L);
		return PositionGetRequest.builder().id(positionIdList).tenantId("1").build();
	}

}
