package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class BillRegisterServiceTest {

	private BillRegisterService billRegisterService;
	
	@Mock
	private BillRegisterRepository billRegisterRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Test
	public final void test_create() {

		List<BillRegister> expextedResult = getBillRegisters();
		
		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillRegister> actualResult = billRegisterService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_search() {

		List<BillRegister> billRegisters = getBillRegisters();
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setTenantId("default");
		Pagination<BillRegister> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billRegisters);

		when(billRegisterRepository.search(billRegisterSearch)).thenReturn(expextedResult);

		Pagination<BillRegister> actualResult = billRegisterService.search(billRegisterSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		BillRegister expextedResult = getBillRegisters().get(0);

		when(billRegisterRepository.save(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_save_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		BillRegister expextedResult = getBillRegisters().get(0);

		when(billRegisterRepository.update(any(BillRegister.class))).thenReturn(expextedResult);

		BillRegister actualResult = billRegisterService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_update_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

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
