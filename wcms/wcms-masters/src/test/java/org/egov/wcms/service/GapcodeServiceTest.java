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

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.repository.GapcodeRepository;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GapcodeServiceTest {

    @Mock
    private GapcodeRepository gapcodeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private GapcodeService gapcodeService;

    @Test
    public void test_Search_For_Gapcode() {
        final List<Gapcode> gapcodeList = new ArrayList<>();
        final Gapcode gapcode = Mockito.mock(Gapcode.class);
        gapcodeList.add(gapcode);

        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class)))
                .thenReturn(gapcodeList);
        assertTrue(gapcodeList.equals(gapcodeService
                .getGapcodes(any(GapcodeGetRequest.class))));
    }

    @Test
    public void test_Search_For_Gapcode_Notnull() {
        final List<Gapcode> gapcodeList = new ArrayList<>();
        final Gapcode gapcode = Mockito.mock(Gapcode.class);
        gapcodeList.add(gapcode);

        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class)))
                .thenReturn(gapcodeList);
        assertNotNull(gapcodeService.getGapcodes(any(GapcodeGetRequest.class)));
    }

    @Test
    public void test_Search_For_Gapcode_Null() {
        final List<Gapcode> gapcodeList = new ArrayList<>();
        final Gapcode gapcode = Mockito.mock(Gapcode.class);
        gapcodeList.add(gapcode);

        when(gapcodeRepository.findForCriteria(any(GapcodeGetRequest.class)))
                .thenReturn(null);
        assertNull(gapcodeService.getGapcodes(any(GapcodeGetRequest.class)));
    }

    @Test
    public void test_throwException_Create_Gapcode() {

        final List<Gapcode> gapcodeList = new ArrayList<>();
        gapcodeList.add(getGapcode());
        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
        gapcodeRequest.setGapcode(gapcodeList);
        when(gapcodeRepository.create(any(GapcodeRequest.class))).thenReturn(
                gapcodeRequest);
        assertTrue(gapcodeRequest.equals(gapcodeService.create(gapcodeRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_Gapcode() throws Exception {

        final GapcodeRequest gapcodeRequest = Mockito
                .mock(GapcodeRequest.class);
        when(gapcodeRepository.update(gapcodeRequest)).thenThrow(
                Exception.class);

        assertTrue(gapcodeRequest.equals(gapcodeService.update(gapcodeRequest)));
    }

    private Gapcode getGapcode() {
        final Gapcode gapcode = new Gapcode();
        gapcode.setId(2L);
        gapcode.setCode("2");
        gapcode.setName("New Gapcode");
        gapcode.setActive(true);
        gapcode.setOutSideUlb(true);
        gapcode.setLogic("Average");
        gapcode.setNoOfMonths("Last 3 months Average");
        gapcode.setDescription("New Gaocode of Connection");
        return gapcode;
    }

}
