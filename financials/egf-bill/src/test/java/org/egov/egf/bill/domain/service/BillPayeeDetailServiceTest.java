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
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.repository.BillPayeeDetailRepository;
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
public class BillPayeeDetailServiceTest {

	private BillPayeeDetailService billPayeeDetailService;
	
	@Mock
	private BillPayeeDetailRepository billPayeeDetailRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		billPayeeDetailService = new BillPayeeDetailService(billPayeeDetailRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();
		
		when(billPayeeDetailRepository.uniqueCheck(any(String.class), any(BillPayeeDetail.class))).thenReturn(true);
		when(billPayeeDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillPayeeDetail> actualResult = billPayeeDetailService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();

		when(billPayeeDetailRepository.uniqueCheck(any(String.class), any(BillPayeeDetail.class))).thenReturn(true);
		when(billPayeeDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillPayeeDetail> actualResult = billPayeeDetailService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = CustomBindException.class)
	public final void test_create_unique_id_false() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();
		
		when(billPayeeDetailRepository.uniqueCheck(any(String.class), any(BillPayeeDetail.class))).thenReturn(false);
		when(billPayeeDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<BillPayeeDetail> actualResult = billPayeeDetailService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = CustomBindException.class)
	public final void test_update_unique_id_false() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();

		when(billPayeeDetailRepository.uniqueCheck(any(String.class), any(BillPayeeDetail.class))).thenReturn(false);
		when(billPayeeDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillPayeeDetail> actualResult = billPayeeDetailService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_null_id() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();
		expextedResult.get(0).setId(null);

		when(billPayeeDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillPayeeDetail> actualResult = billPayeeDetailService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_save() {

		BillPayeeDetail expextedResult = getBillPayeeDetails().get(0);

		when(billPayeeDetailRepository.save(any(BillPayeeDetail.class))).thenReturn(expextedResult);

		BillPayeeDetail actualResult = billPayeeDetailService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_save_with_null_req() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();

		when(billPayeeDetailRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillPayeeDetail> actualResult = billPayeeDetailService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		BillPayeeDetail expextedResult = getBillPayeeDetails().get(0);

		when(billPayeeDetailRepository.update(any(BillPayeeDetail.class))).thenReturn(expextedResult);

		BillPayeeDetail actualResult = billPayeeDetailService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = InvalidDataException.class)
	public final void test_update_with_null_req() {

		List<BillPayeeDetail> expextedResult = getBillPayeeDetails();

		when(billPayeeDetailRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<BillPayeeDetail> actualResult = billPayeeDetailService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	private List<BillPayeeDetail> getBillPayeeDetails() {

		List<BillPayeeDetail> billPayeeDetails = new ArrayList<BillPayeeDetail>();
		
		BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("9").amount(new BigDecimal(1234)).build();
		billPayeeDetail.setTenantId("default");
		
		billPayeeDetails.add(billPayeeDetail);
		return billPayeeDetails;
	}
	
}
