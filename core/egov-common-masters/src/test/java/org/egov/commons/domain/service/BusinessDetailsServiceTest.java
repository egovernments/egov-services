package org.egov.commons.domain.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessAccountDetails;
import org.egov.commons.model.BusinessAccountSubLedgerDetails;
import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessDetails;
import org.egov.commons.model.BusinessDetailsCommonModel;
import org.egov.commons.model.BusinessDetailsCriteria;
import org.egov.commons.repository.BusinessDetailsRepository;
import org.egov.commons.service.BusinessDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BusinessDetailsService.class)
@WebAppConfiguration
public class BusinessDetailsServiceTest {
@Mock
BusinessDetailsRepository  businessDetailsRepository;

@InjectMocks
BusinessDetailsService businessDetailsService;

@Test
public void test_should_create_businessDetails(){
   when(businessDetailsRepository.createBusinessDetails(getModelDetails(), 
   getListOfModelAccountDetails(), getListOfModelAccountSubledger(),getAuthenticatedUser())).thenReturn(getBusinessDetailsCommonModel());
   BusinessDetailsCommonModel commanModel=businessDetailsService.createBusinessDetails(getModelDetails(),getListOfModelAccountDetails(),
		getListOfModelAccountSubledger(), getAuthenticatedUser());
	assertThat(getBusinessDetailsCommonModel().getBusinessDetails().get(0).getName())
	.isEqualTo(commanModel.getBusinessDetails().get(0).getName());
	assertThat(getBusinessDetailsCommonModel().getBusinessDetails().get(0).getCode())
	.isEqualTo(commanModel.getBusinessDetails().get(0).getCode());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountDetails().size()).
	isEqualTo(commanModel.getBusinessAccountDetails().size());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountSubledgerDetails().size()).
	isEqualTo(commanModel.getBusinessAccountSubledgerDetails().size());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountDetails().get(0).getAmount()).
	isEqualTo(commanModel.getBusinessAccountDetails().get(0).getAmount());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountDetails().get(1).getAmount()).
	isEqualTo(commanModel.getBusinessAccountDetails().get(1).getAmount());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountSubledgerDetails().get(0).getAmount()).
	isEqualTo(commanModel.getBusinessAccountSubledgerDetails().get(0).getAmount());
	assertThat(getBusinessDetailsCommonModel().getBusinessAccountSubledgerDetails().get(1).getAmount()).
	isEqualTo(commanModel.getBusinessAccountSubledgerDetails().get(1).getAmount());
	
}

@Test
public void test_should_update_businessDetails(){
	
	when(businessDetailsRepository.updateBusinessDetails(getModelDetailsForUpdate(), getListOfModelAccountDetailsForUpdate(),
				getListOfModelAccountSubledgerForUpdate(), getAuthenticatedUser())).thenReturn(getBusinessDetailsCommonModelForUpdate());
	 BusinessDetailsCommonModel commanModel=businessDetailsService.updateBusinessDetails(getModelDetailsForUpdate(), getListOfModelAccountDetailsForUpdate(), 
			getListOfModelAccountSubledgerForUpdate(), getAuthenticatedUser());
	 assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessDetails().get(0).getName())
		.isEqualTo(commanModel.getBusinessDetails().get(0).getName());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessDetails().get(0).getCode())
		.isEqualTo(commanModel.getBusinessDetails().get(0).getCode());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountDetails().size()).
		isEqualTo(commanModel.getBusinessAccountDetails().size());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountSubledgerDetails().size()).
		isEqualTo(commanModel.getBusinessAccountSubledgerDetails().size());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountDetails().get(0).getAmount()).
		isEqualTo(commanModel.getBusinessAccountDetails().get(0).getAmount());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountDetails().get(1).getAmount()).
		isEqualTo(commanModel.getBusinessAccountDetails().get(1).getAmount());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountSubledgerDetails().get(0).getAmount()).
		isEqualTo(commanModel.getBusinessAccountSubledgerDetails().get(0).getAmount());
		assertThat(getBusinessDetailsCommonModelForUpdate().getBusinessAccountSubledgerDetails().get(1).getAmount()).
		isEqualTo(commanModel.getBusinessAccountSubledgerDetails().get(1).getAmount());
}

