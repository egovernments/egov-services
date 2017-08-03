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
 *//*


package org.egov.pgr.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.service.ReceivingModeService;
import org.egov.pgr.web.contract.ReceivingMode;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ReceivingModeRepository;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeRequest;
import org.egov.pgr.web.contract.ReceivingModeTypeRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingModeServiceTest {

    @Mock
    private ReceivingModeRepository receivingModeRepository;

    @Mock
    private PGRProducer waterMasterProducer;

    @InjectMocks
    private ReceivingModeService receivingModeService;

    @Test
    public void test_Search_For_ReceivingModeTypeServices() {
        final List<ReceivingMode> ReceivingModeTypeList = new ArrayList<>();
        final ReceivingMode receivingModeType = Mockito.mock(ReceivingMode.class);
        ReceivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(ReceivingModeTypeList);
        assertTrue(ReceivingModeTypeList.equals(receivingModeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))));
    }

    @Test
    public void test_Should_ReceivingModeType_Create() {
        final ReceivingMode receivingModeType = getReceivingModeType();
        final List<ReceivingMode> receivingModeTypes = new ArrayList<>();
        receivingModeTypes.add(receivingModeType);
        final ReceivingModeRequest receivingModeRequest = new ReceivingModeRequest();
        receivingModeRequest.setReceivingMode(receivingModeType);
        final ReceivingModeTypeRes propUsageTypeResponse = new ReceivingModeTypeRes();
        propUsageTypeResponse.setResponseInfo(null);
        propUsageTypeResponse.setModeTypes(receivingModeTypes);

        when(receivingModeService.create(any(ReceivingModeRequest.class))).thenReturn(receivingModeRequest);
        assertTrue(receivingModeRequest.equals(receivingModeService.create(receivingModeRequest)));

    }

    private ReceivingMode getReceivingModeType() {
        final ReceivingMode receivingModeType = new ReceivingMode();
        receivingModeType.setCode("23");
        receivingModeType.setName("New receivingMode Name");
        receivingModeType.setDescription("receivingMode Name Description");
        receivingModeType.setActive(true);
        return receivingModeType;
    }

    @Test
    public void test_Search_For_ReceivingModeType_Notnull() {
        final List<ReceivingMode> receivingModeTypeList = new ArrayList<>();
        final ReceivingMode receivingModeType = Mockito.mock(ReceivingMode.class);
        receivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(receivingModeTypeList);
        assertNotNull(receivingModeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class)));
    }

    @Test
    public void test_Search_For_receivingMode_Types_Null() {
        final List<ReceivingMode> receivingModeTypeList = new ArrayList<>();
        final ReceivingMode receivingModeType = Mockito.mock(ReceivingMode.class);
        receivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(null);
        assertNull(receivingModeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class)));
    }

    public List<ReceivingMode> getReceivingModeTypes(final ReceivingModeTypeGetReq receivingModeTypeGetRequest) {
        return receivingModeRepository.getAllReceivingModeTypes(receivingModeTypeGetRequest);
    }

    @Test
    public void test_Should_Update_ReceivingModeType_NotNull() throws Exception {
        final ReceivingModeRequest receivingModeTypeRequest = new ReceivingModeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final ReceivingMode receivingModeType = new ReceivingMode();
        final AuditDetails auditDetails = new AuditDetails();

        receivingModeType.setAuditDetails(auditDetails);
        receivingModeType.setCode("10");
        receivingModeType.setActive(true);
        receivingModeType.getAuditDetails().setCreatedBy(1L);
        receivingModeType.setName("test name");
        receivingModeType.setDescription("test description");

        receivingModeTypeRequest.setRequestInfo(requestInfo);
        receivingModeTypeRequest.setReceivingMode(receivingModeType);

        final ReceivingMode receivingModeTypeResult = receivingModeService.sendMessage("topic", "key", receivingModeTypeRequest);

        assertNotNull(receivingModeTypeResult);
    }

    @Test
    public void test_Should_Update_ReceivingModeType() {
        final ReceivingMode receivingModeType = getReceivingModeType();
        final List<ReceivingMode> receivingModeTypes = new ArrayList<>();
        receivingModeTypes.add(receivingModeType);
        final ReceivingModeRequest receivingModeTypeRequest = new ReceivingModeRequest();
        receivingModeTypeRequest.setReceivingMode(receivingModeType);
        final ReceivingModeTypeRes receivingModeTypeResponse = new ReceivingModeTypeRes();
        receivingModeTypeResponse.setResponseInfo(null);
        receivingModeTypeResponse.setModeTypes(receivingModeTypes);

        when(receivingModeService.update(any(ReceivingModeRequest.class))).thenReturn(receivingModeTypeRequest);
        assertTrue(receivingModeTypeRequest.equals(receivingModeService.update(receivingModeTypeRequest)));

    }

}*/
