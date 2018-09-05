package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Position;
import org.egov.eis.repository.VacantPositionsRepository;
import org.egov.eis.web.contract.NonVacantPositionsResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.VacantPositionsGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class VacantPositionsServiceTest {

	 @Mock
	    private PropertiesManager propertiesManager;

	    @Mock
	    private RestTemplate restTemplate;
	    
	    @Mock
		private VacantPositionsRepository vacantPositionsRepository;

	    @InjectMocks
	    private VacantPositionsService vacantPositionsService;
	    
	    @Test
	    public void test_getVacantPositions() {
	    	
	    	Position position1 = new Position().builder().id(1L).active(true).tenantId("default").build();
	    	Position position2 = new Position().builder().id(2L).active(true).tenantId("default").build();
	    	List<Long> positionIds = new ArrayList<>();
	    	positionIds.add(position1.getId());
	    	positionIds.add(position2.getId());
	    	
	    	List<Position> positions = new ArrayList<>();
	    	positions.add(position1);
	    	positions.add(position2);
	    	
	    	ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
	    	NonVacantPositionsResponse nonVacantPositionsResponse = new NonVacantPositionsResponse().builder().positionIds(positionIds).responseInfo(responseInfo).build();
	        
	    	VacantPositionsGetRequest criteria = new VacantPositionsGetRequest().builder().id(positionIds).asOnDate(new Date()).departmentId(10L).designationId(15L).tenantId("default").destinationTenant("").build();
	    	
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper().builder().requestInfo(requestInfo).build();
	    	
	    	when(restTemplate.postForObject(any(String.class), any(RequestInfoWrapper.class),
	                Matchers.any(Class.class))).thenReturn(nonVacantPositionsResponse);	    

	    	when(vacantPositionsRepository.findForCriteria(criteria)).thenReturn(positions);
	    	
	    	List<Position> result = vacantPositionsService.getVacantPositions(criteria, requestInfoWrapper);
	    	assertThat(result).isEqualTo(positions);
	    
	    }
}
