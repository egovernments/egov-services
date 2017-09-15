package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.repository.BillDetailRepository;
import org.egov.egf.bill.domain.repository.BillPayeeDetailRepository;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
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
public class BillRegisterServiceTest {

	private BillRegisterService billRegisterService;
	
	@Mock
	private BillRegisterRepository billRegisterRepository;
	
	@Mock
	private BillPayeeDetailRepository billPayeeDetailRepository;
	
	@Mock
	private BillDetailRepository billDetailRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		billRegisterService = new BillRegisterService(billRegisterRepository, validator);
	}
	
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
	
	@Test(expected = InvalidDataException.class)
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
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillRegister> expextedResult = getBillRegisters();

		when(billRegisterRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillRegister> actualResult = billRegisterService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	private List<BillRegister> getBillRegisters() {

		List<BillRegister> billRegisters = new ArrayList<BillRegister>();
		
		BillRegister billRegister = BillRegister.builder().id("30").billType("billtype4321").billAmount(new BigDecimal(4321)).build();
//		BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("5").build();
//		BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("billdetailglcode4321").debitAmount(new BigDecimal(10000))
//				.creditAmount(new BigDecimal(10000)).build();
//		billDetail.setTenantId("default");
//		billPayeeDetail.setTenantId("default");
		billRegister.setTenantId("default");
		billRegisters.add(billRegister);
		return billRegisters;
	}
	
}
