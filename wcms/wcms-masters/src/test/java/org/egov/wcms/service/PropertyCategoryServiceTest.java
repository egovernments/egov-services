/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_PropertyCategory() throws Exception {

        final PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
        when(propertyCategoryRepository.persistUpdatePropertyCategory(propertyCategoryRequest)).thenThrow(Exception.class);

        assertTrue(propertyCategoryRequest.equals(propertyCategoryService.update(propertyCategoryRequest)));
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
