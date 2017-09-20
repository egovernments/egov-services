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
import org.egov.wcms.model.SupplyType;

import org.egov.wcms.repository.SupplyTypeRepository;
import org.egov.wcms.web.contract.SupplyTypeGetRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
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
public class SupplyTypeServiceTest {
    @Mock
    private SupplyTypeRepository supplyTypeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private SupplyTypeService supplyTypeService;
    @Mock
    private CodeGeneratorService codeGeneratorService;

    @Test
    public void test_Should_Search_WaterSource() {
        final List<SupplyType> supplyTypes = new ArrayList<>();
        supplyTypes.add(getSupplyType());
        when(supplyTypeRepository.findForCriteria(any(SupplyTypeGetRequest.class))).thenReturn(supplyTypes);
        assertTrue(supplyTypes.equals(supplyTypeService.getSupplyTypes(any(SupplyTypeGetRequest.class))));
    }

    @Test
    public void test_throwException_Push_To_Producer_SupplyType() {

        final List<SupplyType> supplyTypeList = new ArrayList<>();
        supplyTypeList.add(getSupplyType());
        final SupplyTypeRequest supplyTypeRequest = new SupplyTypeRequest();
        supplyTypeRequest.setSupplyTypes(supplyTypeList);
        assertTrue(supplyTypeList.equals(supplyTypeService.pushCreateToQueue("topic", "key",
                supplyTypeRequest)));
    }

    @Test
    public void test_throwException_Create_WaterSource() {

        final List<SupplyType> supplyTypeList = new ArrayList<>();
        supplyTypeList.add(getSupplyType());
        final SupplyTypeRequest supplyTypeRequest = new SupplyTypeRequest();
        supplyTypeRequest.setSupplyTypes(supplyTypeList);
        when(supplyTypeService.create(any(SupplyTypeRequest.class)))
                .thenReturn(supplyTypeRequest);
        assertTrue(supplyTypeRequest.equals(supplyTypeService.create(supplyTypeRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_WaterSource() throws Exception {

        final SupplyTypeRequest supplyTypeRequest = Mockito.mock(SupplyTypeRequest.class);
        when(supplyTypeService.update(supplyTypeRequest)).thenThrow(Exception.class);

        assertTrue(supplyTypeRequest.equals(supplyTypeService.update(supplyTypeRequest)));
    }

    private SupplyType getSupplyType() {
        final SupplyType SupplyType = new SupplyType();
        SupplyType.setTenantId("default");
        SupplyType.setCode("2");
        SupplyType.setName("water source");
        SupplyType.setActive(true);
        SupplyType.setDescription("water soucre type ");
        return SupplyType;
    }

}
