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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.model.CategoryType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.CategoryTypeRepository;
import org.egov.wcms.web.contract.CategoryTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CategoryTypeServiceTest {

    @Mock
    private CategoryTypeRepository categoryRepository;

    @Mock
    private WaterMasterProducer waterMasterProducer;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private CategoryTypeService connectionCategoryService;

    @Test
    public void test_Search_For_Categories() {
        final List<CategoryType> categoryList = new ArrayList<>();
        final CategoryType conCategory = Mockito.mock(CategoryType.class);
        categoryList.add(conCategory);

        when(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class))).thenReturn(categoryList);
        assertTrue(categoryList.equals(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class))));
    }

    @Test
    public void test_Search_For_Categories_Notnull() {
        final List<CategoryType> categoryList = new ArrayList<>();
        final CategoryType conCategory = Mockito.mock(CategoryType.class);
        categoryList.add(conCategory);

        when(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class))).thenReturn(categoryList);
        assertNotNull(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class)));
    }

    @Test
    public void test_Search_For_Categories_Null() {
        final List<CategoryType> categoryList = new ArrayList<>();
        final CategoryType conCategory = Mockito.mock(CategoryType.class);
        categoryList.add(conCategory);

        when(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class))).thenReturn(null);
        assertNull(categoryRepository.findForCriteria(any(CategoryTypeGetRequest.class)));
    }

    public List<CategoryType> getCategories(final CategoryTypeGetRequest categoryGetRequest) {
        return categoryRepository.findForCriteria(categoryGetRequest);
    }

}
