package org.egov.egf.voucher.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.voucher.TestConfiguration;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.model.VoucherType;
import org.egov.egf.voucher.domain.service.VoucherSubTypeService;
import org.egov.egf.voucher.utils.RequestJsonReader;
import org.egov.egf.voucher.web.requests.VoucherSubTypeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(VoucherSubTypeController.class)
@Import(TestConfiguration.class)
public class VoucherSubTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VoucherSubTypeService voucherSubTypeService;

	@Captor
	private ArgumentCaptor<VoucherSubTypeRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create() throws IOException, Exception {

		when(voucherSubTypeService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getVoucherSubTypes());

		mockMvc.perform(post("/vouchersubtype/_create").content(resources.readRequest("vouchersubtype/vouchersubtype_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("vouchersubtype/vouchersubtype_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(voucherSubTypeService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getVoucherSubTypes());

		mockMvc.perform(post("/vouchersubtype/_create").content(resources.readRequest("vouchersubtype/vouchersubtype_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<VoucherSubType> voucherSubTypes = getVoucherSubTypes();
		voucherSubTypes.get(0).setVoucherName("BankToBankk");

		when(voucherSubTypeService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(voucherSubTypes);

		mockMvc.perform(post("/vouchersubtype/_update").content(resources.readRequest("vouchersubtype/vouchersubtype_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("vouchersubtype/vouchersubtype_update_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(voucherSubTypeService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getVoucherSubTypes()));

		mockMvc.perform(post("/vouchersubtype/_update").content(resources.readRequest("vouchersubtype/vouchersubtype_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<VoucherSubType> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getVoucherSubTypes());
		page.getPagedData().get(0).setId("1");

		when(voucherSubTypeService.search(any(VoucherSubTypeSearch.class)))
				.thenReturn(page);

		mockMvc.perform(post("/vouchersubtype/_search").content(resources.getRequestInfo()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("vouchersubtype/vouchersubtype_search_valid_response.json")));
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
