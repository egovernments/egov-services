package org.egov.demand.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.helper.BillHelper;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillSearchCriteria;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.model.enums.Category;
import org.egov.demand.repository.BillRepository;
import org.egov.demand.util.FileUtils;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(PowerMockRunner.class)
@PrepareForTest(BillService.class)
public class BillServiceTest {

	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private BillRepository billRepository;

	@Mock
	private BillHelper billHelper;

	@Mock
	private DemandService demandService;

	@Mock
	private BusinessServDetailService businessServDetailService;

	@Mock
	private TaxHeadMasterService taxHeadMasterService;
	
	@Mock
	private GlCodeMasterService glCodeMasterService;

//	@Spy
	@InjectMocks
	private BillService billService;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testsearchBill(){
		Bill bill=getBills();
		List<Bill> bills=new ArrayList<Bill>();
		bills.add(bill);
		BillResponse billResponse=new BillResponse();
		billResponse.setBill(bills);
		
		when(billRepository.findBill(Matchers.any(BillSearchCriteria.class)))
		.thenReturn(bills);
		
		BillSearchCriteria billSearchcriteria=BillSearchCriteria.builder().tenantId("ap.kurnool").build();
		
		assertEquals(billResponse, billService.searchBill(billSearchcriteria, new RequestInfo()));
	}
	
	@Test
	public void testCreateAsync() {
		Bill bill=getBills();
		BillRequest billRequest=new BillRequest();
		List<Bill> bills=new ArrayList<Bill>();
		bills.add(bill);
		billRequest.setBills(bills);
		
		BillResponse billResponse=new BillResponse();
		billResponse.setBill(bills);
		billResponse.setResposneInfo(null);
		
		doNothing().when(billHelper).getBillRequestWithIds(any(BillRequest.class));
		assertTrue(billResponse.equals(billService.createAsync(billRequest)));

		when(billRepository.findBill(Matchers.any(BillSearchCriteria.class)))
		.thenReturn(bills);
		
		BillSearchCriteria billCriteria=BillSearchCriteria.builder().tenantId("ap.kurnool").build();
		
		assertEquals(billResponse, billService.searchBill(billCriteria, new RequestInfo()));
		
	}
	
	@Test
	public void generateBill() throws Exception{
		
		TaxHeadMasterResponse taxHeadMasterResponse=new TaxHeadMasterResponse();
		DemandResponse demandResponse=new DemandResponse();
		GlCodeMasterResponse glCodeResponse=new GlCodeMasterResponse();
		List<TaxHeadMaster> taxHeadMasters=new ArrayList<TaxHeadMaster>();
		List<Bill> bills=new ArrayList<Bill>();
		
		Bill bill=getBills();
		bills.add(bill);
		BillResponse billResponse=new BillResponse();
		billResponse=getBillResp("billCreateResponse.json");
		BillRequest billRequest=new BillRequest();
		billRequest.setBills(bills);
		billRequest.setRequestInfo(new RequestInfo());
//		billResponse.setBill(bills);
//		billResponse.setResposneInfo(null);
		
		Demand demand=getDemand();
		demandResponse.getDemands().add(demand);
		
		taxHeadMasters.add(getTaxHeadMaster());
		taxHeadMasterResponse.setTaxHeadMasters(taxHeadMasters);
		taxHeadMasterResponse.setResponseInfo(new ResponseInfo());
		
		GlCodeMaster glCodeMaster=getGlCodeMaster();
		glCodeResponse.getGlCodeMasters().add(glCodeMaster);
		glCodeResponse.setResponseInfo(new ResponseInfo());

		
		 
		final BillService mock = PowerMockito.mock(BillService.class);
		
		mock.generateBill(GenerateBillCriteria.builder().tenantId("ap.kurnool").build(), new RequestInfo());
		when(mock.createAsync(any(BillRequest.class))).thenReturn(billResponse);
		
		PowerMockito.doReturn(bills).when(mock,"prepareBill",
				any(List.class),any(String.class),any(RequestInfo.class));
		/*assertTrue(billResponse.equals(mock.generateBill(GenerateBillCriteria.builder().demandId("12")
				.tenantId("ap.kurnool").build(),new RequestInfo())));*/
		assertTrue(billResponse.getBill().get(0).equals(billRequest.getBills().get(0)));
	}
	
	
	
	private BillResponse getBillResp(final String filePath) throws IOException {
		final String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, BillResponse.class);
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
		DemandDetail detail=new DemandDetail();
		detail.setTenantId("ap.kurnool");
		detail.setId("12");
		List<DemandDetail> details=new ArrayList<DemandDetail>();
		details.add(detail);
		detail.setTaxHeadMasterCode("qwert");
		detail.setTaxAmount(new BigDecimal("2.4"));
		demand.setDemandDetails(details);
		return demand;
	}
	
	private Bill getBills(){
		Bill bill=new Bill();
		bill.setId("12");
		bill.setIsActive(true);
		bill.setIsCancelled(true);
		bill.setPayeeAddress("bangalore");
		bill.setPayeeEmail("abc@xyz.com");
		bill.setPayeeName("abcd");
		bill.setTenantId("ap.kurnool");
		bill.setBillDetails(null);
		
		return bill;
	}
	
	private TaxHeadMaster getTaxHeadMaster() {
		TaxHeadMaster taxHeadMaster = new TaxHeadMaster();
		taxHeadMaster.setId("23");
		taxHeadMaster.setCode("string");
		taxHeadMaster.setTenantId("ap.kurnool");
		taxHeadMaster.setCategory(Category.fromValue("TAX"));
		taxHeadMaster.setService("string");
		taxHeadMaster.setName("string");
		taxHeadMaster.setIsDebit(true);
		taxHeadMaster.setIsActualDemand(true);
		
		taxHeadMaster.setValidFrom(324l);
		taxHeadMaster.setValidTill(23l);
		taxHeadMaster.setOrder(12);
	return taxHeadMaster;
	}
	
	private GlCodeMaster getGlCodeMaster(){
		GlCodeMaster glCodeMaster=new GlCodeMaster();
		
		glCodeMaster.setId("12");
		glCodeMaster.setService("string");
		glCodeMaster.setTaxHead("string");
		glCodeMaster.setTenantId("ap.kurnool");
		glCodeMaster.setGlCode("string");
		glCodeMaster.setFromDate(0l);
		glCodeMaster.setToDate(0l);
		
		return glCodeMaster;
	}

}
