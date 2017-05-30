package org.egov.wcms.repository;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.repository.builder.PipeSizeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PipeSizeRowMapper;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PipeSizeRepository.class)
@WebAppConfiguration

public class PipeSizeRepositoryTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private PipeSizeQueryBuilder pipeSizeQueryBuilder;
	
	@Mock
    private PipeSizeRowMapper pipeSizeRowMapper;
	
	@MockBean
	private PipeSizeService pipeSizeService;
	
	@MockBean
    private ErrorHandler errHandler;
	
	@MockBean
	private ApplicationProperties applicationProperties;
	
    @MockBean
    private ResponseInfoFactory responseInfoFactory;
	    
	@InjectMocks
	private PipeSizeRepository pipeSizeRepository;
	
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
        
        
        assertNotNull(pipeSizeRepository.persistCreatePipeSize(pipeSizeRequest));
        
        
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Create_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSize.setSizeInInch(1.2);
        pipeSize.setSizeInMilimeter(10.1);
        
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        
        assertNotNull(pipeSizeRepository.persistCreatePipeSize(pipeSizeRequest));
        
        
	}
	
	@Test
	public void test_Should_Modify_PipeSize() throws Exception{
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
        
        
        assertNotNull(pipeSizeRepository.persistModifyPipeSize(pipeSizeRequest));
        
        
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Modify_PipeSize() throws Exception{
        PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        RequestInfo requestInfo = new RequestInfo();
        PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setCreatedBy(1L);
        pipeSize.setSizeInInch(1.2);
        pipeSize.setSizeInMilimeter(10.1);
        
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSize(pipeSize);
        
        
        assertNotNull(pipeSizeRepository.persistModifyPipeSize(pipeSizeRequest));
        
        
	}
	
	@Test
	public void test_Should_Check_PipeSize() throws Exception{
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(1.22);
        preparedStatementValues.add(1.45);
        
        
        assertNotNull(pipeSizeRepository.checkPipeSizeInmmAndCode("ABC", 1.22, "1"));
        
        
	}
	
/*	@Test(expected = Exception.class)
	public void test_throwException_Check_PipeSize() throws Exception{
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(1.22);
        preparedStatementValues.add(1.45);
        
        when(pipeSizeQueryBuilder.selectPipeSizeInmmAndCodeNotInQuery()).thenThrow(Exception.class);
        
        assertNotNull(pipeSizeRepository.checkPipeSizeInmmAndCode("ABC", 1.22, "1"));
        
        
	} */
	
	@Test
	public void test_Should__FindforCriteria() throws Exception{
        List<Object> preparedStatementValues = new ArrayList<Object>();
        PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        when(pipeSizeQueryBuilder.getQuery(pipeSizeGetRequest, preparedStatementValues)).thenReturn("query");
        List<PipeSize> pipeSizes = pipeSizeRepository.findForCriteria(pipeSizeGetRequest);
        
        assertTrue(pipeSizes != null);
        
        
        
	}
	
	@Test(expected = Exception.class)
	public void test_throwException__FindforCriteria() throws Exception{
        List<Object> preparedStatementValues = new ArrayList<Object>();
        PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        when(pipeSizeQueryBuilder.getQuery(pipeSizeGetRequest, preparedStatementValues)).thenThrow(Exception.class);
        List<PipeSize> pipeSizes = pipeSizeRepository.findForCriteria(pipeSizeGetRequest);
        
        assertTrue(pipeSizes != null);
        
        
        
	}
	
	

}
