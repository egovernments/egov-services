package org.egov.demand.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.enums.Type;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DemandServiceTest {

	@Mock
	private DemandRepository demandRepository;

	@Mock
	private SequenceGenService sequenceGenService;

	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private DemandService demandService;
	
	@Mock
	private ResponseFactory responseInfoFactory;
	
	@Test
	public void methodShouldCreateDemand(){
		
		 final String demandsequence =  "seq_egbs_demand";
		 final String demandDemanddetailSequnece = "seq_egbs_demanddetail";
		
		RequestInfo requestInfo = getRequestInfo();
		
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		
		Demand demand = getDemand();
		List<DemandDetail> details = demand.getDemandDetails();
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		
		DemandRequest demandRequest = new DemandRequest(requestInfo,demands);
		List<String> strings = new ArrayList<>();
		strings.add("1");
		strings.add("2");
		
		when(applicationProperties.getDemandSeqName()).thenReturn(demandsequence);
		when(applicationProperties.getDemandDetailSeqName()).thenReturn(demandDemanddetailSequnece);
		when(sequenceGenService.getIds(demands.size(),demandsequence)).thenReturn(strings);
		when(sequenceGenService.getIds(details.size(),demandDemanddetailSequnece)).thenReturn(strings);
		
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED)).thenReturn(getResponseInfo(requestInfo));
		
		assertEquals(demandService.create(demandRequest), new DemandResponse(getResponseInfo(requestInfo),demands));
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
		demand.setMinimumAmountPayable(BigDecimal.valueOf(200d));
		demand.setTaxPeriodFrom(12345l);
		demand.setTaxPeriodTo(1234567890l);
		demand.setTenantId("ap.kurnool");
		demand.setDemandDetails(getDemandDetails());
		return demand;
	}

	public  List<DemandDetail> getDemandDetails() {

		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		
		demandDetail.setTaxAmount(BigDecimal.valueOf(100d));
		demandDetail.setCollectionAmount(BigDecimal.ZERO);
		TaxHeadMaster taxHeadMaster = new TaxHeadMaster();
		taxHeadMaster.setCode("0002");
		demandDetail.setTaxHeadMaster(taxHeadMaster);
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(BigDecimal.valueOf(200d));
		demandDetail1.setCollectionAmount(BigDecimal.ZERO);
		TaxHeadMaster taxHeadMaster1 = new TaxHeadMaster();
		taxHeadMaster1.setCode("0003");
		demandDetail1.setTaxHeadMaster(taxHeadMaster1);
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
