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
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.util.FileUtils;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentTypeApplicationTypeController.class)
@Import(TestConfiguration.class)
public class DocumentTypeApplicationTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private DocumentTypeApplicationTypeService docNameService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ValidatorUtils validatorUtils;

    @Test(expected = Exception.class)
    public void test_Should_Search_ApplicationDocument() throws Exception {

        final List<DocumentTypeApplicationType> docNames = new ArrayList<>();
        final RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        final DocumentTypeApplicationType docName = new DocumentTypeApplicationType();
        docName.setActive(true);
        docName.setApplicationType("Additional Connection");
        docName.setDocumentTypeId(123);
        docName.setTenantId("1");

        docNames.add(docName);

        final DocumentTypeApplicationTypeGetRequest docNameGetRequest = Mockito.mock(DocumentTypeApplicationTypeGetRequest.class);

        when(docNameService.getDocumentAndApplicationTypes(docNameGetRequest)).thenReturn(docNames);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(post("/documentName/_search")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("RequestInfo.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("propertycategoryresponse.json")));
    }

    private String getFileContents(final String filePath) throws IOException {
        return new FileUtils().getFileContents(
                "org/egov/wcms/web/controller/DocumentNamesController/" + filePath);
    }
}