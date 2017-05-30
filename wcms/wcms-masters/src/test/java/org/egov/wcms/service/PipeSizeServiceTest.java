package org.egov.wcms.service;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.producers.PipeSizeProducer;
import org.egov.wcms.repository.PipeSizeRepository;

import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PipeSizeService.class)
@WebAppConfiguration

public class PipeSizeServiceTest {
	
	@Mock
	private ApplicationProperties applicationProperties;
    
	@Mock
	private PipeSizeRepository pipeSizeRepository;
    
	@Mock
	private PipeSizeProducer pipeSizeProducer;
	
    @Mock
    private CodeGeneratorService codeGeneratorService;
	
	
    @InjectMocks
	private PipeSizeService pipeSizeService;
	
	@Test
	public void test_Should_Create_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSize.setSizeInInch(1.2);
        pipeSize.setSizeInMilimeter(10.1);
        
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        PipeSize pipeSizeResult = pipeSizeService.createPipeSize("topic", "key", pipeSizeRequest);
        
        assertNotNull(pipeSizeResult);
	}
	
/*	@Test(expected = Exception.class)
	public void test_throwException_Create_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        when(pipeSizeRequest.getPipeSize()).thenThrow(Exception.class);
        PipeSize pipeSizeResult = pipeSizeService.createPipeSize("topic", "key", pipeSizeRequest);
        
        assertNotNull(pipeSizeResult);
	} */
	
	@Test
	public void test_Should_Update_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSize.setSizeInInch(1.2);
        pipeSize.setSizeInMilimeter(10.1);
        
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        PipeSize pipeSizeResult = pipeSizeService.updatePipeSize("topic", "key", pipeSizeRequest);
        
        assertNotNull(pipeSizeResult);
	}
	
	@Test
	public void test_Should_Get_PipeSize() throws Exception{
		
		when(pipeSizeRepository.checkPipeSizeInmmAndCode("code", 1.2, "tenantId")).thenReturn(false);
        
        boolean result = pipeSizeService.getPipeSizeInmmAndCode("code", 1.2, "tenantId");
        
        assertTrue(false == result);
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Get_PipeSize() throws Exception{
		
		when(pipeSizeRepository.checkPipeSizeInmmAndCode("code", 1.2, "tenantId")).thenThrow(Exception.class);
        
        boolean result = pipeSizeService.getPipeSizeInmmAndCode("code", 1.2, "tenantId");
        
        assertTrue(false == result);
	}
	
	@Test
	public void test_Should_Get_PipeSizes() throws Exception{
		
		PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        
        List<PipeSize> pipeSizes = new ArrayList<>();
        List<PipeSize> pipeSizesResult = new ArrayList<>();

        pipeSizes.add(pipeSize);
		
		when(pipeSizeRepository.findForCriteria(pipeSizeGetRequest)).thenReturn(pipeSizes);
        
		pipeSizesResult = pipeSizeService.getPipeSizes(pipeSizeGetRequest);
        
        assertTrue(null != pipeSizesResult);
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Get_PipeSizes() throws Exception{
		
		PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        
        List<PipeSize> pipeSizes = new ArrayList<>();
        List<PipeSize> pipeSizesResult = new ArrayList<>();

        pipeSizes.add(pipeSize);
		
		when(pipeSizeRepository.findForCriteria(pipeSizeGetRequest)).thenThrow(Exception.class);
        
		pipeSizesResult = pipeSizeService.getPipeSizes(pipeSizeGetRequest);
        
        assertTrue(null != pipeSizesResult);
	}
	
/*	@Test(expected = Exception.class)
	public void test_throwException_Update_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        when(pipeSizeRequest.getPipeSize()).thenThrow(Exception.class);
        PipeSize pipeSizeResult = pipeSizeService.updatePipeSize("topic", "key", pipeSizeRequest);
        
        assertNotNull(pipeSizeResult);
	} */
	
	

}
