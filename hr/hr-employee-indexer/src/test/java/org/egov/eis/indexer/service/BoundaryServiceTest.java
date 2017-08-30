package org.egov.eis.indexer.service;

import org.egov.boundary.web.contract.Boundary;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.indexer.config.PropertiesManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
		Boundary boundary = Boundary.builder().id("10").name("employee").tenantId("default").build();
		BoundaryResponse boundaryResponse = BoundaryResponse.builder().responseInfo(responseInfo)
				.boundarys(Collections.singletonList(boundary)).build();

		when(propertiesManager.getEgovLocationServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovLocationServiceBasepath()).thenReturn("/egov-location");
		when(propertiesManager.getEgovLocationServiceBoundarySearchPath()).thenReturn("/boundarys");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egov-location/boundarys?Boundary.tenantId=default&Boundary.id=10");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.getForObject(uri, BoundaryResponse.class)).thenReturn(boundaryResponse);

		Boundary insertedBoundary = boundaryService.getBoundary(10L, "default");
		assertEquals(insertedBoundary, boundary);
	}
}
