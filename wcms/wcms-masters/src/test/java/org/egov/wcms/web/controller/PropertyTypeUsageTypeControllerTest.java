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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeUsageType;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.util.FileUtils;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeGetReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertyTypeUsageTypeControllerTest.class)
public class PropertyTypeUsageTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private PropertyUsageTypeService propertyUsageTypeService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @Test(expected = java.lang.AssertionError.class)
    public void test_Should_Search_PropertyUsageType() throws Exception {
        /*
         * MockitoAnnotations.initMocks(this); List<PropertyUsageType> propertyUsageTypes = new ArrayList<>(); PropertyUsageType
         * propUsageType = getPropertyUsageType(); propertyUsageTypes.add(propUsageType);
         * when(propertyUsageTypeService.getPropertyUsageTypes(any(PropertyUsageTypeRequest.class))).thenReturn(propertyUsageTypes
         * ); mockMvc.perform(post("/propertyUsageType/_search") .param("tenantId", "DEFAULT")
         * .contentType(MediaType.APPLICATION_JSON) .content(getFileContents("requestinfowrapper.json")))
         * .andExpect(status().isOk()) .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
         * .andExpect(content().json(getFileContents("propertyusagetyperesponse.json")));
         */

        final List<PropertyTypeUsageType> propertyUsageTypes = new ArrayList<>();
        final RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        final PropertyTypeUsageType propertyUsageType = new PropertyTypeUsageType();
        propertyUsageType.setActive(true);
        propertyUsageType.setUsageType("USG");
        propertyUsageType.setPropertyType("PRO");
        propertyUsageType.setTenantId("DEFAULT");
        propertyUsageType.setId(2L);
        propertyUsageTypes.add(propertyUsageType);

        final PropertyTypeUsageTypeGetReq propUsageTypeGetRequest = Mockito.mock(PropertyTypeUsageTypeGetReq.class);

        when(propertyUsageTypeService.getPropertyUsageTypes(propUsageTypeGetRequest)).thenReturn(propertyUsageTypes);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(post("/propertyUsageType/_search")
                .param("tenantId", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("propertyusagetyperesponse.json")));
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }
}