package org.egov.eis.service.helper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
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
			.thenReturn("http://localhost:8080/hr-employee/positions/_search");
		List<Long> positionIdList = new ArrayList<>();
		positionIdList.add(1L);
		positionIdList.add(2L);
		positionIdList.add(3L);
		String url = testingObject.searchURL(positionIdList, "1");
		System.out.println(url);
		assertEquals("http://localhost:8080/hr-employee/positions/_search?tenantId=1&id=1,2,3", url);
	}

}
