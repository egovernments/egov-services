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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.repository.SourceTypeRepository;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(SourceTypeService.class)
@WebAppConfiguration
public class SourceTypeServiceTest {
    @Mock
    private SourceTypeRepository waterSourceTypeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private SourceTypeService waterSourceTypeService;
    @Mock
    private CodeGeneratorService codeGeneratorService;

    @Test
    public void test_Should_Search_WaterSource() {
        final List<SourceType> waterSourceTypes = new ArrayList<>();
        waterSourceTypes.add(getWaterSourceType());
        when(waterSourceTypeRepository.findForCriteria(any(SourceTypeGetRequest.class))).thenReturn(waterSourceTypes);
        assertTrue(waterSourceTypes.equals(waterSourceTypeService.getWaterSourceTypes(any(SourceTypeGetRequest.class))));
    }

    @Test
    public void test_throwException_Push_To_Producer_WaterSource() {

        final List<SourceType> waterSourceList = new ArrayList<>();
        waterSourceList.add(getWaterSourceType());
        final SourceTypeRequest waterSourceRequest = new SourceTypeRequest();
        waterSourceRequest.setSourceTypes(waterSourceList);
        assertTrue(waterSourceList.equals(waterSourceTypeService.pushCreateToQueue("topic", "key", waterSourceRequest)));
    }

    @Test
    public void test_throwException_Create_WaterSource() {

        final List<SourceType> waterSourceTypeList = new ArrayList<>();
        waterSourceTypeList.add(getWaterSourceType());
        final SourceTypeRequest waterSourceRequest = new SourceTypeRequest();
        waterSourceRequest.setSourceTypes(waterSourceTypeList);
        when(waterSourceTypeRepository.create(any(SourceTypeRequest.class)))
                .thenReturn(waterSourceRequest);
        assertTrue(waterSourceRequest.equals(waterSourceTypeService.create(waterSourceRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_WaterSource() throws Exception {

        final SourceTypeRequest waterSourceRequest = Mockito.mock(SourceTypeRequest.class);
        when(waterSourceTypeRepository.update(waterSourceRequest)).thenThrow(Exception.class);

        assertTrue(waterSourceRequest.equals(waterSourceTypeService.update(waterSourceRequest)));
    }

    private SourceType getWaterSourceType() {
        final SourceType waterSource = new SourceType();
        waterSource.setTenantId("default");
        waterSource.setCode("2");
        waterSource.setName("water source");
        waterSource.setActive(true);
        waterSource.setDescription("water soucre type ");
        return waterSource;
    }

}
