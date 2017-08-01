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
import org.egov.pgr.domain.model.ReceivingCenterType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ReceivingCenterTypeRepository;
import org.egov.pgr.web.contract.ReceivingCenterTypeGetReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingCenterTypeServiceTest {

    @Mock
    private ReceivingCenterTypeRepository receivingCenterRepository;

    @Mock
    private PGRProducer pgrMasterProducer;

    @InjectMocks
    private ReceivingCenterTypeService receivingCenterTypeService;

    @Test
    public void test_Search_For_ReceivingCenterTypeServices() {
        final List<ReceivingCenterType> ReceivingCenterTypeList = new ArrayList<>();
        final ReceivingCenterType receivingCenterType = Mockito.mock(ReceivingCenterType.class);
        ReceivingCenterTypeList.add(receivingCenterType);

        when(receivingCenterRepository.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class))).thenReturn(ReceivingCenterTypeList);
        assertTrue(ReceivingCenterTypeList.equals(receivingCenterTypeService.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class))));
    }

    @Test
    public void test_Should_ReceivingCenterType_Create() {
        final ReceivingCenterType receivingCenterType = getReceivingCenterType();
        final List<ReceivingCenterType> receivingCenterTypes = new ArrayList<>();
        receivingCenterTypes.add(receivingCenterType);
        final ReceivingCenterTypeReq receivingCenterTypeReq = new ReceivingCenterTypeReq();
        receivingCenterTypeReq.setCenterType(receivingCenterType);
        final ReceivingCenterTypeRes propUsageTypeResponse = new ReceivingCenterTypeRes();
        propUsageTypeResponse.setResponseInfo(null);
        propUsageTypeResponse.setCenterTypes(receivingCenterTypes);

        when(receivingCenterTypeService.create(any(ReceivingCenterTypeReq.class))).thenReturn(receivingCenterTypeReq);
        assertTrue(receivingCenterTypeReq.equals(receivingCenterTypeService.create(receivingCenterTypeReq)));

    }

    private ReceivingCenterType getReceivingCenterType() {
        final ReceivingCenterType receivingCenterType = new ReceivingCenterType();
        receivingCenterType.setCode("23");
        receivingCenterType.setName("New receivingCenter Name");
        receivingCenterType.setDescription("receivingCenter Name Description");
        receivingCenterType.setActive(true);
        receivingCenterType.setIscrnrequired(true);
        receivingCenterType.setOrderno(10);
        return receivingCenterType;
    }

    @Test
    public void test_Search_For_ReceivingCenterType_Notnull() {
        final List<ReceivingCenterType> receivingCenterTypeList = new ArrayList<>();
        final ReceivingCenterType receivingCenterType = Mockito.mock(ReceivingCenterType.class);
        receivingCenterTypeList.add(receivingCenterType);

        when(receivingCenterRepository.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class))).thenReturn(receivingCenterTypeList);
        assertNotNull(receivingCenterTypeService.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class)));
    }

    @Test
    public void test_Search_For_receivingCenter_Types_Null() {
        final List<ReceivingCenterType> receivingCenterTypeList = new ArrayList<>();
        final ReceivingCenterType receivingCenterType = Mockito.mock(ReceivingCenterType.class);
        receivingCenterTypeList.add(receivingCenterType);

        when(receivingCenterRepository.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class))).thenReturn(null);
        assertNull(receivingCenterTypeService.getAllReceivingCenterTypes(any(ReceivingCenterTypeGetReq.class)));
    }

    public List<ReceivingCenterType> getReceivingCenterTypes(final ReceivingCenterTypeGetReq receivingCenterTypeGetRequest) {
        return receivingCenterRepository.getAllReceivingCenterTypes(receivingCenterTypeGetRequest);
    }

    @Test
    public void test_Should_Update_ReceivingCenterType_NotNull() throws Exception {
        final ReceivingCenterTypeReq receivingCenterTypeRequest = new ReceivingCenterTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final ReceivingCenterType receivingCenterType = new ReceivingCenterType();
        final AuditDetails auditDetails = new AuditDetails();

        receivingCenterType.setAuditDetails(auditDetails);
        receivingCenterType.setCode("10");
        receivingCenterType.setActive(true);
        receivingCenterType.getAuditDetails().setCreatedBy(1L);
        receivingCenterType.setName("test name");
        receivingCenterType.setDescription("test description");

        receivingCenterTypeRequest.setRequestInfo(requestInfo);
        receivingCenterTypeRequest.setCenterType(receivingCenterType);

        final ReceivingCenterType receivingCenterTypeResult = receivingCenterTypeService.sendMessage("topic", "key", receivingCenterTypeRequest);

        assertNotNull(receivingCenterTypeResult);
    }

    @Test
    public void test_Should_Update_ReceivingCenterType() {
        final ReceivingCenterType receivingCenterType = getReceivingCenterType();
        final List<ReceivingCenterType> receivingCenterTypes = new ArrayList<>();
        receivingCenterTypes.add(receivingCenterType);
        final ReceivingCenterTypeReq receivingCenterTypeRequest = new ReceivingCenterTypeReq();
        receivingCenterTypeRequest.setCenterType(receivingCenterType);
        final ReceivingCenterTypeRes receivingCenterTypeResponse = new ReceivingCenterTypeRes();
        receivingCenterTypeResponse.setResponseInfo(null);
        receivingCenterTypeResponse.setCenterTypes(receivingCenterTypes);

        when(receivingCenterTypeService.update(any(ReceivingCenterTypeReq.class))).thenReturn(receivingCenterTypeRequest);
        assertTrue(receivingCenterTypeRequest.equals(receivingCenterTypeService.update(receivingCenterTypeRequest)));

    }

}