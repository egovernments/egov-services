package org.egov.pgr.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class GrievanceServiceTest {

	@Mock
	private ServiceRequestRepository serviceRequestRepository;
	
	@Mock
	private LogAwareKafkaTemplate< String, Object> kafkaProducer;
	
    @Mock
	private PGRProducer pGRProducer;
	
	@Mock
	private ResponseInfoFactory factory;
	
	@Mock
	private PGRUtils pGRUtils;

	@Mock
	private IdGenRepo idGenRepo;
	
	private String saveTopic = "save-pgr-servicereq";

	private String updateTopic = "update-pgr-servicereq";
	
	private String tenantId = "default";
	
	@InjectMocks
	private GrievanceService service;
	
	
	@Before
	public void before() {
		
		ReflectionTestUtils.setField(service, "saveTopic", "save-pgr-servicereq");
		ReflectionTestUtils.setField(service, "updateTopic", "update-pgr-servicereq");
	}
	
	
	@Test
	public void testGetServiceRequestsSuccess() {
		Object response = null;
		RequestInfo requestInfo = new RequestInfo();
		User userInfo = new User();
		userInfo.setId(10L);
		Role role = new Role();
		role.setId(1L); role.setName("CITIZEN");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		userInfo.setRoles(roles);
		requestInfo.setUserInfo(userInfo);
		ServiceReqSearchCriteria serviceReqSearchCriteria = new ServiceReqSearchCriteria();	
		serviceReqSearchCriteria.setAssignedTo("vishal");
		ServiceResponse serviceReqResponse = new ServiceResponse();
		SearcherRequest searcherRequest = new SearcherRequest();
		Mockito.when(pGRUtils.getDefaultServiceResponse(requestInfo)).thenReturn(serviceReqResponse);
		Mockito.when(pGRUtils.prepareSearchRequest(null, serviceReqSearchCriteria, requestInfo)).thenReturn(searcherRequest);
		StringBuilder uri = new StringBuilder();
		uri.append("http://localhost:8093/infra-search/rainmaker-pgr/serviceRequestSearch/_get");
		Mockito.when(serviceRequestRepository.fetchResult(uri, searcherRequest))
		.thenReturn(serviceReqResponse);
		
		response = service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		
		assertNotNull(response);
		
		
	}
	
	
	@Test(expected = Exception.class)
	public void testGetServiceRequestsException() {
		RequestInfo requestInfo = new RequestInfo();
		User userInfo = new User();
		userInfo.setId(10L);
		Role role = new Role();
		role.setId(1L); role.setName("DGRO");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		userInfo.setRoles(roles);
		requestInfo.setUserInfo(userInfo);
		ServiceReqSearchCriteria serviceReqSearchCriteria = new ServiceReqSearchCriteria();
		serviceReqSearchCriteria.setGroup("group");
		serviceReqSearchCriteria.setTenantId("tenantId");		
		
		
		service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		
		
		
	}
	
	
	@Test
	public void testGetServiceRequestsFailure() {
		Object response = null;
		RequestInfo requestInfo = new RequestInfo();
		User userInfo = new User();
		userInfo.setId(10L);
		Role role = new Role();
		role.setId(1L); role.setName("CITIZEN");
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		userInfo.setRoles(roles);
		requestInfo.setUserInfo(userInfo);
		ServiceReqSearchCriteria serviceReqSearchCriteria = new ServiceReqSearchCriteria();
		serviceReqSearchCriteria.setGroup("group");
		serviceReqSearchCriteria.setTenantId("tenantId");		
		ServiceResponse serviceReqResponse = new ServiceResponse();
		SearcherRequest searcherRequest = new SearcherRequest();
		//Mockito.doNothing().when(service).enrichRequest(requestInfo, serviceReqSearchCriteria);
		Mockito.when(pGRUtils.prepareSearchRequest(null, serviceReqSearchCriteria, requestInfo)).thenReturn(searcherRequest);
		StringBuilder uri = new StringBuilder();
		uri.append("http://localhost:8093/infra-search/rainmaker-pgr/serviceRequestSearch/_get");
		Mockito.when(serviceRequestRepository.fetchResult(uri, searcherRequest))
		.thenReturn(serviceReqResponse);
		response = service.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		
		assertNull(response);
		
		
	}
}
