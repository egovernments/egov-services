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
package org.egov.wcms.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.repository.builder.PropertyTypeCategoryTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyCategoryRowMapper;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.controller.PropertyCategoryController;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertyCategoryController.class)
@WebAppConfiguration
public class PropertyTypeCategoryTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PropertyTypeCategoryTypeQueryBuilder propertyCategoryueryBuilder;

    @Mock
    private PropertyCategoryRowMapper propertyCategoryRowMapper;

    @MockBean
    private PropertyCategoryService propertyCAtegoryService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private PropertyTypeCategoryTypeRepository propertyCategoryRepository;

    @Test
    public void test_Should_Find_PropertyCategory() throws Exception {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        propertyCategory.setActive(true);
        propertyCategory.setCategoryTypeName("category");
        propertyCategory.setPropertyTypeName("property");
        propertyCategory.setTenantId("1");

        propertyCategories.add(propertyCategory);

        final PropertyCategoryGetRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues)).thenReturn("query");
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), propertyCategoryRowMapper))
                .thenReturn(propertyCategories);

        assertNotNull(propertyCategoryRepository.findForCriteria(propertyCategoryRequest));
    }

    @Test
    public void test_Inavalid_Find_PropertyCategory() throws Exception {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        propertyCategory.setActive(true);
        propertyCategory.setCategoryTypeName("category");
        propertyCategory.setPropertyTypeName("property");
        propertyCategory.setTenantId("1");

        propertyCategories.add(propertyCategory);

        final PropertyCategoryGetRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues)).thenReturn(null);
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), propertyCategoryRowMapper))
                .thenReturn(propertyCategories);

        assertTrue(!propertyCategories.equals(propertyCategoryRepository.findForCriteria(propertyCategoryRequest)));
    }

    @Test(expected = Exception.class) // check
    public void test_Should_Create_PropertyCategory() throws Exception {
        final List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        propertyCategory.setActive(true);
        propertyCategory.setCategoryTypeName("category");
        propertyCategory.setPropertyTypeName("property");
        propertyCategory.setTenantId("1");

        propertyCategories.add(propertyCategory);

        final Object[] obj = new Object[] {};

        final PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
        when(jdbcTemplate.update("query", obj)).thenReturn(1);

        assertNotNull(propertyCategoryRepository.persistCreatePropertyCategory(propertyCategoryRequest));
    }

    @Test(expected = Exception.class)
    public void test_Should_Update_PropertyCategory() throws Exception {
        final List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
        propertyCategory.setActive(true);
        propertyCategory.setCategoryTypeName("category");
        propertyCategory.setPropertyTypeName("property");
        propertyCategory.setTenantId("1");

        propertyCategories.add(propertyCategory);

        final Object[] obj = new Object[] {};

        final PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
        when(jdbcTemplate.update("query", obj)).thenReturn(1);

        assertNotNull(propertyCategoryRepository.persistUpdatePropertyCategory(propertyCategoryRequest));
    }

}