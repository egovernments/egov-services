package org.egov.pgr.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.contract.IdGenerationRequest;
import org.egov.pgr.contract.IdGenerationResponse;
import org.egov.pgr.contract.IdRequest;
import org.egov.pgr.contract.IdResponse;
import org.egov.pgr.utils.PGRConstants;
import org.egov.tracer.model.ServiceCallException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class IdGenRepoTest {

	private String idGenHost = "http://localhost:8088/";

	private String idGenPath = "egov-idgen/id/_generate";

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private IdGenRepo idGenRepo;

	@Before
	public void before() {
		/*static void	setField(java.lang.Class<?> targetClass, java.lang.String name, java.lang.Object value)
		Set the static field with the given name on the provided targetClass to the supplied value.*/
		
		ReflectionTestUtils.setField(idGenRepo, "idGenPath", "egov-idgen/id/_generate");
		ReflectionTestUtils.setField(idGenRepo, "idGenHost", "http://localhost:8088/");
	}
	
	@Test
	public void idGenShouldGetResponse() {
		
		long count =2;
		
		when(restTemplate.postForObject(idGenHost+idGenPath, getIdGenRequest(), IdGenerationResponse.class)).thenReturn(getIdGenResponse());
		assertTrue(getIdGenResponse().equals(idGenRepo.getId(new RequestInfo(), "mh.roha", count)));
		
	}
	
	@Test
	public void idGenShouldFail() {
		
		IdResponse idResponse = IdResponse.builder().id("id1").build();
		IdResponse idResponse1 = IdResponse.builder().id("id2").build();
		List<IdResponse> idResponses = new ArrayList<>();
		idResponses.add(idResponse);
		idResponses.add(idResponse1);
		idResponses.add(idResponse1);
		IdGenerationResponse mockFailResponse = IdGenerationResponse.builder().idResponses(idResponses).responseInfo(getResponseInfo()).build();
		
		long count =2;
		
		when(restTemplate.postForObject(idGenHost+idGenPath, getIdGenRequest(), IdGenerationResponse.class)).thenReturn(getIdGenResponse());
		assertFalse(mockFailResponse.equals(idGenRepo.getId(new RequestInfo(), "mh.roha", count)));
	}
	
	@Test(expected = ServiceCallException.class)
	public void idGenShouldThrowServiceCallException() {
		
		long count =2;
		
		when(restTemplate.postForObject(idGenHost+idGenPath, getIdGenRequest(), IdGenerationResponse.class)).thenThrow(new ServiceCallException());
		assertFalse(getIdGenResponse().equals(idGenRepo.getId(new RequestInfo(), "mh.roha", count)));
	}
	
	private IdGenerationRequest getIdGenRequest() {
		
		String name = PGRConstants.ID_NAME;
		String format = PGRConstants.ID_FORMAT;
		
		IdRequest idRequest = IdRequest.builder().tenantId("mh.roha").idName(name).format(format).build();
		IdRequest idRequest1 = IdRequest.builder().tenantId("mh.roha").idName(name).format(format).build();
		List<IdRequest> idRequests = new ArrayList<>();
		idRequests.add(idRequest1);
		idRequests.add(idRequest);
		
		return IdGenerationRequest.builder().idRequests(idRequests).requestInfo(new RequestInfo()).build();
	}

	private IdGenerationResponse getIdGenResponse() {

		IdResponse idResponse = IdResponse.builder().id("id1").build();
		IdResponse idResponse1 = IdResponse.builder().id("id2").build();
		List<IdResponse> idResponses = new ArrayList<>();
		idResponses.add(idResponse);
		idResponses.add(idResponse1);
		return IdGenerationResponse.builder().idResponses(idResponses).responseInfo(getResponseInfo()).build();
	}
	
	private ResponseInfo getResponseInfo() {
		return ResponseInfo.builder().resMsgId("uief87324").status("successful").build();
	}
}