@Test
public void test_should_search_businessDetails_based_on_criteria(){
	when(businessDetailsRepository.getForCriteria(getBusinessDetailsCriteria())).
	thenReturn(getBusinessDetailCommonModel());
	BusinessDetailsCommonModel commonModel=businessDetailsService.getForCriteria(getBusinessDetailsCriteria());
	assertThat(getBusinessDetailCommonModel().getBusinessDetails().get(0).getCode()).isEqualTo(commonModel.getBusinessDetails().get(0).getCode());
	assertThat(getBusinessDetailCommonModel().getBusinessDetails().get(1).getCode()).isEqualTo(commonModel.getBusinessDetails().get(1).getCode());
	assertThat(getBusinessDetailCommonModel().getBusinessAccountDetails().get(0).getChartOfAccount()).
	isEqualTo(commonModel.getBusinessAccountDetails().get(0).getChartOfAccount());
}

@Test
public void test_should_verify_boolean_value_returned_isTrue_based_on_whether_nameAndtenantId_isPresent_inDB(){
	when(businessDetailsRepository.checkDetailsByNameAndTenantIdExists("Trade Licence", "default")).thenReturn(true);
Boolean value=businessDetailsService.getBusinessDetailsByNameAndTenantId("Trade Licence", "default");
	assertEquals(true,value);
}

@Test
public void test_should_verify_boolean_value_returned_isFalse_based_on_whether_nameAndtenantId_isPresent_inDB(){
	when(businessDetailsRepository.checkDetailsByNameAndTenantIdExists("Trade Licence", "default")).thenReturn(false);
Boolean value=businessDetailsService.getBusinessDetailsByNameAndTenantId("Trade Licence", "default");
	assertEquals(false,value);
}

@Test
public void test_should_verify_boolean_value_returned_isTrue_based_on_whether_codeAndtenantId_isPresent_inDB(){
	when(businessDetailsRepository.checkDetailsByCodeAndTenantIdExists("TL", "default")).thenReturn(true);
Boolean value=	businessDetailsService.getBusinessDetailsByCodeAndTenantId("TL", "default");
assertEquals(true, value);
}

@Test
public void test_should_verify_boolean_value_returned_isFalse_based_on_whether_codeAndtenantId_isPresent_inDB(){
	when(businessDetailsRepository.checkDetailsByCodeAndTenantIdExists("TL", "default")).thenReturn(false);
Boolean value=businessDetailsService.getBusinessDetailsByCodeAndTenantId("TL", "default");
	assertEquals(false,value);
}




