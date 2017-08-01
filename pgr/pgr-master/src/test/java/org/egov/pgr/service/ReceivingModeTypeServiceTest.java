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
import org.egov.pgr.domain.model.ReceivingModeType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ReceivingModeTypeRepository;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeTypeReq;
import org.egov.pgr.web.contract.ReceivingModeTypeRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingModeTypeServiceTest {

    @Mock
    private ReceivingModeTypeRepository receivingModeRepository;

    @Mock
    private PGRProducer waterMasterProducer;

    @InjectMocks
    private ReceivingModeTypeService receivingModeTypeService;

    @Test
    public void test_Search_For_ReceivingModeTypeServices() {
        final List<ReceivingModeType> ReceivingModeTypeList = new ArrayList<>();
        final ReceivingModeType receivingModeType = Mockito.mock(ReceivingModeType.class);
        ReceivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(ReceivingModeTypeList);
        assertTrue(ReceivingModeTypeList.equals(receivingModeTypeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))));
    }

    @Test
    public void test_Should_ReceivingModeType_Create() {
        final ReceivingModeType receivingModeType = getReceivingModeType();
        final List<ReceivingModeType> receivingModeTypes = new ArrayList<>();
        receivingModeTypes.add(receivingModeType);
        final ReceivingModeTypeReq receivingModeTypeReq = new ReceivingModeTypeReq();
        receivingModeTypeReq.setModeType(receivingModeType);
        final ReceivingModeTypeRes propUsageTypeResponse = new ReceivingModeTypeRes();
        propUsageTypeResponse.setResponseInfo(null);
        propUsageTypeResponse.setModeTypes(receivingModeTypes);

        when(receivingModeTypeService.create(any(ReceivingModeTypeReq.class))).thenReturn(receivingModeTypeReq);
        assertTrue(receivingModeTypeReq.equals(receivingModeTypeService.create(receivingModeTypeReq)));

    }

    private ReceivingModeType getReceivingModeType() {
        final ReceivingModeType receivingModeType = new ReceivingModeType();
        receivingModeType.setCode("23");
        receivingModeType.setName("New receivingMode Name");
        receivingModeType.setDescription("receivingMode Name Description");
        receivingModeType.setActive(true);
        return receivingModeType;
    }

    @Test
    public void test_Search_For_ReceivingModeType_Notnull() {
        final List<ReceivingModeType> receivingModeTypeList = new ArrayList<>();
        final ReceivingModeType receivingModeType = Mockito.mock(ReceivingModeType.class);
        receivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(receivingModeTypeList);
        assertNotNull(receivingModeTypeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class)));
    }

    @Test
    public void test_Search_For_receivingMode_Types_Null() {
        final List<ReceivingModeType> receivingModeTypeList = new ArrayList<>();
        final ReceivingModeType receivingModeType = Mockito.mock(ReceivingModeType.class);
        receivingModeTypeList.add(receivingModeType);

        when(receivingModeRepository.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class))).thenReturn(null);
        assertNull(receivingModeTypeService.getAllReceivingModeTypes(any(ReceivingModeTypeGetReq.class)));
    }

    public List<ReceivingModeType> getReceivingModeTypes(final ReceivingModeTypeGetReq receivingModeTypeGetRequest) {
        return receivingModeRepository.getAllReceivingModeTypes(receivingModeTypeGetRequest);
    }

    @Test
    public void test_Should_Update_ReceivingModeType_NotNull() throws Exception {
        final ReceivingModeTypeReq receivingModeTypeRequest = new ReceivingModeTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final ReceivingModeType receivingModeType = new ReceivingModeType();
        final AuditDetails auditDetails = new AuditDetails();

        receivingModeType.setAuditDetails(auditDetails);
        receivingModeType.setCode("10");
        receivingModeType.setActive(true);
        receivingModeType.getAuditDetails().setCreatedBy(1L);
        receivingModeType.setName("test name");
        receivingModeType.setDescription("test description");

        receivingModeTypeRequest.setRequestInfo(requestInfo);
        receivingModeTypeRequest.setModeType(receivingModeType);

        final ReceivingModeType receivingModeTypeResult = receivingModeTypeService.sendMessage("topic", "key", receivingModeTypeRequest);

        assertNotNull(receivingModeTypeResult);
    }

    @Test
    public void test_Should_Update_ReceivingModeType() {
        final ReceivingModeType receivingModeType = getReceivingModeType();
        final List<ReceivingModeType> receivingModeTypes = new ArrayList<>();
        receivingModeTypes.add(receivingModeType);
        final ReceivingModeTypeReq receivingModeTypeRequest = new ReceivingModeTypeReq();
        receivingModeTypeRequest.setModeType(receivingModeType);
        final ReceivingModeTypeRes receivingModeTypeResponse = new ReceivingModeTypeRes();
        receivingModeTypeResponse.setResponseInfo(null);
        receivingModeTypeResponse.setModeTypes(receivingModeTypes);

        when(receivingModeTypeService.update(any(ReceivingModeTypeReq.class))).thenReturn(receivingModeTypeRequest);
        assertTrue(receivingModeTypeRequest.equals(receivingModeTypeService.update(receivingModeTypeRequest)));

    }

}