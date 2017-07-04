package org.egov.commons.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessAccountDetails;
import org.egov.commons.model.BusinessAccountSubLedgerDetails;
import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessDetails;
import org.egov.commons.model.BusinessDetailsCommonModel;
import org.egov.commons.model.BusinessDetailsCriteria;
import org.egov.commons.service.BusinessCategoryService;
import org.egov.commons.service.BusinessDetailsService;
import org.egov.commons.web.contract.factory.ResponseInfoFactory;
import org.egov.commons.web.controller.BusinessDetailsController;
import org.egov.commons.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(BusinessDetailsController.class)
public class BusinessDetailsControllerTest {
	@MockBean
	private BusinessCategoryService businessCategoryService;

	@MockBean
	private KafkaTemplate<?,?> kafkaTemplate;

	@MockBean
	private BusinessDetailsService businessDetailsService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ErrorHandler errHandler;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Test
	public void test_should_create_business_details() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(businessDetailsService.getBusinessDetailsByNameAndTenantId("Trade Licence", "default")).thenReturn(true);
		when(businessDetailsService.getBusinessDetailsByCodeAndTenantId("TL", "default")).thenReturn(true);
		when(businessCategoryService.getBusinessCategoryByIdAndTenantId(1L, "default"))
				.thenReturn(getBusinessCategoryModel());
		when(businessDetailsService.createBusinessDetails(getModelDetails(), getListOfModelAccountDetails(),
				getListOfModelAccountSubledger(), getAuthenticatedUser())).thenReturn(getBusinessDetailsCommonModel());

