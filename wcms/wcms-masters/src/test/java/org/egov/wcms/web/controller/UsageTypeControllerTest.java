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
package org.egov.wcms.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.service.UsageTypeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.egov.wcms.web.contract.UsageTypeReq;
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
@WebMvcTest(UsageTypeController.class)
@Import(TestConfiguration.class)
public class UsageTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    UsageTypeService usageTypeService;

    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private UsageTypeController usageTypeController;

    @Test
    public void test_should_create_usageType() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateUsageTypeRequest(getUsageTypeRequest(), false)).thenReturn(errorResponses);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(usageTypeService.pushCreateToQueue(getUsageTypeRequest())).thenReturn(getUsageTypeRequest().getUsageTypes());
        mockMvc.perform(post("/usagetypes/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("UsageTypeRequestCreate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("UsageTypeResponseCreate.json")));
    }

    @Test
    public void test_should_update_usageType() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateUsageTypeRequest(getUsageTypeRequest(), false)).thenReturn(errorResponses);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(usageTypeService.pushUpdateToQueue(getUsageTypeRequestForUpdate()))
                .thenReturn(getUsageTypeRequestForUpdate().getUsageTypes());
        mockMvc.perform(post("/usagetypes/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("UsageTypeRequestUpdate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("UsageTypeResponseUpdate.json")));
    }

    /*
     * @Test public void test_should_search_usageType() throws Exception {
     * when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
     * .thenReturn(getSuccessRequestInfo()); when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),
     * eq(false))) .thenReturn(getFailureRequestInfo());
     * when(usageTypeService.getUsageTypes(getUsageTypeGetRequest())).thenReturn(getUsageType());
     * mockMvc.perform(post("/usagetype/_search?parent=1&" + "tenantId=default&name=School")
     * .contentType(MediaType.APPLICATION_JSON_UTF8) .content(getFileContents("UsageTypeRequest.json")))
     * .andExpect(status().isOk()) .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andExpect(content().json(getFileContents("UsageTypeResponse.json"))); }
     */

    private List<UsageType> getUsageType() {
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(UsageType.builder().id(2L).code("2").name("School").active(true).description("water required for school")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build());

        return usageTypes;
    }

    private UsageTypeGetRequest getUsageTypeGetRequest() {
        return UsageTypeGetRequest.builder().parent("1").tenantId("default").name("School").build();
    }

    private UsageTypeReq getUsageTypeRequestForUpdate() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final UsageType usageType = UsageType.builder().name("Commercial").active(true)
                .description("water required for commercial purposes")
                .tenantId("default").createdBy(1L).createdDate(12323321L)
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final UsageType subusageType = UsageType.builder().name("Industrial").active(true)
                .description("water required for industrial purposes")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        usageTypes.add(subusageType);
        return UsageTypeReq.builder().requestInfo(requestInfo).usageTypes(usageTypes).build();
    }

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("failed").build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("successful").build();
    }

    private UsageTypeReq getUsageTypeRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final UsageType usageType = UsageType.builder().name("Trust").active(true).description("water required for trust")
                .tenantId("default").createdBy(1L).createdDate(12323321L)
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final UsageType subusageType = UsageType.builder().name("School").active(true).description("water required for school")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        usageTypes.add(subusageType);
        return UsageTypeReq.builder().requestInfo(requestInfo).usageTypes(usageTypes).build();
    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
