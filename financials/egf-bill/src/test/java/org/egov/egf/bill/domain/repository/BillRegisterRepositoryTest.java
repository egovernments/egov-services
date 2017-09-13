package org.egov.egf.bill.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
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

	        when(billRegisterJdbcRepository.create(any(BillRegisterEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        billRegisterRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(billRegisterQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<BillRegister> expectedResult = getBillRegisters();

	        BillRegisterEntity entity = new BillRegisterEntity().toEntity(expectedResult.get(0));

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
	    public void test_save() {

	    	BillRegisterEntity entity = getBillRegisterEntity();
	    	BillRegister expectedResult = entity.toDomain();

	        when(billRegisterJdbcRepository.create(any(BillRegisterEntity.class))).thenReturn(entity);

	        BillRegister actualResult = billRegisterRepositoryWithKafka.save(getBillRegisterDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
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
	    	BillRegister billRegisterDetail = new BillRegister();
	    	billRegisterDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	billRegisterDetail.setTenantId("default");
	        return billRegisterDetail;
	    }
	    
	    private BillRegisterEntity getBillRegisterEntity() {
	    	BillRegisterEntity entity = new BillRegisterEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<BillRegister> getBillRegisters() {

			List<BillRegister> billRegisters = new ArrayList<BillRegister>();
			
			BillRegister billRegister = BillRegister.builder().id("b96561462fdc484fa97fa72c3944ad89")
					.build();
			billRegister.setTenantId("default");
			
			billRegisters.add(billRegister);
			return billRegisters;
		}

}
