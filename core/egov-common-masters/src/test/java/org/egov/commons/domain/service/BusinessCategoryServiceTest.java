package org.egov.commons.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessCategoryCriteria;
import org.egov.commons.repository.BusinessCategoryRepository;
import org.egov.commons.service.BusinessCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BusinessCategoryService.class)
@WebAppConfiguration
public class BusinessCategoryServiceTest {
	
	@Mock
	private  BusinessCategoryRepository businessCategoryRepository;
	
	@InjectMocks
	private BusinessCategoryService businessCategoryService;
	
	@Test
	public void test_should_create_businessCategory(){
		
		when(businessCategoryRepository.create(getBusinessCategoryModel(), getAuthenticatedUser())).thenReturn(getBusinessCategoryModel());
		BusinessCategory category=businessCategoryService.create(getBusinessCategoryModel(),getAuthenticatedUser());
		assertThat(category.getCode()).isEqualTo(getBusinessCategoryModel().getCode());
		assertThat(category.getName()).isEqualTo(getBusinessCategoryModel().getName());
	}
	
	@Test
	public void test_should_update_businessCategory(){
		when(businessCategoryRepository.update("CLL",getBusinessCategoryModel(), getAuthenticatedUser())).thenReturn(getBusinessCategoryModel());
		BusinessCategory category=businessCategoryService.update("CLL", getBusinessCategoryModel(),getAuthenticatedUser());
		assertEquals(getBusinessCategoryModel(), category);
	}
	
	
	@Test
	public void test_should_return_all_serviceCategories_As_per_criteria(){
		when(businessCategoryRepository.getForCriteria(getBusinessCriteria())).thenReturn(getListOfModelBusinessCategories());
		List<BusinessCategory>modelCategories=businessCategoryService.getForCriteria(getBusinessCriteria());
		assertThat(modelCategories.get(0).getCode()).isEqualTo(getListOfModelBusinessCategories().get(0).getCode());
		assertThat(modelCategories.get(1).getCode()).isEqualTo(getListOfModelBusinessCategories().get(1).getCode());
		assertThat(modelCategories.get(2).getCode()).isEqualTo(getListOfModelBusinessCategories().get(2).getCode());

	}
	
	@Test
	public void test_should_verify_boolean_value_returned_isTrue_based_on_whether_nameAndtenantId_isPresent_inDB(){
		when(businessCategoryRepository.checkCategoryByNameAndTenantIdExists("Collection", "default")).thenReturn(true);
	Boolean value=businessCategoryService.getBusinessCategoryByNameAndTenantId("Collection", "default");
		assertEquals(true,value);
	}
	
	@Test
	public void test_should_verify_boolean_value_returned_isFalse_based_on_whether_nameAndtenantId_isPresent_inDB(){
		when(businessCategoryRepository.checkCategoryByNameAndTenantIdExists("Collection", "default")).thenReturn(false);
	Boolean value=businessCategoryService.getBusinessCategoryByNameAndTenantId("Collection", "default");
		assertEquals(false,value);
	}
	
	@Test
	public void test_should_verify_boolean_value_returned_isTrue_based_on_whether_codeAndtenantId_isPresent_inDB(){
		when(businessCategoryRepository.checkCategoryByCodeAndTenantIdExists("CL", "default")).thenReturn(true);
	Boolean value=	businessCategoryService.getBusinessCategoryByCodeAndTenantId("CL", "default");
	assertEquals(true, value);
	}
	
	@Test
	public void test_should_verify_boolean_value_returned_isFalse_based_on_whether_codeAndtenantId_isPresent_inDB(){
		when(businessCategoryRepository.checkCategoryByCodeAndTenantIdExists("Collection", "default")).thenReturn(false);
	Boolean value=businessCategoryService.getBusinessCategoryByCodeAndTenantId("Collection", "default");
		assertEquals(false,value);
	}
	
	
	  private List<BusinessCategory> getListOfModelBusinessCategories() {
			BusinessCategory category1=BusinessCategory.builder().id(3L).code("TL").name("Trade Licence")
					.isactive(true).tenantId("default").build();
			BusinessCategory category2=BusinessCategory.builder().id(2L).code("MR").name("Marriage Registration")
					.isactive(true).tenantId("default").build();
			BusinessCategory category3=BusinessCategory.builder().id(1L).code("CL").name("Collection")
					.isactive(true).tenantId("default").build();
	        return Arrays.asList(category1,category2,category3);
		}
	   
	  private BusinessCategoryCriteria getBusinessCriteria() {
			return BusinessCategoryCriteria.builder().ids(Arrays.asList(1L,2L,3L)).active(true).sortBy("code")
					.sortOrder("desc").tenantId("default").build();
		}
	
	private BusinessCategory getBusinessCategoryModel() {
		BusinessCategory category=BusinessCategory.builder().id(1L).code("CL").name("Collection")
				.isactive(true).tenantId("default").build();
		return category;
	}
	
	private AuthenticatedUser getAuthenticatedUser() {
		
		return AuthenticatedUser.builder().id(1L).name("ram").anonymousUser(false).emailId("ram@gmail.com").mobileNumber("73878921").build();
		
	}

}
