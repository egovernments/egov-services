package org.egov.lams.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.DemandReason;
import org.egov.lams.repository.helper.DemandHelper;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandReasonCriteria;
import org.egov.lams.web.contract.DemandReasonResponse;
import org.egov.lams.web.contract.DemandRequest;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DemandRepositoryTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private PropertiesManager propertiesManager;
	
	@Mock
	DemandHelper demandHelper;
	
	@InjectMocks
	private DemandRepository demandRepository;
	
	@Test
	public void createDemandTest(){
		
		RequestInfo requestInfo = new RequestInfo();
		DemandRequest demandRequest = new DemandRequest();
		List<Demand> demands = getdemands();
		demandRequest.setDemand(demands);
		demandRequest.setRequestInfo(requestInfo);
		DemandResponse demandResponse = new DemandResponse();
		demandResponse.setDemands(demands);
		String hostname = "http://demand-services:8080/";
		String createPath = "demand-services/demand/_create";
		 
		when(propertiesManager.getDemandServiceHostName()).thenReturn(hostname);
		when(propertiesManager.getCreateDemandSevice()).thenReturn(createPath);
		when(restTemplate.postForObject(hostname+createPath, demandRequest, DemandResponse.class)).thenReturn(demandResponse);
		
		assertTrue(demandResponse.equals(demandRepository.createDemand(demands, requestInfo)));
	}

	@Test
	public void getDemandBySearchTest(){
		
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(1l);
		RequestInfo requestInfo = new RequestInfo();
		requestInfo = new RequestInfo();
		requestInfo.setApiId("apiid");
		requestInfo.setVer("ver");
		requestInfo.setTs(new Date());
		String url = "http://demand-services:8080/demand-services/demand/_search?demandId=" + demandSearchCriteria.getDemandId();
		DemandResponse demandResponse = new DemandResponse();
		demandResponse.setDemands(getdemands());
		
		when(propertiesManager.getDemandServiceHostName()).thenReturn("http://demand-services:8080/");
		when(propertiesManager.getDemandSearchServicepath()).thenReturn("demand-services/demand/_search");
		when(restTemplate.postForObject(url, requestInfo, DemandResponse.class)).thenReturn(demandResponse);
		
		assertTrue(demandResponse.equals(demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo)));
	}
	
	@Test
	public void updateDemandForCollectionTest(){
		DemandRequest demandRequest = new DemandRequest();
		RequestInfo requestInfo = new RequestInfo(); 
		demandRequest.setRequestInfo(requestInfo);
		List<Demand> demands = getdemands();
		demandRequest.setDemand(demands);
		DemandResponse demandResponse = new DemandResponse();
		demandResponse.setDemands(demands);
		String url = "http://demand-services:8080/demand-services/demand/"+demands.get(0).getId() +"/_update" ;
		
		when(propertiesManager.getDemandServiceHostName()).thenReturn("http://demand-services:8080/");
		when(propertiesManager.getUpdateDemandBasePath()).thenReturn("demand-services/demand/");
		when(propertiesManager.getUpdateDemandService()).thenReturn("_update");
		when(restTemplate.postForObject(url, demandRequest, DemandResponse.class)).thenReturn(demandResponse);
	
		assertTrue(demandResponse.equals(demandRepository.updateDemandForCollection(demands, requestInfo)));
	}
	
	@Test
	public void updateDemandTest(){
		DemandRequest demandRequest = new DemandRequest();
		RequestInfo requestInfo = new RequestInfo(); 
		List<Demand> demands = getdemands();
		
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);
		
		DemandResponse demandResponse = new DemandResponse();
		demandResponse.setDemands(demands);
		
		String url = "http://demand-services:8080/demand-services/demand/_update" ;
		
		when(propertiesManager.getDemandServiceHostName()).thenReturn("http://demand-services:8080/");
		when(propertiesManager.getUpdateDemandBasePath()).thenReturn("demand-services/demand/");
		when(propertiesManager.getUpdateDemandService()).thenReturn("_update");
		when(restTemplate.postForObject(url, demandRequest, DemandResponse.class)).thenReturn(demandResponse);
	
		assertTrue(demandResponse.equals(demandRepository.updateDemand(demands, requestInfo)));
	}
	
	
	
	private List<Demand> getdemands() {
		
		List<Demand> demands = new ArrayList<>();
		Demand demand = new Demand();
		demand.setId("1");
		demand.setModuleName("Leases And Agreements");
		demand.setMinAmountPayable(0.0);
		DemandDetails demandDetail = new DemandDetails();
		demandDetail.setTaxReason("No Reason");
		demandDetail.setTaxAmount(BigDecimal.valueOf(2230d));
		demandDetail.setCollectionAmount(BigDecimal.ZERO);
		demandDetail.setRebateAmount(BigDecimal.ZERO);
		List<DemandDetails> demandDetails = new ArrayList<>();
		demandDetails.add(demandDetail);
		demand.setDemandDetails(demandDetails);
		demands.add(demand);
		System.out.println(demands);
		return demands;
	}

	private List<DemandReason> getDemandReasons(){
		List<DemandReason> demandReasons = new ArrayList<>();
		DemandReason demandReason = new DemandReason();
		demandReason.setName("No Reason");
		demandReasons.add(demandReason);
		return demandReasons;
	}
	
	private AgreementRequest getAgreementRequest(){
		AgreementRequest agreementRequest = new AgreementRequest();
		RequestInfo requestInfo = new RequestInfo();
		agreementRequest.setRequestInfo(requestInfo);
		Agreement agreement = new Agreement();
		agreement.setRent(2230d);
		agreementRequest.setAgreement(agreement);
		return agreementRequest;
	}

}
