package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.config.MainConfiguration;
import org.egov.wcms.producer.WaterConnectionProducer;
import org.egov.wcms.repository.WCRepository;
import org.egov.wcms.util.ResponseInfoFactory;
import org.egov.wcms.util.WCServiceUtils;
import org.egov.wcms.util.WaterConnectionConstants;
import org.egov.wcms.web.models.SearcherRequest;
import org.egov.wcms.web.models.WaterConnectionReq;
import org.egov.wcms.web.models.WaterConnectionRes;
import org.egov.wcms.web.models.WaterConnectionSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(MockitoJUnitRunner.class)
public class WaterConnectionServiceTest {
	
	@Mock
	private WCServiceUtils wCServiceUtils;
	
	@Mock
	private WCRepository wcRepository;
	
	@Mock
	private WaterConnectionProducer waterConnectionProducer;
	
	@Mock
	private MainConfiguration mainConfiguration;
	
	@Mock
	private ResponseInfoFactory responseInfoFactory;
	
	@Mock
	private WaterConnectionService service;
	
	@InjectMocks
	private WaterConnectionService waterConnectionService;
	
	@Test
	public void testCreateWCSuccess() {
		WaterConnectionReq waterConnectionReq = new WaterConnectionReq();
		Mockito.doNothing().when(service).enrichCreateRequest(waterConnectionReq);
		Mockito.when(mainConfiguration.getSaveWaterConnectionTopic()).thenReturn("save-wcms-connection");
		Mockito.doNothing().when(waterConnectionProducer).push("save-wcms-connection", waterConnectionReq);
		
		assertNotNull(waterConnectionService.createWaterConnections(waterConnectionReq));
	}
	
	@Test(expected = Exception.class)
	public void testCreateWCFailure() {
		WaterConnectionReq waterConnectionReq = new WaterConnectionReq();
		RequestInfo requestInfo = new RequestInfo();
		waterConnectionReq.setRequestInfo(requestInfo);
		Mockito.doNothing().when(service).enrichCreateRequest(waterConnectionReq);
		Mockito.when(mainConfiguration.getSaveWaterConnectionTopic()).thenReturn("save-wcms-connection");
		Mockito.doNothing().when(waterConnectionProducer).push("save-wcms-connection", waterConnectionReq);
		Mockito.when(responseInfoFactory.createResponseInfoFromRequestInfo(waterConnectionReq.getRequestInfo(), true)).thenThrow(Exception.class);
		
		assertNotNull(waterConnectionService.createWaterConnections(waterConnectionReq));
	}
	
	@Test
	public void testUpdateWCSuccess() {
		WaterConnectionReq waterConnectionReq = new WaterConnectionReq();
		Mockito.doNothing().when(service).enrichUpdateRequest(waterConnectionReq);
		Mockito.when(mainConfiguration.getUpdateWaterConnectionTopic()).thenReturn("update-wcms-connection");
		Mockito.doNothing().when(waterConnectionProducer).push("update-wcms-connection", waterConnectionReq);
		
		assertNotNull(waterConnectionService.updateWaterConnections(waterConnectionReq));
	}
	
	@Test(expected = Exception.class)
	public void testUpdateWCFailure() {
		WaterConnectionReq waterConnectionReq = new WaterConnectionReq();
		RequestInfo requestInfo = new RequestInfo();
		waterConnectionReq.setRequestInfo(requestInfo);
		Mockito.doNothing().when(service).enrichUpdateRequest(waterConnectionReq);
		Mockito.when(mainConfiguration.getUpdateWaterConnectionTopic()).thenReturn("update-wcms-connection");
		Mockito.doNothing().when(waterConnectionProducer).push("update-wcms-connection", waterConnectionReq);
		Mockito.when(responseInfoFactory.createResponseInfoFromRequestInfo(waterConnectionReq.getRequestInfo(), true)).thenThrow(Exception.class);
		
		assertNotNull(waterConnectionService.updateWaterConnections(waterConnectionReq));
	}
	

	@Test
	public void testGetWaterConnectionsSuccess() {
		ObjectMapper mapper = new ObjectMapper();
		RequestInfo requestInfo = new RequestInfo();
		WaterConnectionSearchCriteria WaterConnectionSearchCriteria = new WaterConnectionSearchCriteria();
		WaterConnectionRes waterConnectionRes = new WaterConnectionRes();
		Mockito.when(wCServiceUtils.getObjectMapper()).thenReturn(mapper);
		SearcherRequest searcherRequest = new SearcherRequest();
		StringBuilder uri = new StringBuilder();

		Mockito.when(wCServiceUtils.getSearcherRequest(uri, WaterConnectionSearchCriteria, requestInfo, "searchWC")).thenReturn(searcherRequest);
		Mockito.when(wcRepository.fetchResult(uri, searcherRequest)).thenReturn(waterConnectionRes);
		
		Mockito.when(wCServiceUtils.getDefaultWaterConnectionResponse(requestInfo)).thenReturn(waterConnectionRes);
		
		assertNotNull(waterConnectionService.getWaterConnections(WaterConnectionSearchCriteria, requestInfo));

	}
	
	@Test
	public void testGetWaterConnectionsFailure() {
		ObjectMapper mapper = new ObjectMapper();
		RequestInfo requestInfo = new RequestInfo();
		WaterConnectionSearchCriteria WaterConnectionSearchCriteria = new WaterConnectionSearchCriteria();
		WaterConnectionRes waterConnectionRes = new WaterConnectionRes();
		Mockito.when(wCServiceUtils.getObjectMapper()).thenReturn(mapper);
		SearcherRequest searcherRequest = new SearcherRequest();
		StringBuilder uri = new StringBuilder();

		Mockito.when(wCServiceUtils.getSearcherRequest(uri, WaterConnectionSearchCriteria, requestInfo, 
							WaterConnectionConstants.SEARCHER_WC_SEARCH_DEF_NAME)).thenReturn(searcherRequest);
		Mockito.when(wcRepository.fetchResult(uri, searcherRequest)).thenReturn(waterConnectionRes);
		
		assertNull(waterConnectionService.getWaterConnections(WaterConnectionSearchCriteria, requestInfo));

	}
	
	@Test(expected = Exception.class)
	public void testGetWaterConnectionsException() {
		ObjectMapper mapper = new ObjectMapper();
		RequestInfo requestInfo = new RequestInfo();
		WaterConnectionSearchCriteria WaterConnectionSearchCriteria = new WaterConnectionSearchCriteria();
		WaterConnectionRes waterConnectionRes = new WaterConnectionRes();
		Mockito.when(wCServiceUtils.getObjectMapper()).thenReturn(mapper);
		SearcherRequest searcherRequest = new SearcherRequest();
		StringBuilder uri = new StringBuilder();

		Mockito.when(wCServiceUtils.getSearcherRequest(uri, WaterConnectionSearchCriteria, requestInfo, "searchWC")).thenReturn(searcherRequest);
		Mockito.when(wcRepository.fetchResult(uri, searcherRequest)).thenThrow(Exception.class);
		
		Mockito.when(wcRepository.fetchResult(uri, searcherRequest)).thenReturn(waterConnectionRes);
		
		assertNotNull(waterConnectionService.getWaterConnections(WaterConnectionSearchCriteria, requestInfo));	}
}
