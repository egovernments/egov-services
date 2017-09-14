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
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.egov.egf.bill.persistence.queue.repository.ChecklistQueueRepository;
import org.egov.egf.bill.persistence.repository.ChecklistJdbcRepository;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChecklistRepositoryTest {

	private ChecklistRepository checklistRepositoryWithKafka;

	private ChecklistRepository checklistRepositoryWithOutKafka;
	
	@Mock
	private ChecklistJdbcRepository checklistJdbcRepository;

	@Mock
	private ChecklistQueueRepository checklistQueueRepository;

	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private ChecklistESRepository checklistESRepository;

	@Captor
	private ArgumentCaptor<List<Checklist>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		checklistRepositoryWithKafka = new ChecklistRepository(
				checklistJdbcRepository,
				checklistQueueRepository, financialConfigurationContractRepository,
				checklistESRepository, "yes");

		checklistRepositoryWithOutKafka = new ChecklistRepository(
				checklistJdbcRepository,
				checklistQueueRepository, financialConfigurationContractRepository,
				checklistESRepository, "no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<Checklist> expectedResult = getChecklists();
	        
	        ChecklistEntity entity = new ChecklistEntity().toEntity(expectedResult.get(0));

	        when(checklistJdbcRepository.create(any(ChecklistEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        checklistRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(checklistQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<Checklist> expectedResult = getChecklists();

	        ChecklistEntity entity = new ChecklistEntity().toEntity(expectedResult.get(0));

	        when(checklistJdbcRepository.create(any(ChecklistEntity.class))).thenReturn(entity);

	        checklistRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(checklistQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<Checklist> expectedResult = getChecklists();

	        requestInfo.setAction("update");
	        checklistRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(checklistQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<Checklist> expectedResult = getChecklists();

	        ChecklistEntity entity = new ChecklistEntity().toEntity(expectedResult.get(0));

	        when(checklistJdbcRepository.update(any(ChecklistEntity.class))).thenReturn(entity);

	        checklistRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(checklistQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_save() {

	    	ChecklistEntity entity = getChecklistEntity();
	    	Checklist expectedResult = entity.toDomain();

	        when(checklistJdbcRepository.create(any(ChecklistEntity.class))).thenReturn(entity);

	        Checklist actualResult = checklistRepositoryWithKafka.save(getChecklistDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
	    }
	    
	    @Test
	    public void test_search() {

	        Pagination<Checklist> expectedResult = new Pagination<>();
	        expectedResult.setPageSize(500);
	        expectedResult.setOffset(0);

	        when(financialConfigurationContractRepository.fetchDataFrom()).thenReturn("db");
	        when(checklistJdbcRepository.search(any(ChecklistSearch.class))).thenReturn(expectedResult);

	        Pagination<Checklist> actualResult = checklistRepositoryWithKafka.search(getChecklistSearch());

	        assertEquals(expectedResult, actualResult);

	    }
	    
	    private ChecklistSearch getChecklistSearch() {
	    	ChecklistSearch checklistSearch = new ChecklistSearch();
	    	checklistSearch.setPageSize(500);
	    	checklistSearch.setOffset(0);
	        return checklistSearch;

	    }
	    
	    private Checklist getChecklistDomin() {
	    	Checklist checklistDetail = new Checklist();
	    	checklistDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	checklistDetail.setTenantId("default");
	        return checklistDetail;
	    }
	    
	    private ChecklistEntity getChecklistEntity() {
	    	ChecklistEntity entity = new ChecklistEntity();
	        entity.setId("");
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<Checklist> getChecklists() {

			List<Checklist> checklists = new ArrayList<Checklist>();
			
			Checklist checklist = Checklist.builder().id("9").type("checklisttype").
									subType("checklistSubType").key("checklistkey").description("description")
									.build();
			checklist.setTenantId("default");
			
			checklists.add(checklist);
			return checklists;
		}

}
