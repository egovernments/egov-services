package org.egov.demand.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Bill;
import org.egov.demand.model.ConsolidatedTax;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.model.DemandDue;
import org.egov.demand.model.DemandDueCriteria;
import org.egov.demand.model.DemandUpdateMisRequest;
import org.egov.demand.model.Owner;
import org.egov.demand.producer.Producer;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.repository.OwnerRepository;
import org.egov.demand.util.DemandEnrichmentUtil;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.DemandDetailResponse;
import org.egov.demand.web.contract.DemandDueResponse;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	
	@Mock
	private OwnerRepository ownerRepository;
	
	@Mock
	private DemandEnrichmentUtil demandEnrichmentUtil;
	
	@Mock
	private Producer producer;
	
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
		Mockito.doNothing().when(producer).push("create-demand-index", demandRequest);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED)).thenReturn(getResponseInfo(requestInfo));
		
		assertEquals(demandService.create(demandRequest), new DemandResponse(getResponseInfo(requestInfo),demands));
	}
	
	@Test
	public void methodShouldUpdateAsync(){
		
		RequestInfo requestInfo=new RequestInfo();
		
		User user=new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		
		Demand demand =getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		List<DemandDetail> details = demand.getDemandDetails();
		demands.add(demand);
		DemandRequest demandRequest=new DemandRequest(requestInfo,demands);
		List<String> strings = new ArrayList<>();
		strings.add("1");
		strings.add("2");
		
		when(applicationProperties.getDemandSeqName()).thenReturn("seq_egbs_demand");
		when(applicationProperties.getDemandDetailSeqName()).thenReturn("seq_egbs_demanddetail");
		when(sequenceGenService.getIds(demands.size(),"seq_egbs_demand")).thenReturn(strings);
		when(sequenceGenService.getIds(details.size(),"seq_egbs_demanddetail")).thenReturn(strings);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED)).thenReturn(getResponseInfo(requestInfo));
		Mockito.doNothing().when(producer).push("create-demand-index", demandRequest);

		assertEquals(demandService.updateAsync(demandRequest), new DemandResponse(getResponseInfo(requestInfo),demands));
	}
	
	/*@Test
	public void testShouldUpdateDemandFromBill(){
		BillRequest billRequest=new BillRequest();
		Bill bill=getBill();
		billRequest.getBills().add(bill);
		
		Demand demand =getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		demands.add(demand);
		
		when(demandRepository.getDemands(any(DemandCriteria.class),any())).thenReturn(demands);
		doNothing().when(demandRepository).update(any(DemandRequest.class));
		
		assertTrue(demandResponse.equals(billService.createAsync(billRequest)));
	}*/
	
	@Test
	public void methodShouldUpdateCollection(){
		
		RequestInfo requestInfo=new RequestInfo();
		User user=new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		
		Demand demand =getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		demands.add(demand);
		DemandRequest demandRequest=new DemandRequest(requestInfo,demands);
		
		when(demandRepository.getDemands(any(DemandCriteria.class),any())).thenReturn(demands);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED)).thenReturn(getResponseInfo(requestInfo));
		
		assertEquals(demandService.updateCollection(demandRequest), new DemandResponse(getResponseInfo(requestInfo),demands));
		
	}
	
	@Test
	public void methodShouldGetDemands(){
		RequestInfo requestInfo=new RequestInfo();
		User user=new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		
		Owner owner=getOwner();
		List<Owner> owners=new ArrayList<Owner>();
		owners.add(owner);
		
		Demand demand =getDemand();
		List<Demand> demands=new ArrayList<Demand>();
		demands.add(demand);
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(demands);
		
		DemandCriteria demandCriteria=DemandCriteria.builder().tenantId("ap.kurnool").mobileNumber("1234").email("xyz@abc.com").receiptRequired(false).build();
		when(ownerRepository.getOwners(any(UserSearchRequest.class))).thenReturn(owners);
		when(demandRepository.getDemands(any(DemandCriteria.class),any())).thenReturn(demands);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK)).thenReturn(getResponseInfo(requestInfo));
		when(demandEnrichmentUtil.enrichOwners(any(List.class),any(List.class))).thenReturn(demands);
		
		assertEquals(demandService.getDemands(demandCriteria,any()), demandResponse);
	
	}
	
	@Test
	public void testShouldGetDemandDetails(){
		RequestInfo requestInfo=new RequestInfo();
		User user=new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		DemandDetailResponse demandDetailResponse=new DemandDetailResponse();
		List<DemandDetail> demandDetails=getDemandDetails();
		demandDetailResponse.setDemandDetails(demandDetails);
		
		when(demandRepository.getDemandDetails(any(DemandDetailCriteria.class))).thenReturn(demandDetails);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK)).thenReturn(getResponseInfo(requestInfo));
		
		assertEquals(demandService.getDemandDetails(any(DemandDetailCriteria.class),any(RequestInfo.class)), demandDetailResponse);
	}
	
	@Test
	public void testShouldUpdateMISAsync(){
		
		DemandUpdateMisRequest demandRequest=DemandUpdateMisRequest.builder().tenantId("default").consumerCode("ConsumerCode").build();
		DemandResponse demandResponse=new DemandResponse();
		demandResponse.setDemands(null);
		when(applicationProperties.getUpdateMISTopicName()).thenReturn("someTopicName");
		
		assertEquals(demandService.updateMISAsync(demandRequest), demandResponse);
	}
	
	@Test
	public void testShouldUpdateMIS(){
		doNothing().when(demandRepository).updateMIS(any(DemandUpdateMisRequest.class));
		DemandUpdateMisRequest request=DemandUpdateMisRequest.builder().tenantId("default").build();
		demandService.updateMIS(request);
	}
	
	@Test
	public void testShouldGetDues(){
		List<Demand> demand=new ArrayList<Demand>();
		Set<String> consumerCode=new HashSet<>();
		consumerCode.add("consumer");
		demand.add(getDemand());
		DemandDueResponse demandDueResponse=new DemandDueResponse();
		DemandDue due=new DemandDue();
		ConsolidatedTax consolidatedTax=ConsolidatedTax.builder().arrearsBalance(300d).arrearsCollection(0.0).arrearsDemand(300d).
				currentBalance(0d).currentCollection(0d).currentDemand(0.0).build();
		due.setConsolidatedTax(consolidatedTax);
		due.setDemands(demand);
		demandDueResponse.setDemandDue(due);
		demandDueResponse.setResponseInfo(null);
		DemandResponse demandResponse=DemandResponse.builder().demands(demand).build();
		DemandDueCriteria demandDueCriteria=DemandDueCriteria.builder().
				consumerCode(consumerCode).businessService("PT").tenantId("ap.kurnool").build();
		
		RequestInfo requestInfo=new RequestInfo();
		User user=new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		
		Owner owner=getOwner();
		List<Owner> owners=new ArrayList<Owner>();
		owners.add(owner);
		
		DemandCriteria demandCriteria=DemandCriteria.builder().tenantId("ap.kurnool").mobileNumber("1234").email("xyz@abc.com").build();
		when(ownerRepository.getOwners(any(UserSearchRequest.class))).thenReturn(owners);
		when(demandRepository.getDemands(any(DemandCriteria.class),any())).thenReturn(demand);
		when(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK)).thenReturn(getResponseInfo(requestInfo));
		when(demandEnrichmentUtil.enrichOwners(any(List.class),any(List.class))).thenReturn(demand);
		
		
		assertEquals(demandService.getDues(demandDueCriteria, new RequestInfo()).toString(), demandDueResponse.toString());
		
	}
	
	public static ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setVer(requestInfo.getVer());
		return responseInfo;
	}
	
	public Bill getBill(){
		Bill bill=new Bill();
		
		bill.setAuditDetail(getAuditDetails());
		bill.setTenantId("ap.kurnool");
		bill.setId("12");
		
		return bill;
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
		demand.setAuditDetail(getAuditDetails());
		return demand;
	}

	public  List<DemandDetail> getDemandDetails() {

		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		
		demandDetail.setTaxAmount(BigDecimal.valueOf(100d));
		demandDetail.setCollectionAmount(BigDecimal.ZERO);
		demandDetail.setTaxHeadMasterCode("0002");
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(BigDecimal.valueOf(200d));
		demandDetail1.setCollectionAmount(BigDecimal.ZERO);
		demandDetail1.setTaxHeadMasterCode("0003");
		
		demandDetail.setAuditDetail(getAuditDetails());
		demandDetail1.setAuditDetail(getAuditDetails());
		demandDetails.add(demandDetail);
		demandDetails.add(demandDetail1);
		return demandDetails;
	}
	
	public AuditDetail getAuditDetails(){
		
		AuditDetail auditDetail=new AuditDetail();
		auditDetail.setCreatedBy("xyz");
		auditDetail.setCreatedTime(2345l);
		auditDetail.setLastModifiedBy("xyz");
		auditDetail.setLastModifiedTime(2345l);
		return auditDetail;
	}
	
	public Owner getOwner(){
		Owner owner=new Owner();
		
		owner.setAadhaarNumber("1234");
		owner.setEmailId("xyz@abc.com");
		owner.setId(1l);
		owner.setMobileNumber("1234");
		owner.setName("qwert");
		
		return owner;
	}

	public  RequestInfo getRequestInfo() {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("apiId");
		requestInfo.setVer("ver");
		requestInfo.setDid("did");
		return requestInfo;
	}


}
