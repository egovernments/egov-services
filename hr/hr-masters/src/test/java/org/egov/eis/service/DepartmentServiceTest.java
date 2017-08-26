package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Department;
import org.egov.eis.web.contract.DepartmentResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

	    @Mock
	    private PropertiesManager propertiesManager;

	    @Mock
	    private RestTemplate restTemplate;

	    @InjectMocks
	    private DepartmentService departmentService;
	    
	    @Test
	    public void test_getDepartments() {
	    	
	    	Department department1 = new Department().builder().id(1L).code("100").name("account").active(true).tenantId("default").build();
	        Department department2 = new Department().builder().id(2L).code("200").name("hr").active(true).tenantId("default").build();
	  
	    	List<Department> departments = new ArrayList<>();
	    	departments.add(department1);
	    	departments.add(department2);
	    	
	    	ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
	    	DepartmentResponse departmentResponse = new DepartmentResponse().builder().department(departments).responseInfo(responseInfo).build();
	        
	    	List<Long> ids = new ArrayList<>();
	    	ids.add(1L);
	    	ids.add(2L);
	    	String tenantId = "default";
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper().builder().requestInfo(requestInfo).build();
	    	
	    	when(restTemplate.postForObject(any(URI.class), Matchers.<HttpEntity<?>>any(),
	                Matchers.any(Class.class))).thenReturn(departmentResponse);	    

	    	List<Department> result = departmentService.getDepartments(ids, tenantId, requestInfoWrapper);
	    	assertThat(result).isEqualTo(departments);
	    
	    }
}
