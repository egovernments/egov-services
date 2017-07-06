/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.repository.PropertyPipeSizeRepository;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypesRes;
import org.egov.wcms.web.contract.PropertyTypePipeSizeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PropertyTypePipeSizeService.class)
public class PropertyTypePipeSizeServiceTest {

    @Mock
    private PropertyPipeSizeRepository propertyPipeSizeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private PropertyTypePipeSizeService propertyPipeSizeService;

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_Should_Search_PropertyPipeSize() {
        final List<PropertyTypePipeSize> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(getPropertyPipeSize());
        final PropertyTypePipeSizeGetRequest propertyTypePipeSizeGetRequest = Mockito.mock(PropertyTypePipeSizeGetRequest.class);
        when(propertyPipeSizeRepository.findForCriteria(propertyTypePipeSizeGetRequest)).thenThrow(Exception.class);
        assertTrue(propertyPipeSizes
                .equals(propertyPipeSizeService.getPropertyPipeSizes(propertyTypePipeSizeGetRequest)));
    }

    @Test
    public void test_throwException_Push_To_Producer_PropertyPipeSize() {

        final PropertyTypePipeSize propertyPipeSize = getPropertyPipeSize();
        final PropertyTypePipeSizeRequest propertyPipeSizeRequest = new PropertyTypePipeSizeRequest();
        propertyPipeSizeRequest.setPropertyPipeSize(propertyPipeSize);
        assertTrue(propertyPipeSize.equals(propertyPipeSizeService.createPropertyPipeSize("", "", propertyPipeSizeRequest)));
    }

    @Test
    public void test_throwException_Create_PropertyPipeSize() {

        final PropertyTypePipeSize propertyPipeSize = getPropertyPipeSize();
        final PropertyTypePipeSizeRequest propertyPipeSizeRequest = new PropertyTypePipeSizeRequest();
        propertyPipeSizeRequest.setPropertyPipeSize(propertyPipeSize);
        when(propertyPipeSizeRepository.persistCreatePropertyPipeSize(any(PropertyTypePipeSizeRequest.class)))
                .thenReturn(propertyPipeSizeRequest);
        assertTrue(propertyPipeSizeRequest.equals(propertyPipeSizeService.create(propertyPipeSizeRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_PropertyPipeSize() throws Exception {

        final PropertyTypePipeSizeRequest propertyPipeSizeRequest = Mockito.mock(PropertyTypePipeSizeRequest.class);
        when(propertyPipeSizeRepository.persistUpdatePropertyPipeSize(propertyPipeSizeRequest)).thenThrow(Exception.class);

        assertTrue(propertyPipeSizeRequest.equals(propertyPipeSizeService.update(propertyPipeSizeRequest)));
    }

    private PropertyTypePipeSize getPropertyPipeSize() {

        final PropertyTypePipeSize propertyPipeSize = new PropertyTypePipeSize();
        propertyPipeSize.setTenantId("default");
        propertyPipeSize.setPropertyTypeName("property type");
        propertyPipeSize.setPipeSize(2d);
        propertyPipeSize.setActive(true);
        return propertyPipeSize;
    }

}
