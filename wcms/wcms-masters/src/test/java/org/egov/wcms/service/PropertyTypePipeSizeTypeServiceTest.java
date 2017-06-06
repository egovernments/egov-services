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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypePipeSizeType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.PropertyPipeSizeRepository;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyTypePipeSizeTypeServiceTest {

    @Mock
    private PropertyPipeSizeRepository propertyPipeSizeRepository;

    @Mock
    private WaterMasterProducer waterMasterProducer;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private PropertyTypePipeSizeTypeService propertyPipeSizeService;

    @Test
    public void test_Should_Search_PropertyPipeSize() {
        final List<PropertyTypePipeSizeType> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(getPropertyPipeSize());
        when(propertyPipeSizeRepository.findForCriteria(any(PropertyTypePipeSizeTypeGetRequest.class))).thenReturn(propertyPipeSizes);
        assertTrue(propertyPipeSizes.equals(propertyPipeSizeService.getPropertyPipeSizes(any(PropertyTypePipeSizeTypeGetRequest.class))));
    }

    @Test
    public void test_throwException_Push_To_Producer_PropertyPipeSize() {

        final PropertyTypePipeSizeType propertyPipeSize = getPropertyPipeSize();
        final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest = new PropertyTypePipeSizeTypeRequest();
        propertyPipeSizeRequest.setPropertyPipeSize(propertyPipeSize);
        assertTrue(propertyPipeSize.equals(propertyPipeSizeService.createPropertyPipeSize("", "", propertyPipeSizeRequest)));
    }

    @Test
    public void test_throwException_Create_PropertyPipeSize() {

        final PropertyTypePipeSizeType propertyPipeSize = getPropertyPipeSize();
        final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest = new PropertyTypePipeSizeTypeRequest();
        propertyPipeSizeRequest.setPropertyPipeSize(propertyPipeSize);
        when(propertyPipeSizeRepository.persistCreatePropertyPipeSize(any(PropertyTypePipeSizeTypeRequest.class)))
                .thenReturn(propertyPipeSizeRequest);
        assertTrue(propertyPipeSizeRequest.equals(propertyPipeSizeService.create(propertyPipeSizeRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_PropertyPipeSize() throws Exception {

        final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest = Mockito.mock(PropertyTypePipeSizeTypeRequest.class);
        when(propertyPipeSizeRepository.persistUpdatePropertyPipeSize(propertyPipeSizeRequest)).thenThrow(Exception.class);

        assertTrue(propertyPipeSizeRequest.equals(propertyPipeSizeService.update(propertyPipeSizeRequest)));
    }

    private PropertyTypePipeSizeType getPropertyPipeSize() {

        final PropertyTypePipeSizeType propertyPipeSize = new PropertyTypePipeSizeType();
        propertyPipeSize.setTenantId("default");
        propertyPipeSize.setPropertyTypeName("property type");
        propertyPipeSize.setPipeSizeType(2d);
        propertyPipeSize.setActive(true);
        return propertyPipeSize;
    }

}
