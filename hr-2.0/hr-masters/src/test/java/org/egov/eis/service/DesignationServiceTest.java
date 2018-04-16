package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Designation;
import org.egov.eis.model.Sequence;
import org.egov.eis.repository.DesignationRepository;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.DesignationRequest;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class DesignationServiceTest {

	    @Mock
	    private DesignationRepository designationRepository;


	    @Mock
		private ResponseInfoFactory responseInfoFactory;
	    
	    @Mock
	    private List<Designation> designations;
	   @Mock
	   private CommonIdGenerationService commonIdGenerationService;
	    @InjectMocks
	    private DesignationService designationService;
	    
	    @Test
	    public void test_getDesignation() {
	    	DesignationGetRequest criteria = new DesignationGetRequest().builder().tenantId("default").code("100").active(true).build();
	        when(designationRepository.findForCriteria(criteria)).thenReturn(designations);
	        List<Designation> result = designationService.getDesignations(criteria);   
	        
	        assertThat(result).isEqualTo(designations);
	    }
	    
	    @Test
	    public void test_createDesignation() {
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	Designation designation = new Designation().builder().id(10L).active(true).chartOfAccounts("accounts").code("100").description("account designation").build();
	    	DesignationRequest designationRequest = new DesignationRequest().builder().requestInfo(requestInfo).designation(designation).build();
	    	ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
	        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(expectedResponseInfo);
	        when(commonIdGenerationService.getNextId(any(Sequence.class))).thenReturn(Long.valueOf("10"));
	        ResponseEntity<?> response = designationService.createDesignation(designationRequest);
	    	//verify(kafkaTemplate).send(anyString(), any(DesignationRequest.class));
	    	assertEquals(response.getStatusCode().toString(),"200");
	    }
	    
	    @Test
	    public void test_updateDesignation() {
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	Designation designation = new Designation().builder().id(10L).active(true).chartOfAccounts("accounts").code("100").description("account designation").build();
	    	DesignationRequest designationRequest = new DesignationRequest().builder().requestInfo(requestInfo).designation(designation).build();

	    	ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
	        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(expectedResponseInfo);
	    	ResponseEntity<?> response = designationService.updateDesignation(designationRequest);
	    	//verify(kafkaTemplate).send(anyString(), any(DesignationRequest.class));
	    	assertEquals(response.getStatusCode().toString(),"200");
	    }
	    
	    @Test
	    public void test_create() {
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	Designation designation = new Designation().builder().id(10L).active(true).chartOfAccounts("accounts").code("100").description("account designation").build();
	    	DesignationRequest designationRequest = new DesignationRequest().builder().requestInfo(requestInfo).designation(designation).build();

	    	designationService.create(designationRequest);
	    	verify(designationRepository).create(any(DesignationRequest.class));
	    }
	    
	    @Test
	    public void test_update() {
	    	RequestInfo requestInfo = new RequestInfo().builder().apiId("emp").build();
	    	Designation designation = new Designation().builder().id(10L).active(true).chartOfAccounts("accounts").code("100").description("account designation").build();
	    	DesignationRequest designationRequest = new DesignationRequest().builder().requestInfo(requestInfo).designation(designation).build();

	    	designationService.update(designationRequest);
	    	verify(designationRepository).update(any(DesignationRequest.class));
	    }
}
