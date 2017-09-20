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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.service.GapcodeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(GapcodeController.class)
@Import(TestConfiguration.class)
public class GapcodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GapcodeService gapcodeService;

    @InjectMocks
    private GapcodeController gapcodeController;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ErrorHandler errHandler;

    @Test
    public void test_should_create_gapcode() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateGapcodeRequest(getGapcodeRequest(), false))
                .thenReturn(errorResponses);
        when(
                gapcodeService.pushCreateToQueue(
                        applicationProperties.getCreateGapcodeTopicName(),
                        "gapcode-create", getGapcodeRequest())).thenReturn(
                                getListOfGapcode());
        when(
                responseInfoFactory.createResponseInfoFromRequestInfo(
                        any(RequestInfo.class), eq(true))).thenReturn(
                                getSuccessRequestInfo());
        when(
                responseInfoFactory.createResponseInfoFromRequestInfo(
                        any(RequestInfo.class), eq(false))).thenReturn(
                                getFailureRequestInfo());
        final ResultActions msb = mockMvc.perform(post("/gapcodes/_create")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                        getFileContents("GapcodeRequestCreate.json")));
        msb.andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8));
        // .andExpect(content().json(getFileContents("GapcodeResponseCreate.json")));
    }

    @Test(expected = Exception.class)
    public void test_Should_Search_PropertyCategory() throws Exception {

        final List<Gapcode> glist = new ArrayList<>();
        final RequestInfo requestInfo = new RequestInfo();
        final ResponseInfo responseInfo = new ResponseInfo();
        final Gapcode gapcode = new Gapcode();
        gapcode.setActive(true);
        gapcode.setCode("Gapcode1");
        gapcode.setTenantId("1");

        glist.add(gapcode);

        final GapcodeGetRequest propertyCategoryGetRequest = Mockito
                .mock(GapcodeGetRequest.class);

        when(gapcodeService.getGapcodes(propertyCategoryGetRequest))
                .thenReturn(glist);
        when(
                responseInfoFactory.createResponseInfoFromRequestInfo(
                        requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(
                post("/gapcodes/_search?").param("tenantId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json(getFileContents("GapcodeResponse.json")));
    }

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("emp").ver("1.0")
                .resMsgId("uief87324").msgId("20170826").status("failed")
                .build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("emp").ver("1.0")
                .resMsgId("uief87324").msgId("20170826").ts(1L)
                .status("successful").build();
    }

    private List<Gapcode> getListOfGapcode() {
        final Gapcode gapcode = Gapcode.builder().id(1L).code("Gapcode123")
                .logic("logic").active(true).tenantId("default").build();
        return Arrays.asList(gapcode);
    }

    private GapcodeRequest getGapcodeRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("emp")
                .ver("1.0").action("POST").did("4354648646").key("xyz")
                .msgId("20170826").authToken("345678f").userInfo(userInfo)
                .build();
        final Gapcode gapcode = Gapcode.builder().id(1L).code("Gapcode123")
                .outSideUlb(true).noOfMonths("Gapcode Test 123").logic("")
                .description("").active(true).tenantId("default").build();
        return GapcodeRequest.builder().requestInfo(requestInfo)
                .gapcode(Arrays.asList(gapcode)).build();

    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}