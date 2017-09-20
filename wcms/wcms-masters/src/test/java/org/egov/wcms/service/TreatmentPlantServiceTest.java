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
import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.repository.TreatmentPlantRepository;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TreatmentPlantService.class)
public class TreatmentPlantServiceTest {

    @Mock
    private TreatmentPlantRepository treatmentPlantRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private TreatmentPlantService treatmentPlantService;

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_Should_Search_TreatmentPlant() {
        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        treatmentPlantList.add(getTreatmentPlant());
        final TreatmentPlantGetRequest treatmentPlantGetRequest = Mockito.mock(TreatmentPlantGetRequest.class);
        when(treatmentPlantRepository.findForCriteria(treatmentPlantGetRequest)).thenThrow(Exception.class);
        assertTrue(treatmentPlantList.equals(treatmentPlantService.getTreatmentPlant(treatmentPlantGetRequest)));
    }

    @Test
    public void test_throwException_Push_To_Producer_TreatmentPlant() {
        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        treatmentPlantList.add(getTreatmentPlant());
        final TreatmentPlantRequest treatmentPlantRequest = new TreatmentPlantRequest();
        treatmentPlantRequest.setTreatmentPlants(treatmentPlantList);
        assertTrue(
                treatmentPlantList.equals(treatmentPlantService.pushCreateToQueue("", "", treatmentPlantRequest)));
    }

    @Test
    public void test_throwException_Create_TreatmentPlant() {

        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        treatmentPlantList.add(getTreatmentPlant());
        final TreatmentPlantRequest treatmentPlantRequest = new TreatmentPlantRequest();
        treatmentPlantRequest.setTreatmentPlants(treatmentPlantList);
        when(treatmentPlantRepository.create(any(TreatmentPlantRequest.class)))
                .thenReturn(treatmentPlantRequest);
        assertTrue(treatmentPlantRequest.equals(treatmentPlantService.create(treatmentPlantRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_TreatmentPlant() throws Exception {

        final TreatmentPlantRequest treatmentPlantRequest = Mockito.mock(TreatmentPlantRequest.class);
        when(treatmentPlantRepository.update(treatmentPlantRequest)).thenThrow(Exception.class);

        assertTrue(treatmentPlantRequest.equals(treatmentPlantService.update(treatmentPlantRequest)));
    }

    private TreatmentPlant getTreatmentPlant() {
        final TreatmentPlant treatmentPlant = new TreatmentPlant();
        treatmentPlant.setTenantId("default");
        treatmentPlant.setName("test");
        treatmentPlant.setCode("12");
        treatmentPlant.setLocation("test");
        treatmentPlant.setCapacity(2d);
        treatmentPlant.setPlantType("test");
        treatmentPlant.setStorageReservoirId(2l);
        treatmentPlant.setPlantType("abcd");
        return treatmentPlant;
    }

}
