package org.egov.egf.bill.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillChecklistJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
import org.egov.egf.bill.web.contract.BillRegisterSearchContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BillRegisterRepositoryTest {

	private BillRegisterRepository billRegisterRepositoryWithKafka;

	private BillRegisterRepository billRegisterRepositoryWithOutKafka;
	
	@Mock
	private BillRegisterJdbcRepository billRegisterJdbcRepository;

	@Mock
	private BillDetailJdbcRepository billDetailJdbcRepository;
	
	@Mock
	private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;
	
	@Mock
	private BillRegisterQueueRepository billRegisterQueueRepository;

	@Mock
	private BillChecklistJdbcRepository billChecklistJdbcReepository;
	
	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private BillRegisterESRepository billRegisterESRepository;

	@Captor
	private ArgumentCaptor<List<BillRegister>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		billRegisterRepositoryWithKafka = new BillRegisterRepository(
				billRegisterJdbcRepository, billDetailJdbcRepository, billPayeeDetailJdbcRepository,
				billRegisterQueueRepository, billChecklistJdbcReepository, financialConfigurationContractRepository,
				billRegisterESRepository, "yes");

		billRegisterRepositoryWithOutKafka = new BillRegisterRepository(
				billRegisterJdbcRepository, billDetailJdbcRepository, billPayeeDetailJdbcRepository,
				billRegisterQueueRepository, billChecklistJdbcReepository, financialConfigurationContractRepository,
				billRegisterESRepository, "no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<BillRegister> expectedResult = getBillRegisters();
	        
	        BillRegisterEntity entity = new BillRegisterEntity().toEntity(expectedResult.get(0));
	        BillDetailEntity bdEntity = new BillDetailEntity().toEntity(expectedResult.get(0).getBillDetails().iterator().next());
	        BillPayeeDetailEntity bpdEntity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0).
	        		getBillDetails().iterator().next().getBillPayeeDetails().iterator().next());

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(bdEntity);
	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(bpdEntity);
	        when(billRegisterJdbcRepository.create(any(BillRegisterEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        billRegisterRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(billRegisterQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<BillRegister> expectedResult = getBillRegisters();

	        BillRegisterEntity entity = new BillRegisterEntity().toEntity(expectedResult.get(0));
	        BillDetailEntity bdEntity = new BillDetailEntity().toEntity(expectedResult.get(0).getBillDetails().iterator().next());
	        BillChecklistEntity bclEntity = new BillChecklistEntity().toEntity(expectedResult.get(0).getCheckLists().get(0));
	        BillPayeeDetailEntity bpdEntity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0).
	        		getBillDetails().iterator().next().getBillPayeeDetails().iterator().next());

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(bdEntity);
	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(bpdEntity);

	        when(billRegisterJdbcRepository.create(any(BillRegisterEntity.class))).thenReturn(entity);
	        when(billChecklistJdbcReepository.create(any(BillChecklistEntity.class))).thenReturn(bclEntity);
	        billRegisterRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(billRegisterQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<BillRegister> expectedResult = getBillRegisters();

	        requestInfo.setAction("update");
	        billRegisterRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(billRegisterQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<BillRegister> expectedResult = getBillRegisters();
	        BillDetailEntity bdEntity = new BillDetailEntity().toEntity(expectedResult.get(0).getBillDetails().iterator().next());
	        BillPayeeDetailEntity bpdEntity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0).
	        		getBillDetails().iterator().next().getBillPayeeDetails().iterator().next());
	        BillRegisterEntity entity = new BillRegisterEntity().toEntity(expectedResult.get(0));
	        
	        when(billPayeeDetailJdbcRepository.delete(any(BillPayeeDetailEntity.class))).thenReturn(bpdEntity);
	        when(billDetailJdbcRepository.delete(any(BillDetailEntity.class))).thenReturn(bdEntity);
	        when(financialConfigurationContractRepository.fetchDataFrom()).thenReturn("");
	        when(billRegisterJdbcRepository.update(any(BillRegisterEntity.class))).thenReturn(entity);
	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(bdEntity);
	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(bpdEntity);

	        billRegisterRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(billRegisterQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_search() {

	        Pagination<BillRegister> expectedResult = new Pagination<>();
	        expectedResult.setPageSize(500);
	        expectedResult.setOffset(0);

	        when(financialConfigurationContractRepository.fetchDataFrom()).thenReturn("db");
	        when(billRegisterJdbcRepository.search(any(BillRegisterSearch.class))).thenReturn(expectedResult);

	        Pagination<BillRegister> actualResult = billRegisterRepositoryWithKafka.search(getBillRegisterSearch());

	        assertEquals(expectedResult, actualResult);

	    }
	    
	    @Test
	    public void test_search1() {

	        Pagination<BillRegister> expectedResult = new Pagination<>();
	        expectedResult.setPageSize(500);
	        expectedResult.setOffset(0);

	        when(financialConfigurationContractRepository.fetchDataFrom()).thenReturn("es");
	        when(billRegisterESRepository.search(any(BillRegisterSearchContract.class))).thenReturn(expectedResult);

	        Pagination<BillRegister> actualResult = billRegisterRepositoryWithKafka.search(getBillRegisterSearch());

	        assertEquals(expectedResult, actualResult);

	    }
	    
	    @Test
	    public void test_unique_check() {
	    	Boolean expectedResult = true;
	    	when(billRegisterJdbcRepository.uniqueCheck(any(String.class), any(BillRegisterEntity.class))).thenReturn(true);
	    	Boolean actualResult = billRegisterJdbcRepository.uniqueCheck("id", getBillRegisterEntity());
	        assertEquals(expectedResult, actualResult);
	    }
	    
	    private BillRegisterSearch getBillRegisterSearch() {
	    	BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
	    	billRegisterSearch.setPageSize(500);
	    	billRegisterSearch.setOffset(0);
	        return billRegisterSearch;

	    }
	    
	    private BillRegister getBillRegisterDomin() {
			BillRegister billRegister = BillRegister.builder().id("30").billType("billtype4321").billAmount(new BigDecimal(4321)).build();
			BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("5").build();
			BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("billdetailglcode4321").debitAmount(new BigDecimal(10000))
					.creditAmount(new BigDecimal(10000)).build();
			billDetail.setTenantId("default");
			billPayeeDetail.setTenantId("default");
			billRegister.setTenantId("default");
			
			List<BillPayeeDetail> sbpd=new ArrayList<BillPayeeDetail>();
			sbpd.add(billPayeeDetail);
			billDetail.setBillPayeeDetails(sbpd);
			
			List<BillDetail> sbd=new ArrayList<BillDetail>();
			sbd.add(billDetail);
			
			billRegister.setBillDetails(sbd);

	        return billRegister;
	    }
	    
	    private BillRegisterEntity getBillRegisterEntity() {
	    	BillRegisterEntity entity = new BillRegisterEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<BillRegister> getBillRegisters() {

			List<BillRegister> billRegisters = new ArrayList<BillRegister>();
			
			BillRegister billRegister = BillRegister.builder().id("30").billType("billtype4321").billAmount(new BigDecimal(4321)).build();
			BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("5").build();
			BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("1").debitAmount(new BigDecimal(10000))
					.creditAmount(new BigDecimal(10000)).chartOfAccount(getChartOfAccountContract()).build();
			Checklist checklist = Checklist.builder().id("1").build();
			checklist.setTenantId("default");
			BillChecklist billChecklist = BillChecklist.builder().id("1").checklist(checklist).build();
			
			List bclList = new ArrayList<>();
			bclList.add(billChecklist);
			
			billDetail.setTenantId("default");
			billPayeeDetail.setTenantId("default");
			
			List bpdList = new ArrayList<>();
			bpdList.add(billPayeeDetail);
			
			billDetail.setBillPayeeDetails(bpdList);
			List bdList = new ArrayList<>();
			bdList.add(billDetail);
			
			billRegister.setBillDetails(bdList);
			billRegister.setCheckLists(bclList);
			billRegister.setTenantId("default");
			billRegisters.add(billRegister);
			return billRegisters;
		}
		
		private ChartOfAccountContract getChartOfAccountContract() {
			ChartOfAccountContract chartOfAccount = ChartOfAccountContract.builder().glcode("1").build();
			chartOfAccount.setTenantId("default");
			return chartOfAccount;
		}

}
