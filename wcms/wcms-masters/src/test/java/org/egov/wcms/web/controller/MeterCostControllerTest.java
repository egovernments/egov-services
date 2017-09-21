
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.MeterCostGetRequest;
import org.egov.wcms.web.contract.MeterCostReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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
@WebMvcTest(MeterCostController.class)
@Import(TestConfiguration.class)
public class MeterCostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MeterCostService meterCostService;

    @InjectMocks
    private MeterCostController meterCostController;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private ErrorHandler errHandler;

    @Test
    public void test_should_create_meter_cost() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateMeterCostRequest(getMeterCostRequest(), false)).thenReturn(errorResponses);
        when(meterCostService.pushCreateToQueue(any(MeterCostReq.class))).thenReturn(getListOfMeterCosts());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        mockMvc.perform(post("/metercosts/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("MeterCostRequestCreate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterCostResponseCreate.json")));
    }

    @Test
    public void test_should_update_meter_cost() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(validatorUtils.validateMeterCostRequest(getMeterCostRequestForUpdate(), true)).thenReturn(errorResponses);
        when(meterCostService.pushUpdateToQueue(getMeterCostRequestForUpdate()))
                .thenReturn(getListOfUpdatedMeterCosts());
        mockMvc.perform(post("/metercosts/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("MeterCostRequestUpdate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterCostResponseUpdate.json")));

    }

    @Test
    public void test_should_search_meter_cost() throws Exception {
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(meterCostService.getMeterCostByCriteria(getMeterCostGetRequest())).thenReturn(getSearchResult());
        mockMvc.perform(post("/metercosts/_search?ids=1,2&active=true&tenantId=default&sortBy=code&sortOrder=desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("MeterCostRequest.json")))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("MeterCostResponse.json")));

    }

    private List<MeterCost> getSearchResult() {
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("Meter").meterMake("meterMake123")
                .amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("Weter").meterMake("meterMake234")
                .amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        return Arrays.asList(meterCost2, meterCost1);
    }

    private MeterCostGetRequest getMeterCostGetRequest() {
        return MeterCostGetRequest.builder().active(true).ids(Arrays.asList(1L, 2L)).tenantId("default").sortBy("code")
                .sortOrder("desc").build();
    }

    private List<MeterCost> getListOfUpdatedMeterCosts() {
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("1")
                .meterMake("meterMakeUpdated1").amount(3000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("2")
                .meterMake("meterMakeUpdated2").amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        return Arrays.asList(meterCost1, meterCost2);
    }

    private MeterCostReq getMeterCostRequestForUpdate() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterCost meterCost1 = MeterCost.builder().code("1")
                .meterMake("meterMakeUpdated1").amount(3000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().code("2")
                .meterMake("meterMakeUpdated2").amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

    }

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("failed").build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("successful").build();
    }

    private List<MeterCost> getListOfMeterCosts() {
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("1").meterMake("meterMake123")
                .amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("2").meterMake("meterMake234")
                .amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        return Arrays.asList(meterCost1, meterCost2);
    }

    private MeterCostReq getMeterCostRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterCost meterCost1 = MeterCost.builder().meterMake("meterMake123")
                .amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().meterMake("meterMake234")
                .amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
