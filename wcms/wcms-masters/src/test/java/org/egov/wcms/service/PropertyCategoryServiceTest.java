package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.PropertyTypeCategoryTypeRepository;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypesRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PropertyCategoryService.class)
@WebAppConfiguration
public class PropertyCategoryServiceTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private PropertyTypeCategoryTypeRepository propertyCategoryRepository;

    @Mock
    private WaterMasterProducer waterMasterProducer;

    @InjectMocks
    private PropertyCategoryService propertyCategoryService;

    @Test
    public void test_Should_Find_PropertyCategory() throws Exception {

        final PropertyTypeCategoryTypeReq propertyCategoryRequest = new PropertyTypeCategoryTypeReq();
        doReturn(propertyCategoryRequest).when(propertyCategoryRepository).persistCreatePropertyCategory(propertyCategoryRequest);

        assertTrue(propertyCategoryRequest.equals(propertyCategoryService.create(propertyCategoryRequest)));
    }

    @Test(expected = Exception.class)
    public void test_throwException_Find_PropertyCategory() throws Exception {

        final PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
        when(propertyCategoryRepository.persistCreatePropertyCategory(propertyCategoryRequest)).thenThrow(Exception.class);

        assertTrue(propertyCategoryRequest.equals(propertyCategoryService.create(propertyCategoryRequest)));
    }

    @Test
    public void test_Should_Get_PropertyCategories() throws Exception {

        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        final PropertyTypeCategoryTypesRes propertyCategoryResponse = new PropertyTypeCategoryTypesRes();
        propertyCategory.setPropertyTypeName("propertyType");
        propertyCategory.setCategoryTypeName("categoryType");
        propertyCategory.setActive(true);
        propertyCategory.setTenantId("1");

        final ArrayList<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        propertyCategories.add(propertyCategory);

        final PropertyCategoryGetRequest propertyCategoryGetRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        doReturn(propertyCategoryResponse).when(propertyCategoryRepository).findForCriteria(propertyCategoryGetRequest);

        assertNotNull(propertyCategoryService.getPropertyCategories(propertyCategoryGetRequest));
    }

    @Test(expected = Exception.class)
    public void test_throwException_Get_PropertyCategories() throws Exception {

        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        propertyCategory.setPropertyTypeName("propertyType");
        propertyCategory.setCategoryTypeName("categoryType");
        propertyCategory.setActive(true);
        propertyCategory.setTenantId("1");

        final ArrayList<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        propertyCategories.add(propertyCategory);

        final PropertyCategoryGetRequest propertyCategoryGetRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryRepository.findForCriteria(propertyCategoryGetRequest)).thenThrow(Exception.class);

        assertTrue(propertyCategories.equals(propertyCategoryService.getPropertyCategories(propertyCategoryGetRequest)));
    }

    /*
     * @Test public void test_Should_Create_PropertyCategory() throws Exception{ PropertyCategoryProducer propertyCategoryProducer
     * = new PropertyCategoryProducer(); PropertyCategoryProducer spy = Mockito.spy(propertyCategoryProducer); ObjectMapper mapper
     * = new ObjectMapper(); mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY); PropertyCategoryRequest
     * propertyCategoryRequest = Mockito.mock(PropertyCategoryRequest.class); doNothing().when(spy).sendMessage("topic", "key",
     * "propertyCategory"); assertTrue(propertyCategoryRequest.equals(propertyCategoryService.createPropertyCategory("topic",
     * "key", propertyCategoryRequest))); }
     * @Test(expected = Exception.class) public void test_throwException_Create_PropertyCategory() throws Exception{ ObjectMapper
     * mapper = new ObjectMapper(); PropertyCategoryRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryRequest.class);
     * when(mapper.writeValueAsString(propertyCategoryRequest)).thenReturn("propertyCategory");
     * doNothing().when(propertyCategoryProducer).sendMessage("topic", "key", "propertyCategory");
     * assertTrue(propertyCategoryRequest.equals(propertyCategoryService.createPropertyCategory("topic", "key",
     * propertyCategoryRequest))); }
     */

}
