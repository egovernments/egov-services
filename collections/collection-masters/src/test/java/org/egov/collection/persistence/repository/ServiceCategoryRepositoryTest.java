package org.egov.collection.persistence.repository;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.specification.ServiceCategorySpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@RunWith(MockitoJUnitRunner.class)
public class ServiceCategoryRepositoryTest {
	
	@Mock
	private ServiceCategoryJpaRepository serviceCategoryJpaRepository;
	
	@InjectMocks
	private ServiceCategoryRepository serviceCategoryRepository;
	
	@Test
	public void  test_should_create_serviceCategory()
	{
		ServiceCategory actualServiceCategory=getServiceCategoryEntity();
	when(serviceCategoryJpaRepository.save(actualServiceCategory)).
	thenReturn(getServiceCategoryEntityAsResponse());
	org.egov.collection.domain.model.ServiceCategory expectedServiceCategory=
			serviceCategoryRepository.create(getServiceCategoryEntity());
	assertEquals(actualServiceCategory.getCode(),expectedServiceCategory.getCode());
	assertEquals(actualServiceCategory.getName(), expectedServiceCategory.getName());
	}
	
	
	@Test
	public void  test_should_update_serviceCategory()
	{
		ServiceCategory actualServiceCategory=getServiceCategoryEntity();
	when(serviceCategoryJpaRepository.save(actualServiceCategory)).
	thenReturn(getServiceCategoryEntityAsResponse());
	org.egov.collection.domain.model.ServiceCategory expectedServiceCategory=
			serviceCategoryRepository.update(getServiceCategoryEntity());
	assertEquals(actualServiceCategory.getCode(),expectedServiceCategory.getCode());
	assertEquals(actualServiceCategory.getName(), expectedServiceCategory.getName());
	}
	
	
	@Test
	public void test_should_find_service_category_byCode_and_tenantId()
	{
		ServiceCategory expectedResult=getServiceCategoryEntityAsResponse();
		when(serviceCategoryJpaRepository.findByCodeAndTenantId("serviceCode123","default")).thenReturn(expectedResult);
		ServiceCategory actualServiceCategory=serviceCategoryRepository.
				findByCodeAndTenantId("serviceCode123","default");
		assertEquals(expectedResult, actualServiceCategory);
		
	}
	
	@Test
	public void test_should_find_all_servicecatagories_by_specification_and_sort()
	{
		
		List<ServiceCategory>expectedServiceCatagories= getEntityServiceCategoriesForSearch();
		Sort sort=new Sort(Direction.DESC,"code");
		when(serviceCategoryJpaRepository.findAll(getServiceCategorySpecification(),sort)).
		thenReturn(getEntityServiceCategoriesForSearch());
		 List<ServiceCategory> actualServiceCatagories=serviceCategoryRepository.
				 findAll(getServiceCategorySpecification(), sort);
		 assertEquals(expectedServiceCatagories, actualServiceCatagories);
		
	}
	private List<ServiceCategory> getEntityServiceCategoriesForSearch() {
		ServiceCategory service1=ServiceCategory.builder()
				.id(2L).code("TL").name("Trade Licence").isactive(true).tenantId("default").build();
		ServiceCategory service2=ServiceCategory.builder()
				.id(3L).code("STAX").name("Sewerage Tax").isactive(true).tenantId("default").build();
		ServiceCategory service3=ServiceCategory.builder()
				.id(1L).code("ADTAX").name("Advertisement Tax").isactive(true).tenantId("default").build();
		return Arrays.asList(service1,service2,service3);
	}
	
	
	private ServiceCategorySpecification getServiceCategorySpecification() {
		List<Long> Ids=Arrays.asList(1L,2L,3L);
		ServiceCategorySearchCriteria serviceCriteria=ServiceCategorySearchCriteria.builder().isactive(true).tenantId("default").ids(Ids).build();
	  return new ServiceCategorySpecification(serviceCriteria);
	  }
	
	private ServiceCategory getServiceCategoryEntityAsResponse() {
	
		return ServiceCategory.builder().id(1L).code("serviceCode123").
				isactive(true).name("serviceName123").tenantId("default").build();
	}

    private ServiceCategory getServiceCategoryEntity() {
		return ServiceCategory.builder().id(1L).code("serviceCode123").isactive(true).
				name("serviceName123").tenantId("default").build();
			
		}
}
