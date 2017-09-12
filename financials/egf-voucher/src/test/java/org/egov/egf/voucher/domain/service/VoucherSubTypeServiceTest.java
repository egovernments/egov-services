package org.egov.egf.voucher.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.model.VoucherType;
import org.egov.egf.voucher.domain.repository.VoucherSubTypeRepository;
import org.junit.Before;
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
public class VoucherSubTypeServiceTest {

	private VoucherSubTypeService voucherSubTypeService;
	
	@Mock
	private VoucherSubTypeRepository voucherSubTypeRepository;
	
	@Mock
	private SmartValidator validator;
	
	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();
	
	@Before
	public void setup() {
		voucherSubTypeService = new VoucherSubTypeService(voucherSubTypeRepository, validator);
	}
	
	@Test
	public final void test_create() {

		List<VoucherSubType> expextedResult = getVoucherSubTypes();
		
		when(voucherSubTypeRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);
		
		List<VoucherSubType> actualResult = voucherSubTypeService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_update() {

		List<VoucherSubType> expextedResult = getVoucherSubTypes();

		when(voucherSubTypeRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<VoucherSubType> actualResult = voucherSubTypeService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	@Test
	public final void test_search() {

		List<VoucherSubType> voucherSubTypes = getVoucherSubTypes();
		VoucherSubTypeSearch voucherSubTypeSearch = new VoucherSubTypeSearch();
		voucherSubTypeSearch.setTenantId("default");
		Pagination<VoucherSubType> expextedResult = new Pagination<>();

		expextedResult.setPagedData(voucherSubTypes);

		when(voucherSubTypeRepository.search(voucherSubTypeSearch)).thenReturn(expextedResult);

		Pagination<VoucherSubType> actualResult = voucherSubTypeService.search(voucherSubTypeSearch);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test
	public final void test_save() {

		VoucherSubType expextedResult = getVoucherSubTypes().get(0);

		when(voucherSubTypeRepository.save(any(VoucherSubType.class))).thenReturn(expextedResult);

		VoucherSubType actualResult = voucherSubTypeService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_save_with_null_req() {

		List<VoucherSubType> expextedResult = getVoucherSubTypes();

		when(voucherSubTypeRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<VoucherSubType> actualResult = voucherSubTypeService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update1() {

		VoucherSubType expextedResult = getVoucherSubTypes().get(0);

		when(voucherSubTypeRepository.update(any(VoucherSubType.class))).thenReturn(expextedResult);

		VoucherSubType actualResult = voucherSubTypeService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}
	
	@Test(expected = CustomBindException.class)
	public final void test_update_with_null_req() {

		List<VoucherSubType> expextedResult = getVoucherSubTypes();

		when(voucherSubTypeRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<VoucherSubType> actualResult = voucherSubTypeService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}
	
	private List<VoucherSubType> getVoucherSubTypes() {

		List<VoucherSubType> voucherSubTypes = new ArrayList<VoucherSubType>();
		
		VoucherSubType voucherSubType = VoucherSubType.builder().id("b96561462fdc484fa97fa72c3944ad89")
				.voucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA)
				.voucherName("BankToBank").exclude(true).build();
		voucherSubType.setTenantId("default");
		
		voucherSubTypes.add(voucherSubType);
		return voucherSubTypes;
	}
	
}