		mockMvc.perform(post("/businessDetails/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessDetailsRequestCreate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessDetailsResponseCreate.json")));

	}

	@Test
	public void test_should_update_business_details() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(businessDetailsService.getBusinessDetailsByNameAndTenantId("Trade Licence Mutation", "default"))
				.thenReturn(true);
		when(businessDetailsService.getBusinessDetailsByCodeAndTenantId("TLM", "default")).thenReturn(true);
		when(businessCategoryService.getBusinessCategoryByIdAndTenantId(1L, "default"))
				.thenReturn(getBusinessCategoryModel());
		when(businessDetailsService.updateBusinessDetails(getModelDetailsForUpdate(),
				getListOfModelAccountDetailsForUpdate(), getListOfModelAccountSubledgerForUpdate(),
				getAuthenticatedUser())).thenReturn(getBusinessDetailsCommonModelForUpdate());

		mockMvc.perform(post("/businessDetails/TL/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessDetailsRequestUpdate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessDetailsResponseUpdate.json")));

	}

	@Test
	public void test_should_search_business_details_based_on_criteria() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());

		when(businessDetailsService.getForCriteria(getBusinessDetailsCriteria()))
				.thenReturn(getBusinessDetailCommonModel());
		mockMvc.perform(
				post("/businessDetails/_search?active=true&businessCategoryCode=TL&tenantId=default&ids=1,2&sortBy=code&sortOrder=desc")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(getFileContents("businessDetailsRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessDetailsResponse.json")));
	}

	private BusinessDetailsCommonModel getBusinessDetailCommonModel() {
		BusinessCategory category = BusinessCategory.builder().id(1L).build();
		BusinessDetails details1 = BusinessDetails.builder().id(1L).code("TL").name("Trade Licence").isEnabled(true)
				.businessType("C").businessUrl("/receipts/receipt-create.action").voucherCreation(true)
				.isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").functionary("456")
				.department("56").tenantId("default").businessCategory(category).build();
		BusinessDetails details2 = BusinessDetails.builder().id(2L).code("PT").name("Property Tax").isEnabled(true)
				.businessType("C").businessUrl("/receipts/receipt-create.action").voucherCreation(true)
				.isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").functionary("456")
				.department("56").tenantId("default").businessCategory(category).build();
		BusinessDetails details = BusinessDetails.builder().id(1L).build();
		BusinessDetails details12 = BusinessDetails.builder().id(2L).build();

		List<BusinessDetails> listBusinessDetails = Arrays.asList(details1, details2);
		BusinessAccountDetails account1 = BusinessAccountDetails.builder().id(1L).chartOfAccount(56L).amount(1000.0)
				.tenantId("default").businessDetails(details).build();
		BusinessAccountDetails account2 = BusinessAccountDetails.builder().id(2L).chartOfAccount(57L).amount(2000.0)
				.tenantId("default").businessDetails(details).build();
		BusinessAccountDetails account3 = BusinessAccountDetails.builder().id(3L).chartOfAccount(56L).amount(1000.0)
				.tenantId("default").businessDetails(details12).build();
		BusinessAccountDetails account4 = BusinessAccountDetails.builder().id(4L).chartOfAccount(57L).amount(2000.0)
				.tenantId("default").businessDetails(details12).build();

		List<BusinessAccountDetails> listAccountDetails = Arrays.asList(account1, account2, account3, account4);

		BusinessAccountDetails businessAccountDetail = BusinessAccountDetails.builder().id(1L).build();
		BusinessAccountDetails businessAccountDetail2 = BusinessAccountDetails.builder().id(3L).build();
		BusinessAccountSubLedgerDetails subledger1 = BusinessAccountSubLedgerDetails.builder().id(1L)
				.accountDetailType(34L).accountDetailKey(23L).amount(10000.0).tenantId("default")
				.businessAccountDetail(businessAccountDetail).build();
		BusinessAccountSubLedgerDetails subledger2 = BusinessAccountSubLedgerDetails.builder().id(2L)
				.accountDetailType(35L).accountDetailKey(24L).amount(20000.0).tenantId("default")
				.businessAccountDetail(businessAccountDetail).build();
		BusinessAccountSubLedgerDetails subledger3 = BusinessAccountSubLedgerDetails.builder().id(3L)
				.accountDetailType(34L).accountDetailKey(23L).amount(10000.0).tenantId("default")
				.businessAccountDetail(businessAccountDetail2).build();
		BusinessAccountSubLedgerDetails subledger4 = BusinessAccountSubLedgerDetails.builder().id(4L)
				.accountDetailType(35L).accountDetailKey(24L).amount(20000.0).tenantId("default")
				.businessAccountDetail(businessAccountDetail2).build();
		List<BusinessAccountSubLedgerDetails> listSubledgerDetails = Arrays.asList(subledger1, subledger2, subledger3,
				subledger4);

		return BusinessDetailsCommonModel.builder().businessDetails(listBusinessDetails)
				.businessAccountDetails(listAccountDetails).businessAccountSubledgerDetails(listSubledgerDetails)
				.build();
	}

	private BusinessDetailsCriteria getBusinessDetailsCriteria() {

		return BusinessDetailsCriteria.builder().active(true).businessCategoryCode("TL").ids(Arrays.asList(1L, 2L))
				.tenantId("default").sortBy("code").sortOrder("desc").build();
	}

	private BusinessDetailsCommonModel getBusinessDetailsCommonModelForUpdate() {

		return BusinessDetailsCommonModel.builder()
				.businessDetails(Collections.singletonList(getModelDetailsForUpdate()))
				.businessAccountDetails(getListOfModelAccountDetailsForUpdate())
				.businessAccountSubledgerDetails(getListOfModelAccountSubledgerForUpdate()).build();
	}

	private List<BusinessAccountSubLedgerDetails> getListOfModelAccountSubledgerForUpdate() {
		BusinessAccountDetails accountDetail1 = BusinessAccountDetails.builder().id(1L).build();
		BusinessAccountDetails accountDetail2 = BusinessAccountDetails.builder().id(5L).build();
		BusinessAccountDetails accountDetail3 = BusinessAccountDetails.builder().id(6L).build();

		BusinessAccountSubLedgerDetails subledger1 = BusinessAccountSubLedgerDetails.builder().id(1L)
				.accountDetailType(34L).accountDetailKey(23L).amount(50000.00).tenantId("default")
				.businessAccountDetail(accountDetail1).build();
		BusinessAccountSubLedgerDetails subledger2 = BusinessAccountSubLedgerDetails.builder().id(3L)
				.accountDetailType(34L).accountDetailKey(23L).amount(30000.00).tenantId("default")
				.businessAccountDetail(accountDetail2).build();
		BusinessAccountSubLedgerDetails subledger3 = BusinessAccountSubLedgerDetails.builder().id(4L)
				.accountDetailType(34L).accountDetailKey(23L).amount(40000.00).tenantId("default")
				.businessAccountDetail(accountDetail3).build();
		BusinessAccountSubLedgerDetails subledger4 = BusinessAccountSubLedgerDetails.builder().id(5L)
				.accountDetailType(34L).accountDetailKey(23L).amount(50000.00).tenantId("default")
				.businessAccountDetail(accountDetail1).build();
		return Arrays.asList(subledger1, subledger2, subledger3, subledger4);
	}

	private List<BusinessAccountDetails> getListOfModelAccountDetailsForUpdate() {
		BusinessDetails details = BusinessDetails.builder().id(1L).build();
		BusinessAccountDetails account1 = BusinessAccountDetails.builder().id(1L).amount(5000.00).chartOfAccount(56L)
				.tenantId("default").businessDetails(details).build();
		BusinessAccountDetails account2 = BusinessAccountDetails.builder().id(5L).amount(3000.00).chartOfAccount(58L)
				.tenantId("default").businessDetails(details).build();
		BusinessAccountDetails account3 = BusinessAccountDetails.builder().id(6L).amount(4000.00).chartOfAccount(59L)
				.tenantId("default").businessDetails(details).build();

		return Arrays.asList(account1, account2, account3);
	}

	private BusinessDetails getModelDetailsForUpdate() {
		return BusinessDetails.builder().id(1L).code("TLM").name("Trade Licence Mutation").isEnabled(true)
				.businessCategory(getBusinessCategoryModel()).businessType("C")
				.businessUrl("/receipts/receipt-create.action").voucherCreation(true).isVoucherApproved(true)
				.ordernumber(2).fund("12").function("123").fundSource("234").functionary("456").department("56")
				.tenantId("default").build();
	}

	private BusinessDetailsCommonModel getBusinessDetailsCommonModel() {
		return BusinessDetailsCommonModel.builder().businessDetails(Collections.singletonList(getModelDetails()))
				.businessAccountDetails(getListOfModelAccountDetails())
				.businessAccountSubledgerDetails(getListOfModelAccountSubledger()).build();

	}

	private List<BusinessAccountSubLedgerDetails> getListOfModelAccountSubledger() {
		BusinessAccountSubLedgerDetails subledger1 = BusinessAccountSubLedgerDetails.builder().id(1L)
				.accountDetailType(34L).accountDetailKey(23L).amount(10000.00)
				.businessAccountDetail(getAccountAssociatedWithSubledger()).tenantId("default").build();
		BusinessAccountSubLedgerDetails subledger2 = BusinessAccountSubLedgerDetails.builder().id(2L)
				.accountDetailType(35L).accountDetailKey(24L).amount(20000.00)
				.businessAccountDetail(getAccountAssociatedWithSubledger()).tenantId("default").build();
		return Arrays.asList(subledger1, subledger2);
	}

	private List<BusinessAccountDetails> getListOfModelAccountDetails() {
		BusinessAccountDetails account1 = BusinessAccountDetails.builder().id(1L).amount(1000.00).chartOfAccount(56L)
				.tenantId("default").businessDetails(getModelDetails()).build();
		BusinessAccountDetails account2 = BusinessAccountDetails.builder().id(2L).amount(2000.00).chartOfAccount(57L)
				.tenantId("default").businessDetails(getModelDetails()).build();
		return Arrays.asList(account1, account2);
	}

	private BusinessAccountDetails getAccountAssociatedWithSubledger() {
		return BusinessAccountDetails.builder().id(1L).amount(1000.00).chartOfAccount(56L).tenantId("default")
				.businessDetails(getModelDetails()).build();

	}

	private BusinessDetails getModelDetails() {
		return BusinessDetails.builder().id(1L).code("TL").name("Trade Licence").isEnabled(true)
				.businessCategory(getBusinessCategoryModel()).businessType("C")
				.businessUrl("/receipts/receipt-create.action").voucherCreation(true).isVoucherApproved(true)
				.ordernumber(2).fund("12").function("123").fundSource("234").functionary("456").department("56")
				.tenantId("default").build();

	}

	private BusinessCategory getBusinessCategoryModel() {
		BusinessCategory category = BusinessCategory.builder().id(1L).code("TL").name("Trade Licence").isactive(true)
				.tenantId("default").build();
		return category;
	}

	private RequestInfo getRequestInfo() {
		User userInfo = User.builder().id(1L).build();
		return RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST").did("4354648646").key("xyz")
				.msgId("654654").authToken("aceb9362-4147-40ff-936a-34497f83d740").userInfo(userInfo)
				.build();
	}

	private AuthenticatedUser getAuthenticatedUser() {

		return AuthenticatedUser.builder().id(1L).anonymousUser(false).build();

	}

	private ResponseInfo getResponseInfo() {
		return ResponseInfo.builder().apiId("org.egov.collection").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("successful").build();
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
