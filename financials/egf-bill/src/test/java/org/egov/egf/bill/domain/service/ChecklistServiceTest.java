package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(BillTestConfiguration.class)
@RunWith(SpringRunner.class)
public class ChecklistServiceTest {

	private ChecklistService checklistService;
	
	@Mock
	private ChecklistRepository checklistRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		checklistService = new ChecklistService(checklistRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<Checklist> expextedResult = getChecklists();
		
		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(true);
		when(checklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<Checklist> actualResult = checklistService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = CustomBindException.class)
	public final void test_create_unique_false() {

		List<Checklist> expextedResult = getChecklists();
		
		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(false);
		when(checklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<Checklist> actualResult = checklistService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<Checklist> expextedResult = getChecklists();

		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(true);
		when(checklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Checklist> actualResult = checklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = CustomBindException.class)
	public final void test_update_unique_false() {

		List<Checklist> expextedResult = getChecklists();

		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(false);
		when(checklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Checklist> actualResult = checklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_null_id() {

		List<Checklist> expextedResult = getChecklists();
		expextedResult.get(0).setId(null);

		when(checklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Checklist> actualResult = checklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_null_req() {

		List<Checklist> checklists = getChecklists();
		ChecklistSearch checklistSearch = new ChecklistSearch();
		checklistSearch.setTenantId("default");
		Pagination<Checklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(checklists);

		when(checklistRepository.search(checklistSearch)).thenReturn(expextedResult);

		Pagination<Checklist> actualResult = checklistService.search(null, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_null_tenant() {

		List<Checklist> checklists = getChecklists();
		ChecklistSearch checklistSearch = new ChecklistSearch();
		Pagination<Checklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(checklists);

		when(checklistRepository.search(checklistSearch)).thenReturn(expextedResult);

		Pagination<Checklist> actualResult = checklistService.search(checklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_search() {

		List<Checklist> checklists = getChecklists();
		ChecklistSearch checklistSearch = new ChecklistSearch();
		checklistSearch.setTenantId("default");
		Pagination<Checklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(checklists);

		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(true);
		when(checklistRepository.search(checklistSearch)).thenReturn(expextedResult);

		Pagination<Checklist> actualResult = checklistService.search(checklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_search_unique_false() {

		List<Checklist> checklists = getChecklists();
		ChecklistSearch checklistSearch = new ChecklistSearch();
		checklistSearch.setTenantId("default");
		Pagination<Checklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(checklists);

		when(checklistRepository.uniqueCheck(any(String.class), any(String.class), any(Checklist.class))).thenReturn(false);
		when(checklistRepository.search(checklistSearch)).thenReturn(expextedResult);

		Pagination<Checklist> actualResult = checklistService.search(checklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		Checklist expextedResult = getChecklists().get(0);

		when(checklistRepository.save(any(Checklist.class))).thenReturn(expextedResult);

		Checklist actualResult = checklistService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_save_with_null_req() {

		List<Checklist> expextedResult = getChecklists();

		when(checklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Checklist> actualResult = checklistService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		Checklist expextedResult = getChecklists().get(0);

		when(checklistRepository.update(any(Checklist.class))).thenReturn(expextedResult);

		Checklist actualResult = checklistService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<Checklist> expextedResult = getChecklists();

		when(checklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Checklist> actualResult = checklistService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

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
