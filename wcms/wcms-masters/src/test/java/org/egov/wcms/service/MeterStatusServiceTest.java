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
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.repository.MeterStatusRepository;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MeterStatusServiceTest {
    @Mock
    private MeterStatusRepository meterStatusRepository;

    @InjectMocks
    private MeterStatusService meterStatusService;

    @Test
    public void test_should_push_createMeterStatusRequest_to_queue() {
        when(meterStatusRepository.pushCreateToQueue(getMeterStatusRequest())).thenReturn(getListOfMeterStatuses());
        assertTrue(getListOfMeterStatuses().equals(meterStatusService.pushCreateToQueue(getMeterStatusRequest())));
    }

    @Test
    public void test_should_push_updateMeterStatusRequest_to_queue() {
        when(meterStatusRepository.pushUpdateToQueue(getMeterStatusRequestForUpdate()))
                .thenReturn(getListOfMeterStatusesForUpdate());
        assertTrue(getListOfMeterStatusesForUpdate()
                .equals(meterStatusService.pushUpdateToQueue(getMeterStatusRequestForUpdate())));
    }

    @Test
    public void test_should_create_meterStatusRequest_and_persist_to_db() {
        when(meterStatusRepository.create(getMeterStatusRequest())).thenReturn(getMeterStatusRequest());
        assertTrue(getMeterStatusRequest().equals(meterStatusService.create(getMeterStatusRequest())));
    }

    @Test
    public void test_should_update_meterStatusRequest_and_persist_to_db() {
        when(meterStatusRepository.update(getMeterStatusRequestForUpdate()))
                .thenReturn(getMeterStatusRequestForUpdate());
        assertTrue(
                getMeterStatusRequestForUpdate().equals(meterStatusService.update(getMeterStatusRequestForUpdate())));
    }

    @Test
    public void test_should_be_able_to_search_meterStatus_as_per_criteria() {
        when(meterStatusRepository.getMeterStatusByCriteria(getMeterStatusGetRequest())).thenReturn(getListOfMeterStatuses());
        assertTrue(getListOfMeterStatuses().equals(meterStatusService.getMeterStatus(getMeterStatusGetRequest())));
    }

    private MeterStatusGetRequest getMeterStatusGetRequest() {
        return MeterStatusGetRequest.builder().ids(Arrays.asList(1L, 2L)).tenantId("default").sortBy("status")
                .sortOrder("desc").build();
    }

    private MeterStatusReq getMeterStatusRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").ts(1546789443L).msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterStatus meterStatus1 = MeterStatus.builder().code("23").meterStatus("Meter Not working")
                .description("meter is not working properly")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        final MeterStatus meterStatus2 = MeterStatus.builder().code("24").meterStatus("Door Lock").description("door is locked")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        return MeterStatusReq.builder().requestInfo(requestInfo).meterStatus(Arrays.asList(meterStatus1, meterStatus2)).build();
    }

    private List<MeterStatus> getListOfMeterStatusesForUpdate() {
        final MeterStatus meterStatus1 = MeterStatus.builder().code("1").meterStatus("Meter faulty")
                .description("meter is faulty")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        final MeterStatus meterStatus2 = MeterStatus.builder().code("2").meterStatus("Non access to meter")
                .description("meter is inaccessible")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        return Arrays.asList(meterStatus1, meterStatus2);
    }

    private MeterStatusReq getMeterStatusRequestForUpdate() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").ts(1546789443L).msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterStatus meterStatus1 = MeterStatus.builder().code("1").meterStatus("Meter faulty")
                .description("meter is faulty")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        final MeterStatus meterStatus2 = MeterStatus.builder().code("2").meterStatus("Non access to meter")
                .description("meter is inaccessible")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        return MeterStatusReq.builder().requestInfo(requestInfo).meterStatus(Arrays.asList(meterStatus1, meterStatus2)).build();
    }

    private List<MeterStatus> getListOfMeterStatuses() {
        final MeterStatus meterStatus1 = MeterStatus.builder().id(1L).code("1").meterStatus("Meter Not working")
                .description("meter is not working properly")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        final MeterStatus meterStatus2 = MeterStatus.builder().id(2L).code("2").meterStatus("Door Lock")
                .description("door is locked")
                .tenantId("default").createdBy(1L).createdDate(1546789443L).lastModifiedBy(1L).lastModifiedDate(1546789443L)
                .build();
        return Arrays.asList(meterStatus1, meterStatus2);
    }

}
