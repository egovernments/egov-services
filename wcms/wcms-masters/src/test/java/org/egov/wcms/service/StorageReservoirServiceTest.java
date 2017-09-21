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
import org.egov.wcms.model.StorageReservoir;
import org.egov.wcms.repository.StorageReservoirRepository;
import org.egov.wcms.web.contract.StorageReservoirGetRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(StorageReservoirService.class)
public class StorageReservoirServiceTest {

    @Mock
    private StorageReservoirRepository storageReservoirRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private StorageReservoirService storageReservoirService;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_Should_Search_StorageReservoir() {
        final List<StorageReservoir> storageReservoirList = new ArrayList<>();
        storageReservoirList.add(getStorageReservoir());
        final StorageReservoirGetRequest storageReservoirGetRequest = Mockito.mock(StorageReservoirGetRequest.class);
        when(storageReservoirRepository.findForCriteria(storageReservoirGetRequest)).thenThrow(Exception.class);
        assertTrue(
                storageReservoirList.equals(storageReservoirService.getStorageReservoir(storageReservoirGetRequest)));
    }

    @Test
    public void test_throwException_Push_To_Producer_StorageReservoir() {
        final List<StorageReservoir> storageReservoirList = new ArrayList<>();
        storageReservoirList.add(getStorageReservoir());
        final StorageReservoirRequest storageReservoirRequest = new StorageReservoirRequest();
        storageReservoirRequest.setStorageReservoirs(storageReservoirList);
        assertTrue(storageReservoirList
                .equals(storageReservoirService.pushCreateToQueue("", "", storageReservoirRequest)));
    }

    @Test
    public void test_throwException_Create_StorageReservoir() {

        final List<StorageReservoir> storageReservoirList = new ArrayList<>();
        storageReservoirList.add(getStorageReservoir());
        final StorageReservoirRequest storageReservoirRequest = new StorageReservoirRequest();
        storageReservoirRequest.setStorageReservoirs(storageReservoirList);
        when(storageReservoirRepository.create(any(StorageReservoirRequest.class)))
                .thenReturn(storageReservoirRequest);
        assertTrue(storageReservoirRequest.equals(storageReservoirService.create(storageReservoirRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_StorageReservoir() throws Exception {

        final StorageReservoirRequest storageReservoirRequest = Mockito.mock(StorageReservoirRequest.class);
        when(storageReservoirRepository.update(storageReservoirRequest))
                .thenThrow(Exception.class);

        assertTrue(storageReservoirRequest.equals(storageReservoirService.update(storageReservoirRequest)));
    }

    private StorageReservoir getStorageReservoir() {
        final StorageReservoir storageReservoir = new StorageReservoir();
        storageReservoir.setTenantId("default");
        storageReservoir.setName("test");
        storageReservoir.setCode("12");
        storageReservoir.setLocation("test1");
        storageReservoir.setCapacity(2d);
        storageReservoir.setNoOfSubLines(2l);
        storageReservoir.setNoOfMainDistributionLines(2l);
        storageReservoir.setNoOfConnection(2l);
        storageReservoir.setReservoirType("abcd");
        return storageReservoir;
    }

}
