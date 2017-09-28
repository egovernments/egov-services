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
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.persistence.queue.repository.BillDetailQueueRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BillDetailRepositoryTest {

	private BillDetailRepository billDetailRepositoryWithKafka;

	private BillDetailRepository billDetailRepositoryWithOutKafka;
	
	@Mock
	private BillDetailJdbcRepository billDetailJdbcRepository;

	@Mock
	private BillDetailQueueRepository billDetailQueueRepository;

	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private BillDetailESRepository billDetailESRepository;

	@Captor
	private ArgumentCaptor<List<BillDetail>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		billDetailRepositoryWithKafka = new BillDetailRepository(
				billDetailJdbcRepository,
				billDetailQueueRepository,
				"yes");

		billDetailRepositoryWithOutKafka = new BillDetailRepository(
				billDetailJdbcRepository,
				billDetailQueueRepository,
				"no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<BillDetail> expectedResult = getBillDetails();
	        
	        BillDetailEntity entity = new BillDetailEntity().toEntity(expectedResult.get(0));

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        billDetailRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(billDetailQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<BillDetail> expectedResult = getBillDetails();

	        BillDetailEntity entity = new BillDetailEntity().toEntity(expectedResult.get(0));

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(entity);

	        billDetailRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(billDetailQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<BillDetail> expectedResult = getBillDetails();

	        requestInfo.setAction("update");
	        billDetailRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(billDetailQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<BillDetail> expectedResult = getBillDetails();

	        BillDetailEntity entity = new BillDetailEntity().toEntity(expectedResult.get(0));

	        when(billDetailJdbcRepository.update(any(BillDetailEntity.class))).thenReturn(entity);

	        billDetailRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(billDetailQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_save() {

	    	BillDetailEntity entity = getBillDetailEntity();
	    	BillDetail expectedResult = entity.toDomain();

	        when(billDetailJdbcRepository.create(any(BillDetailEntity.class))).thenReturn(entity);

	        BillDetail actualResult = billDetailRepositoryWithKafka.save(getBillDetailDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
	    }
	    
	    private BillDetailSearch getBillDetailSearch() {
	    	BillDetailSearch billDetailSearch = new BillDetailSearch();
	    	billDetailSearch.setPageSize(500);
	    	billDetailSearch.setOffset(0);
	        return billDetailSearch;

	    }
	    
	    private BillDetail getBillDetailDomin() {
	    	BillDetail billDetailDetail = new BillDetail();
	    	billDetailDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	billDetailDetail.setTenantId("default");
	        return billDetailDetail;
	    }
	    
	    private BillDetailEntity getBillDetailEntity() {
	    	BillDetailEntity entity = new BillDetailEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<BillDetail> getBillDetails() {

			List<BillDetail> billDetails = new ArrayList<BillDetail>();
			
			BillDetail billDetail = BillDetail.builder().id("9").orderId(1).glcode("1").debitAmount(new BigDecimal(1234)).creditAmount(new BigDecimal(1234))
									.build();
			billDetail.setTenantId("default");
			
			billDetails.add(billDetail);
			return billDetails;
		}

}
