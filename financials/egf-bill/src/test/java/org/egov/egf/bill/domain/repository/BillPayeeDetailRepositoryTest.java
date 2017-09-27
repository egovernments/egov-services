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
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetailSearch;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.persistence.queue.repository.BillPayeeDetailQueueRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BillPayeeDetailRepositoryTest {

	private BillPayeeDetailRepository billPayeeDetailRepositoryWithKafka;

	private BillPayeeDetailRepository billPayeeDetailRepositoryWithOutKafka;
	
	@Mock
	private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

	@Mock
	private BillPayeeDetailQueueRepository billPayeeDetailQueueRepository;

	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private BillPayeeDetailESRepository billPayeeDetailESRepository;

	@Captor
	private ArgumentCaptor<List<BillPayeeDetail>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		billPayeeDetailRepositoryWithKafka = new BillPayeeDetailRepository(
				billPayeeDetailJdbcRepository,
				billPayeeDetailQueueRepository,
				"yes");

		billPayeeDetailRepositoryWithOutKafka = new BillPayeeDetailRepository(
				billPayeeDetailJdbcRepository,
				billPayeeDetailQueueRepository,
				"no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<BillPayeeDetail> expectedResult = getBillPayeeDetails();
	        
	        BillPayeeDetailEntity entity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0));

	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        billPayeeDetailRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(billPayeeDetailQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<BillPayeeDetail> expectedResult = getBillPayeeDetails();

	        BillPayeeDetailEntity entity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0));

	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(entity);

	        billPayeeDetailRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(billPayeeDetailQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<BillPayeeDetail> expectedResult = getBillPayeeDetails();

	        requestInfo.setAction("update");
	        billPayeeDetailRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(billPayeeDetailQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<BillPayeeDetail> expectedResult = getBillPayeeDetails();

	        BillPayeeDetailEntity entity = new BillPayeeDetailEntity().toEntity(expectedResult.get(0));

	        when(billPayeeDetailJdbcRepository.update(any(BillPayeeDetailEntity.class))).thenReturn(entity);

	        billPayeeDetailRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(billPayeeDetailQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_save() {

	    	BillPayeeDetailEntity entity = getBillPayeeDetailEntity();
	    	BillPayeeDetail expectedResult = entity.toDomain();

	        when(billPayeeDetailJdbcRepository.create(any(BillPayeeDetailEntity.class))).thenReturn(entity);

	        BillPayeeDetail actualResult = billPayeeDetailRepositoryWithKafka.save(getBillPayeeDetailDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
	    }
	    
	    private BillPayeeDetailSearch getBillPayeeDetailSearch() {
	    	BillPayeeDetailSearch billPayeeDetailSearch = new BillPayeeDetailSearch();
	    	billPayeeDetailSearch.setPageSize(500);
	    	billPayeeDetailSearch.setOffset(0);
	        return billPayeeDetailSearch;

	    }
	    
	    private BillPayeeDetail getBillPayeeDetailDomin() {
	    	BillPayeeDetail billPayeeDetailDetail = new BillPayeeDetail();
	    	billPayeeDetailDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	billPayeeDetailDetail.setTenantId("default");
	        return billPayeeDetailDetail;
	    }
	    
	    private BillPayeeDetailEntity getBillPayeeDetailEntity() {
	    	BillPayeeDetailEntity entity = new BillPayeeDetailEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<BillPayeeDetail> getBillPayeeDetails() {

			List<BillPayeeDetail> billPayeeDetails = new ArrayList<BillPayeeDetail>();
			
			BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("9").amount(new BigDecimal(1234)).build();
			billPayeeDetail.setTenantId("default");
			
			billPayeeDetails.add(billPayeeDetail);
			return billPayeeDetails;
		}

}
