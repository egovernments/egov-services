package org.egov.pgr.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.tracer.model.ServiceCallException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestServiceTest {
	
	public static final Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);

	@Mock
	private ServiceRequestRepository serviceRequestRepository;
	
	@InjectMocks
	private ServiceRequestService service;
	
	@Test
	public void testGetServiceRequestsSuccess() {
		Object response = new Object();
		RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
		ServiceReqSearchCriteria serviceReqSearchCriteria = Mockito.mock(ServiceReqSearchCriteria.class);
		ServiceReqResponse serviceReqResponse = new ServiceReqResponse();
		Mockito.when(serviceRequestRepository.getServiceRequests(requestInfo, serviceReqSearchCriteria))
		.thenReturn(serviceReqResponse);
		response = service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		
		assertNotNull(response);
		
		
	}
	
	@Test
	public void testGetServiceRequestsFailure() {
		Object response = new Object();
		RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
		ServiceReqSearchCriteria serviceReqSearchCriteria = Mockito.mock(ServiceReqSearchCriteria.class);
		Mockito.when(serviceRequestRepository.getServiceRequests(requestInfo, serviceReqSearchCriteria))
		.thenReturn(null);
		
		response = service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		
		assertNull(response);
		
		
	}
	
	@Test(expected = ServiceCallException.class)
	public void testGetServiceRequestsException() {
		RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
		ServiceReqSearchCriteria serviceReqSearchCriteria = Mockito.mock(ServiceReqSearchCriteria.class);
		Mockito.when(serviceRequestRepository.getServiceRequests(requestInfo, serviceReqSearchCriteria))
		.thenThrow(new ServiceCallException());
		
		service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
				
	}

}
