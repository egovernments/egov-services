package org.egov.egf.bill.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.persistence.queue.repository.BillChecklistQueueRepository;
import org.egov.egf.bill.persistence.repository.BillChecklistJdbcRepository;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BillChecklistRepositoryTest {

	private BillChecklistRepository billChecklistRepositoryWithKafka;

	private BillChecklistRepository billChecklistRepositoryWithOutKafka;
	
	@Mock
	private BillChecklistJdbcRepository billChecklistJdbcRepository;

	@Mock
	private BillChecklistQueueRepository billChecklistQueueRepository;

	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private BillChecklistESRepository billChecklistESRepository;

	@Captor
	private ArgumentCaptor<List<BillChecklist>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		billChecklistRepositoryWithKafka = new BillChecklistRepository(
				billChecklistJdbcRepository,
				billChecklistQueueRepository, financialConfigurationContractRepository,
				billChecklistESRepository, "yes");

		billChecklistRepositoryWithOutKafka = new BillChecklistRepository(
				billChecklistJdbcRepository,
				billChecklistQueueRepository, financialConfigurationContractRepository,
				billChecklistESRepository, "no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<BillChecklist> expectedResult = getBillChecklists();
	        
	        BillChecklistEntity entity = new BillChecklistEntity().toEntity(expectedResult.get(0));

	        when(billChecklistJdbcRepository.create(any(BillChecklistEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        billChecklistRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(billChecklistQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<BillChecklist> expectedResult = getBillChecklists();

	        BillChecklistEntity entity = new BillChecklistEntity().toEntity(expectedResult.get(0));

	        when(billChecklistJdbcRepository.create(any(BillChecklistEntity.class))).thenReturn(entity);

	        billChecklistRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(billChecklistQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<BillChecklist> expectedResult = getBillChecklists();

	        requestInfo.setAction("update");
	        billChecklistRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(billChecklistQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<BillChecklist> expectedResult = getBillChecklists();

	        BillChecklistEntity entity = new BillChecklistEntity().toEntity(expectedResult.get(0));

	        when(billChecklistJdbcRepository.update(any(BillChecklistEntity.class))).thenReturn(entity);

	        billChecklistRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(billChecklistQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_save() {

	    	BillChecklistEntity entity = getBillChecklistEntity();
	    	BillChecklist expectedResult = entity.toDomain();

	        when(billChecklistJdbcRepository.create(any(BillChecklistEntity.class))).thenReturn(entity);

	        BillChecklist actualResult = billChecklistRepositoryWithKafka.save(getBillChecklistDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
	    }
	    
	    @Test
	    public void test_unique_check() {

	    	Boolean exp = true;
	    	
	    	when(billChecklistJdbcRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(true);
	    	
	    	Boolean act = billChecklistRepositoryWithKafka.uniqueCheck("string", getBillChecklists().get(0));
	    	
	    	assertEquals(exp, act);
	    	
	    }
	    
	    @Test
	    public void test_find_by_id() {
	        final BillChecklistEntity entity = getBillChecklistEntity();
	        final BillChecklist expectedResult = entity.toDomain();

	        when(billChecklistJdbcRepository.findById(any(BillChecklistEntity.class))).thenReturn(entity);

	        final BillChecklist actualResult = billChecklistRepositoryWithKafka.findById(getBillChecklistDomin());

	    }

	    
	    private BillChecklistSearch getBillChecklistSearch() {
	    	BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
	    	billChecklistSearch.setPageSize(500);
	    	billChecklistSearch.setOffset(0);
	        return billChecklistSearch;

	    }
	    
	    private BillChecklist getBillChecklistDomin() {
	    	BillChecklist billChecklistDetail = new BillChecklist();
	    	billChecklistDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	billChecklistDetail.setTenantId("default");
	        return billChecklistDetail;
	    }
	    
	    private BillChecklistEntity getBillChecklistEntity() {
	    	BillChecklistEntity entity = new BillChecklistEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
	    
		private List<BillChecklist> getBillChecklists() {

			List<BillChecklist> billChecklists = new ArrayList<BillChecklist>();
			
			BillChecklist billChecklist = BillChecklist.builder().id("6").checklistValue("newValue").build();
			billChecklist.setBill(BillRegister.builder().id("29").build());
			billChecklist.setChecklist(Checklist.builder().id("4").build());
			billChecklist.setTenantId("default");
			
			billChecklists.add(billChecklist);
			return billChecklists;
		}

}
