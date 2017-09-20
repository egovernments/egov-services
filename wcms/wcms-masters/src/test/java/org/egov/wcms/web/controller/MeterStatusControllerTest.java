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
package org.egov.wcms.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.service.MeterStatusService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MeterStatusController.class)
@Import(TestConfiguration.class)
public class MeterStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private MeterStatusService meterStatusService;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ErrorHandler errHandler;

    @InjectMocks
    private MeterStatusController meterStatusController;

    @Test
    public void test_should_create_meter_status() throws Exception {
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(meterStatusService.pushCreateToQueue(getMeterStatusRequest())).thenReturn(getListOfMeterStatuses());
        mockMvc.perform(post("/meterStatus/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("MeterStatusRequestCreate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterStatusResponseCreate.json")));
    }

    @Test
    public void test_should_update_meter_status() throws Exception {
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(meterStatusService.pushUpdateToQueue(getMeterStatusRequestForUpdate()))
                .thenReturn(getListOfMeterStatusesForUpdate());
        mockMvc.perform(post("/meterStatus/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("MeterStatusRequestUpdate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterStatusResponseUpdate.json")));
    }

    @Test
    public void test_should_search_meter_status() throws Exception {
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(meterStatusService.getMeterStatus(getMeterStatusGetRequest())).thenReturn(listOfMeterStatuses());
        mockMvc.perform(post("/meterStatus/_search?ids=1,2&tenantId=default&sortBy=status&sortOrder=desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("MeterStatusRequest.json")))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterStatusResponse.json")));
    }

    private List<MeterStatus> listOfMeterStatuses() {
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

    private MeterStatusGetRequest getMeterStatusGetRequest() {
        return MeterStatusGetRequest.builder().ids(Arrays.asList(1L, 2L)).tenantId("default").sortBy("status")
                .sortOrder("desc").build();
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

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("failed").build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("successful").build();
    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
