package org.egov.eis.indexer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.core.web.contract.RequestInfo;
import org.egov.core.web.contract.RequestInfoWrapper;
import org.egov.core.web.contract.ResponseInfo;
import org.egov.eis.indexer.config.PropertiesManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryServiceTest {

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private BoundaryService boundaryService;

	@Test
	public void testGetBoundary() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Boundary boundary = new Boundary().builder().id(10L).name("employee1").tenantid("1").build();
		List<Boundary> boundaries = new ArrayList<>();
		boundaries.add(boundary);
		BoundaryResponse employeeTypeResponse = new BoundaryResponse().builder().responseInfo(responseInfo)
				.boundarys(boundaries).build();

		when(propertiesManager.getEgovLocationServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovLocationServiceBasepath()).thenReturn("/egov-location");
		when(propertiesManager.getEgovLocationServiceBoundarySearchPath()).thenReturn("/boundarys");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egov-location/boundarys?Boundary.id=10");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.getForObject(uri, BoundaryResponse.class)).thenReturn(employeeTypeResponse);

		List<Boundary> insertedBoundary = boundaryService.getBoundary("10", new RequestInfo());
		assertEquals(insertedBoundary, boundaries);
	}
}
