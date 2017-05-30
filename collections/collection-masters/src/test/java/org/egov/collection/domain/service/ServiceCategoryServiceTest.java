package org.egov.collection.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.repository.ServiceCategoryRepository;
import org.egov.collection.persistence.specification.ServiceCategorySpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@RunWith(MockitoJUnitRunner.class)
public class ServiceCategoryServiceTest {

    @Mock
    private ServiceCategoryRepository serviceCategoryRepository;

    @InjectMocks
    private ServiceCategoryService serviceCategoryService;
    
    @Test
    public void test_should_create_servicecategory_when_data_is_specified() {
    	org.egov.collection.domain.model.ServiceCategory expectedServiceCategory=getServiceCategoryModel();
       when(serviceCategoryRepository.create(getServiceCategoryEntity()))
                .thenReturn(expectedServiceCategory);
        final org.egov.collection.domain.model.ServiceCategory actualServiceCategory =
        		serviceCategoryService.create(getServiceCategoryEntity());
        assertEquals(expectedServiceCategory,actualServiceCategory);
    }
    
    @Test
    public void test_should_update_servicecategory_when_data_is_specified() {
    org.egov.collection.domain.model.ServiceCategory expectedServiceCategory=getServiceCategoryModel();
      when(serviceCategoryRepository.update(getServiceCategoryEntity()))
                .thenReturn(expectedServiceCategory);
        final org.egov.collection.domain.model.ServiceCategory actualServiceCategory = serviceCategoryService.update(getServiceCategoryEntity());
        assertEquals(expectedServiceCategory, actualServiceCategory);
    }
    

    @Test
    public void test_should_return_servicecategory_when_code_and_tenantId_is_provided() {
        final ServiceCategory expectedServiceCategory =getServiceCategoryEntity();
       when(serviceCategoryRepository.findByCodeAndTenantId("serviceCode", "default"))
                .thenReturn(expectedServiceCategory);
        final ServiceCategory actualServiceCategory = serviceCategoryService.
        		findByCodeAndTenantId("serviceCode", "default");
        assertEquals(expectedServiceCategory, actualServiceCategory);
    }
   

    @Test
    public void test_should_return_all_servicecategories_based_on_crieria_and_sort_which_is_decending() {
    	Sort sort=new Sort(Direction.DESC,"code");
        when(serviceCategoryRepository.findAll(getServiceCategorySpecification(),sort))
                .thenReturn(getEntityServiceCategoriesForSearch());
        final List<org.egov.collection.domain.model.ServiceCategory> actualResult =
        		serviceCategoryService.findAll(getServiceCategoryCriteria(),"code:desc");
        assertThat(actualResult.size()).isEqualTo(3);
    }
    

    @Test
    public void test_should_return_all_servicecategories_based_on_crieria_and_sort_which_is_ascending() {
    	Sort sort=new Sort(Direction.ASC,"code");
        when(serviceCategoryRepository.findAll(getServiceCategorySpecification(),sort))
                .thenReturn(getEntityServiceCategoriesForSearchWithSortAscending());
        final List<org.egov.collection.domain.model.ServiceCategory> actualResult =
        		serviceCategoryService.findAll(getServiceCategoryCriteria(),"code");
        assertThat(actualResult.size()).isEqualTo(3);
    }
    
    
 
	private List<ServiceCategory> getEntityServiceCategoriesForSearchWithSortAscending() {
		ServiceCategory service1=ServiceCategory.builder()
				.id(1L).code("ADTAX").name("Advertisement Tax").isactive(true).tenantId("default").build();
		ServiceCategory service2=ServiceCategory.builder()
				.id(3L).code("STAX").name("Sewerage Tax").isactive(true).tenantId("default").build();
		ServiceCategory service3=ServiceCategory.builder()
				.id(2L).code("TL").name("Trade Licence").isactive(true).tenantId("default").build();
		
		
		return Arrays.asList(service1,service2,service3);
	}

	private ServiceCategorySearchCriteria getServiceCategoryCriteria() {
		List<Long> Ids=Arrays.asList(1L,2L,3L);
		return ServiceCategorySearchCriteria.builder().isactive(true).tenantId("default").ids(Ids).build();
	}

	
	
	private ServiceCategorySpecification getServiceCategorySpecification() {
		List<Long> Ids=Arrays.asList(1L,2L,3L);
		ServiceCategorySearchCriteria serviceCriteria=ServiceCategorySearchCriteria.builder().isactive(true).
				tenantId("default").ids(Ids).build();
	  return new ServiceCategorySpecification(serviceCriteria);
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


	private org.egov.collection.domain.model.ServiceCategory getServiceCategoryModel() {
		
		return org.egov.collection.domain.model.ServiceCategory.builder()
			    .code("serviceCode").name("serviceName")
				.isactive(true).tenantId("default").build();
	}

	private ServiceCategory getServiceCategoryEntity() {
		ServiceCategory serviceCategory=ServiceCategory.builder().code("serviceCode").name("serviceName")
				.isactive(true).tenantId("default").build();
		serviceCategory.setCreatedBy(1L);
		serviceCategory.setCreatedDate(new Date());
		serviceCategory.setLastModifiedBy(1L);
		serviceCategory.setLastModifiedDate(new Date());
		return serviceCategory;
	}
}