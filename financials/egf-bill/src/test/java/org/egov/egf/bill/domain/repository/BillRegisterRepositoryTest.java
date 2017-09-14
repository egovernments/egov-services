package org.egov.egf.bill.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.queue.repository.BillRegisterQueueRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillRegisterJdbcRepository;
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
				billRegisterQueueRepository, financialConfigurationContractRepository,
				billRegisterESRepository, "yes");

		billRegisterRepositoryWithOutKafka = new BillRegisterRepository(
				billRegisterJdbcRepository, billDetailJdbcRepository, billPayeeDetailJdbcRepository,
				billRegisterQueueRepository, financialConfigurationContractRepository,
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
	        BillPayeeDetailEntity bpdEntity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0).
	        		getBillDetails().iterator().next().getBillPayeeDetails().iterator().next());

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(bdEntity);
	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(bpdEntity);

	        when(billRegisterJdbcRepository.create(any(BillRegisterEntity.class))).thenReturn(entity);

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

	        BillRegisterEntity entity = new BillRegisterEntity().toEntity(expectedResult.get(0));

	        when(billRegisterJdbcRepository.update(any(BillRegisterEntity.class))).thenReturn(entity);

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
			
			HashSet<BillPayeeDetail> sbpd=new HashSet<BillPayeeDetail>();
			sbpd.add(billPayeeDetail);
			billDetail.setBillPayeeDetails(sbpd);
			
			HashSet<BillDetail> sbd=new HashSet<BillDetail>();
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
			BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("billdetailglcode4321").debitAmount(new BigDecimal(10000))
					.creditAmount(new BigDecimal(10000)).build();
			billDetail.setTenantId("default");
			billPayeeDetail.setTenantId("default");
			billRegister.setTenantId("default");
			
			
			HashSet<BillPayeeDetail> sbpd=new HashSet<BillPayeeDetail>();
			sbpd.add(billPayeeDetail);
			billDetail.setBillPayeeDetails(sbpd);
			
			HashSet<BillDetail> sbd=new HashSet<BillDetail>();
			sbd.add(billDetail);
			
			billRegister.setBillDetails(sbd);
			
			billRegisters.add(billRegister);
			return billRegisters;
		}

}
