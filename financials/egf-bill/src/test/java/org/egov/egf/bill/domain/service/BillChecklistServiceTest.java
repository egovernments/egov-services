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
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.repository.BillChecklistRepository;
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
public class BillChecklistServiceTest {

	private BillChecklistService billChecklistService;
	
	@Mock
	private BillChecklistRepository billChecklistRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		billChecklistService = new BillChecklistService(billChecklistRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<BillChecklist> expextedResult = getBillChecklists();
		
		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(true);
		when(billChecklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillChecklist> actualResult = billChecklistService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillChecklist> expextedResult = getBillChecklists();

		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(true);
		when(billChecklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillChecklist> actualResult = billChecklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_search() {

		List<BillChecklist> billChecklists = getBillChecklists();
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		billChecklistSearch.setTenantId("default");
		Pagination<BillChecklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billChecklists);

		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(true);
		when(billChecklistRepository.search(billChecklistSearch)).thenReturn(expextedResult);

		Pagination<BillChecklist> actualResult = billChecklistService.search(billChecklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected=CustomBindException.class)
	public final void test_create_unique_false() {

		List<BillChecklist> expextedResult = getBillChecklists();
		
		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(false);
		when(billChecklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillChecklist> actualResult = billChecklistService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected=CustomBindException.class)
	public final void test_update_unique_false() {

		List<BillChecklist> expextedResult = getBillChecklists();

		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(false);
		when(billChecklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillChecklist> actualResult = billChecklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected=CustomBindException.class)
	public final void test_search_unique_false() {

		List<BillChecklist> billChecklists = getBillChecklists();
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		billChecklistSearch.setTenantId("default");
		Pagination<BillChecklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billChecklists);

		when(billChecklistRepository.uniqueCheck(any(String.class), any(BillChecklist.class))).thenReturn(false);
		when(billChecklistRepository.search(billChecklistSearch)).thenReturn(expextedResult);

		Pagination<BillChecklist> actualResult = billChecklistService.search(billChecklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_with_null_req() {

		List<BillChecklist> billChecklists = getBillChecklists();
		Pagination<BillChecklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billChecklists);

		when(billChecklistRepository.search(any(BillChecklistSearch.class))).thenReturn(expextedResult);

		Pagination<BillChecklist> actualResult = billChecklistService.search(null, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_search_null_tenant() {

		List<BillChecklist> billChecklists = getBillChecklists();
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		Pagination<BillChecklist> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billChecklists);

		when(billChecklistRepository.search(any(BillChecklistSearch.class))).thenReturn(expextedResult);

		Pagination<BillChecklist> actualResult = billChecklistService.search(billChecklistSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		BillChecklist expextedResult = getBillChecklists().get(0);

		when(billChecklistRepository.save(any(BillChecklist.class))).thenReturn(expextedResult);

		BillChecklist actualResult = billChecklistService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_save_with_null_req() {

		List<BillChecklist> expextedResult = getBillChecklists();

		when(billChecklistRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillChecklist> actualResult = billChecklistService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		BillChecklist expextedResult = getBillChecklists().get(0);

		when(billChecklistRepository.update(any(BillChecklist.class))).thenReturn(expextedResult);

		BillChecklist actualResult = billChecklistService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillChecklist> expextedResult = getBillChecklists();

		when(billChecklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillChecklist> actualResult = billChecklistService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_id() {

		List<BillChecklist> expextedResult = getBillChecklists();
		expextedResult.get(0).setId(null);

		when(billChecklistRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillChecklist> actualResult = billChecklistService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

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