private BusinessDetailsCommonModel getBusinessDetailCommonModel() {
	  BusinessCategory category=BusinessCategory.builder().id(1L).build();
	  BusinessDetails details1=BusinessDetails.builder().id(1L).code("TL").name("Trade Licence").isEnabled(true)
			  .businessType("C").businessUrl("/receipts/receipt-create.action").
				voucherCreation(true).isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").
				functionary("456").department("56").tenantId("default").businessCategory(category)
				.build();
	  BusinessDetails details2=BusinessDetails.builder().id(2L).code("PT").name("Property Tax").isEnabled(true)
			  .businessType("C").businessUrl("/receipts/receipt-create.action").
				voucherCreation(true).isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").
				functionary("456").department("56").tenantId("default").businessCategory(category)
				.build();
	  BusinessDetails details=BusinessDetails.builder().id(1L).build();
	  BusinessDetails details12=BusinessDetails.builder().id(2L).build();

	 List<BusinessDetails>listBusinessDetails= Arrays.asList(details1,details2);
	 BusinessAccountDetails account1=BusinessAccountDetails.builder().id(1L).chartOfAccount(56L).amount(1000.0).tenantId("default").businessDetails(details).build();
	 BusinessAccountDetails account2=BusinessAccountDetails.builder().id(2L).chartOfAccount(57L).amount(2000.0).tenantId("default").businessDetails(details).build();
	 BusinessAccountDetails account3=BusinessAccountDetails.builder().id(3L).chartOfAccount(56L).amount(1000.0).tenantId("default").businessDetails(details12).build();
	 BusinessAccountDetails account4=BusinessAccountDetails.builder().id(4L).chartOfAccount(57L).amount(2000.0).tenantId("default").businessDetails(details12).build();

	 
	 List<BusinessAccountDetails> listAccountDetails=Arrays.asList(account1,account2,account3,account4);
	 
	 BusinessAccountDetails businessAccountDetail=BusinessAccountDetails.builder().id(1L).build();
	 BusinessAccountDetails businessAccountDetail2=BusinessAccountDetails.builder().id(3L).build();
	 BusinessAccountSubLedgerDetails subledger1=BusinessAccountSubLedgerDetails.builder().id(1L).accountDetailType(34L).accountDetailKey(23L).amount(10000.0).tenantId("default")
	 .businessAccountDetail(businessAccountDetail).build();
	 BusinessAccountSubLedgerDetails subledger2=BusinessAccountSubLedgerDetails.builder().id(2L).accountDetailType(35L).accountDetailKey(24L).amount(20000.0).tenantId("default")
			 .businessAccountDetail(businessAccountDetail).build();
	 BusinessAccountSubLedgerDetails subledger3=BusinessAccountSubLedgerDetails.builder().id(3L).accountDetailType(34L).accountDetailKey(23L).amount(10000.0).tenantId("default")
			 .businessAccountDetail(businessAccountDetail2).build();
	 BusinessAccountSubLedgerDetails subledger4=BusinessAccountSubLedgerDetails.builder().id(4L).accountDetailType(35L).accountDetailKey(24L).amount(20000.0).tenantId("default")
			 .businessAccountDetail(businessAccountDetail2).build();
	 List<BusinessAccountSubLedgerDetails> listSubledgerDetails=Arrays.asList(subledger1,subledger2,subledger3,subledger4);
	 
	 return BusinessDetailsCommonModel.builder().businessDetails(listBusinessDetails).
			  businessAccountDetails(listAccountDetails).businessAccountSubledgerDetails(listSubledgerDetails).build();
}
private BusinessDetailsCriteria getBusinessDetailsCriteria() {
	  
	  return BusinessDetailsCriteria.builder().active(true).businessCategoryCode("TL").ids(Arrays.asList(1L,2L)).
	  tenantId("default").sortBy("code").sortOrder("desc").build();
}

private BusinessDetailsCommonModel getBusinessDetailsCommonModelForUpdate() {

	return BusinessDetailsCommonModel.builder().businessDetails(Collections.singletonList(getModelDetailsForUpdate())).
			businessAccountDetails(getListOfModelAccountDetailsForUpdate())
			.businessAccountSubledgerDetails(getListOfModelAccountSubledgerForUpdate()).build();
}

private List<BusinessAccountSubLedgerDetails> getListOfModelAccountSubledgerForUpdate() {
	BusinessAccountDetails accountDetail1=BusinessAccountDetails.builder().id(1L).build();
	BusinessAccountDetails accountDetail2=BusinessAccountDetails.builder().id(5L).build();
	BusinessAccountDetails accountDetail3=BusinessAccountDetails.builder().id(6L).build();
	
	BusinessAccountSubLedgerDetails subledger1=	BusinessAccountSubLedgerDetails.builder().id(1L).accountDetailType(34L).accountDetailKey(23L).amount(50000.00).
	tenantId("default").businessAccountDetail(accountDetail1).build();
	BusinessAccountSubLedgerDetails subledger2=BusinessAccountSubLedgerDetails.builder().id(3L).accountDetailType(34L).accountDetailKey(23L).amount(30000.00).
	tenantId("default").businessAccountDetail(accountDetail2).build();
	BusinessAccountSubLedgerDetails subledger3=BusinessAccountSubLedgerDetails.builder().id(4L).accountDetailType(34L).accountDetailKey(23L).amount(40000.00).
	tenantId("default").businessAccountDetail(accountDetail3).build();
	BusinessAccountSubLedgerDetails subledger4=BusinessAccountSubLedgerDetails.builder().id(5L).accountDetailType(34L).accountDetailKey(23L).amount(50000.00).
	tenantId("default").businessAccountDetail(accountDetail1).build();
    return Arrays.asList(subledger1,subledger2,subledger3,subledger4);
}

