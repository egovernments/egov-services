package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.domain.repository.BillDetailRepository;
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
public class BillDetailServiceTest {

	private BillDetailService billDetailService;
	
	@Mock
	private BillDetailRepository billDetailRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		billDetailService = new BillDetailService(billDetailRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<BillDetail> expextedResult = getBillDetails();
		
		when(billDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillDetail> actualResult = billDetailService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillDetail> expextedResult = getBillDetails();

		when(billDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillDetail> actualResult = billDetailService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_search() {

		List<BillDetail> billDetails = getBillDetails();
		BillDetailSearch billDetailSearch = new BillDetailSearch();
		billDetailSearch.setTenantId("default");
		Pagination<BillDetail> expextedResult = new Pagination<>();

		expextedResult.setPagedData(billDetails);

		when(billDetailRepository.search(billDetailSearch)).thenReturn(expextedResult);

		Pagination<BillDetail> actualResult = billDetailService.search(billDetailSearch, errors);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		BillDetail expextedResult = getBillDetails().get(0);

		when(billDetailRepository.save(any(BillDetail.class))).thenReturn(expextedResult);

		BillDetail actualResult = billDetailService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_save_with_null_req() {

		List<BillDetail> expextedResult = getBillDetails();

		when(billDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillDetail> actualResult = billDetailService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		BillDetail expextedResult = getBillDetails().get(0);

		when(billDetailRepository.update(any(BillDetail.class))).thenReturn(expextedResult);

		BillDetail actualResult = billDetailService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillDetail> expextedResult = getBillDetails();

		when(billDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillDetail> actualResult = billDetailService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

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
