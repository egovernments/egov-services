package org.egov.demand.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.TestConfiguration;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.enums.Type;
import org.egov.demand.service.DemandService;
import org.egov.demand.util.FileUtils;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DemandController.class)
@Import(TestConfiguration.class)
public class DemandControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DemandService demandService;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

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

		 when(demandService.create(demandRequest)).thenReturn(demands);
		 when(responseInfoFactory.getResponseInfo(requestInfo,HttpStatus.CREATED))
		 	.thenReturn(getResponseInfo(requestInfo));

		mockMvc.perform(post("/demand/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("demandrequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("demandresponse.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	public static ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
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
		demand.setMinimumAmountPayable(200d);
		demand.setTaxPeriodFrom(12345l);
		demand.setTaxPeriodTo(1234567890l);
		demand.setType(Type.DUES);
		demand.setTenantId("ap.kurnool");
		demand.setDemandDetails(getDemandDetails());
		return demand;
	}

	public  List<DemandDetail> getDemandDetails() {

		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		
		demandDetail.setTaxAmount(100d);
		demandDetail.setCollectionAmount(0d);
		demandDetail.setTaxHeadCode("0002");
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(200d);
		demandDetail1.setCollectionAmount(0d);
		demandDetail1.setTaxHeadCode("0003");
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