private List<BusinessAccountDetails> getListOfModelAccountDetailsForUpdate() {
	BusinessDetails details=	BusinessDetails.builder().id(1L).build();
	BusinessAccountDetails account1=	BusinessAccountDetails.builder().id(1L).amount(5000.00).chartOfAccount(56L).
	tenantId("default").businessDetails(details).build();
	BusinessAccountDetails account2=	BusinessAccountDetails.builder().id(5L).amount(3000.00).chartOfAccount(58L).
	tenantId("default").businessDetails(details).build();
	BusinessAccountDetails account3=	BusinessAccountDetails.builder().id(6L).amount(4000.00).chartOfAccount(59L).
			tenantId("default").businessDetails(details).build();

	return Arrays.asList(account1,account2,account3);
}


private BusinessDetails getModelDetailsForUpdate() {
	return BusinessDetails.builder().id(1L).code("TLM").name("Trade Licence Mutation").isEnabled(true).
			businessCategory(getBusinessCategoryModel()).businessType("C").businessUrl("/receipts/receipt-create.action").
			voucherCreation(true).isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").
			functionary("456").department("56").tenantId("default").build();
}

private BusinessDetails getModelDetails() {
	return BusinessDetails.builder().id(1L).code("TL").name("Trade Licence").isEnabled(true).
	businessCategory(getBusinessCategoryModel()).businessType("C").businessUrl("/receipts/receipt-create.action").
	voucherCreation(true).isVoucherApproved(true).ordernumber(2).fund("12").function("123").fundSource("234").
	functionary("456").department("56").tenantId("default").build();

	 
}
private BusinessDetailsCommonModel getBusinessDetailsCommonModel() {
	 return BusinessDetailsCommonModel.builder().businessDetails(Arrays.asList(getModelDetails())).
	  businessAccountDetails(getListOfModelAccountDetails()).businessAccountSubledgerDetails(getListOfModelAccountSubledger()).build();

}

private BusinessAccountDetails getAccountAssociatedWithSubledger() {
	return  BusinessAccountDetails.builder().id(1L).amount(1000.00).chartOfAccount(56L).tenantId("default").
	businessDetails(getModelDetails()).build();

}

private BusinessCategory getBusinessCategoryModel() {
	  BusinessCategory category=BusinessCategory.builder().id(1L).code("TL").name("Trade Licence")
				.isactive(true).tenantId("default").build();
		return category;
}

private AuthenticatedUser getAuthenticatedUser() {
	
	return AuthenticatedUser.builder().id(1L).anonymousUser(false).build();
	
}

private List<BusinessAccountDetails> getListOfModelAccountDetails() {
	BusinessAccountDetails account1 =	BusinessAccountDetails.builder().id(1L).amount(1000.00).chartOfAccount(56L).tenantId("default").
			businessDetails(getModelDetails()).build();
	BusinessAccountDetails account2 =   BusinessAccountDetails.builder().id(2L).amount(2000.00).chartOfAccount(57L).tenantId("default").
			businessDetails(getModelDetails()).build();	
	return Arrays.asList(account1,account2);
}

private List<BusinessAccountSubLedgerDetails> getListOfModelAccountSubledger() {
	  BusinessAccountSubLedgerDetails subledger1=  BusinessAccountSubLedgerDetails.builder().id(1L).accountDetailType(34L).
			  accountDetailKey(23L).amount(10000.00).businessAccountDetail(getAccountAssociatedWithSubledger()).tenantId("default").build();
	  BusinessAccountSubLedgerDetails subledger2= BusinessAccountSubLedgerDetails.builder().id(2L).accountDetailType(35L).
			  accountDetailKey(24L).amount(20000.00).businessAccountDetail(getAccountAssociatedWithSubledger()).tenantId("default").build();
    return Arrays.asList(subledger1,subledger2);
}
}
