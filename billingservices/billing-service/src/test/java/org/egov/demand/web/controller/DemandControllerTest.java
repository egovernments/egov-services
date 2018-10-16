package org.egov.demand.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.TestConfiguration;
import org.egov.demand.model.*;
import org.egov.demand.service.DemandService;
import org.egov.demand.util.FileUtils;
import org.egov.demand.web.contract.DemandDetailResponse;
import org.egov.demand.web.contract.DemandDueResponse;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.demand.web.validator.DemandValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DemandController.class)
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class DemandControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DemandValidator demandValidator;

	@MockBean
	private DemandService demandService;

	@MockBean
	private ResponseFactory responseInfoFactory;

	@Test
	public void testShouldCreateDemands() throws IOException, Exception {

		RequestInfo requestInfo = getRequestInfo();
		Demand demand = getDemand();
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		DemandRequest demandRequest = new DemandRequest(requestInfo, demands);
		System.err.println(demandRequest);
		//when(demandService.create(any(DemandRequest.class))).thenReturn(demands);
		//when(responseInfoFactory.getResponseInfo(any(RequestInfo.class), any(HttpStatus.class)))
		//.thenReturn(getResponseInfo(requestInfo));

		 when(demandService.create(demandRequest)).thenReturn(new DemandResponse( getResponseInfo(requestInfo),demands));

		mockMvc.perform(post("/demand/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("demandrequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("demandresponse.json")));
	}

	@Test
	public void testShouldUpdateDemand()throws IOException,Exception{
		
		Demand demand=getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		demands.add(demand);
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(demands);
		demandResponse.setResponseInfo(new ResponseInfo());
		
		 when(demandService.updateAsync(any(DemandRequest.class))).thenReturn(new DemandResponse( getResponseInfo(getRequestInfo()),demands));
		
		 mockMvc.perform(post("/demand/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("demandrequest.json"))).andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("demandresponse.json")));
	}
	
	@Test
	public void  testShouldUpdateCollection() throws IOException, Exception{
		
		Demand  demand=getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		demands.add(demand);
		DemandResponse demandRespnose=new DemandResponse();
		demandRespnose.setDemands(demands);
		demandRespnose.setResponseInfo(new ResponseInfo());
		
		when(demandService.updateCollection(any(DemandRequest.class))).thenReturn(new DemandResponse( getResponseInfo(getRequestInfo()),demands));
		
		 mockMvc.perform(post("/demand/collection/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("demandrequest.json"))).andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("demandresponse.json")));
	}
	
	@Test
	public void testShouldSearchDemand() throws IOException, Exception{
		List<Demand> demand=new ArrayList<Demand>();
		demand.add(getDemand());
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(demand);
		demandResponse.setResponseInfo(new ResponseInfo());
		
		when(demandService.getDemands(any(DemandCriteria.class),any(RequestInfo.class))).thenReturn(new DemandResponse( getResponseInfo(getRequestInfo()),demand));
		
		mockMvc.perform(post("/demand/_search").param("tenantId", "ap.kurnool")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("demandresponse.json")));
		
	}
	
	@Test
	public void testShouldSearchDemandDetails() throws IOException, Exception{
		
		DemandDetailResponse demandDetailResponse=new DemandDetailResponse();
		demandDetailResponse.setDemandDetails(getDemandDetails());
		demandDetailResponse.setResponseInfo(new ResponseInfo());

		when(demandService.getDemandDetails(any(DemandDetailCriteria.class),any(RequestInfo.class))).thenReturn(new DemandDetailResponse( getResponseInfo(getRequestInfo()),getDemandDetails()));
		
		mockMvc.perform(post("/demand/demanddetail/_search").param("tenantId", "ap.kurnool")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("demandDetailsResponse.json")));
		
	}
	
	@Test
	public void testShouldUpdateMIS() throws IOException, Exception{
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(null);
		demandResponse.setResponseInfo(new ResponseInfo());
		
		when(demandService.updateMISAsync(any(DemandUpdateMisRequest.class))).thenReturn(new DemandResponse(getResponseInfo(getRequestInfo()),null));
		
		mockMvc.perform(post("/demand/_updatemis").param("tenantId", "ap.kurnool").param("id","1").param("consumerCode", "consumerCode1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("UpdatemisResponse.json")));
		
	}
	
	@Test
	public void testShouldUpdateMISWithException() throws IOException, Exception{
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(null);
		demandResponse.setResponseInfo(new ResponseInfo());
		
		when(demandService.updateMISAsync(any(DemandUpdateMisRequest.class))).thenReturn(new DemandResponse(getResponseInfo(getRequestInfo()),null));
		
		mockMvc.perform(post("/demand/_updatemis").param("tenantId", "ap.kurnool")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void testShouldgetDues() throws IOException, Exception{
		List<Demand> demand=new ArrayList<Demand>();
		demand.add(getDemand());
		DemandDueResponse demandDueResponse=new DemandDueResponse();
		DemandDue due=new DemandDue();
		ConsolidatedTax consolidatedTax=ConsolidatedTax.builder().arrearsBalance(0.0).arrearsCollection(0.0).arrearsDemand(0.0).
				currentBalance(50d).currentCollection(205d).currentDemand(255d).build();
		due.setConsolidatedTax(consolidatedTax);
		due.setDemands(demand);
		demandDueResponse.setDemandDue(due);
		demandDueResponse.setResponseInfo(new ResponseInfo());
		
		when(demandService.getDues(any(DemandDueCriteria.class),any(RequestInfo.class))).thenReturn(new DemandDueResponse( getResponseInfo(getRequestInfo()),due));
		
		mockMvc.perform(post("/demand/_dues").param("tenantId", "ap.kurnool").param("consumerCode", "consumer").param("businessService", "PT")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("demandDueResponse.json")));
		
	}
	
	@Test
	public void testShouldGetDuesWithException() throws IOException, Exception{
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(null);
		demandResponse.setResponseInfo(new ResponseInfo());
		
		when(demandService.getDues(any(DemandDueCriteria.class),any(RequestInfo.class))).thenReturn(new DemandDueResponse(getResponseInfo(getRequestInfo()),null));
		
		mockMvc.perform(post("/demand/_dues").param("tenantId", "ap.kurnool")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isBadRequest());
		
	}
	
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	public static ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		return responseInfo;
	}

	public  Demand getDemand() {

		Demand demand = new Demand();
		Owner owner = new Owner();
		owner.setId(1l);

		demand.setConsumerCode("consumercode");
		demand.setBusinessService("businessservice");
		demand.setConsumerType("consumertype");
		demand.setOwner(owner);
		demand.setMinimumAmountPayable(BigDecimal.valueOf(200));
		demand.setTaxPeriodFrom(12345l);
		demand.setTaxPeriodTo(1234567890l);
		demand.setTenantId("ap.kurnool");
		demand.setDemandDetails(getDemandDetails());
		return demand;
	}

	public  List<DemandDetail> getDemandDetails() {

		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		
		demandDetail.setTaxAmount(BigDecimal.valueOf(100));
		demandDetail.setCollectionAmount(BigDecimal.ZERO);
	 	demandDetail.setTaxHeadMasterCode("0002");
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(BigDecimal.valueOf(200));
		demandDetail1.setCollectionAmount(BigDecimal.ZERO);
		demandDetail1.setCollectionAmount(BigDecimal.ZERO);
		demandDetail1.setTaxHeadMasterCode("0003");
		demandDetails.add(demandDetail);
		demandDetails.add(demandDetail1);
		return demandDetails;
	}

	public  RequestInfo getRequestInfo() {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("apiId");
		requestInfo.setVer("ver");
		requestInfo.setDid("did");
		return requestInfo;
	}

}