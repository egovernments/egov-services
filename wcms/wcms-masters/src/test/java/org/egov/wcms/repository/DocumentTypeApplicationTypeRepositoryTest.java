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
package org.egov.wcms.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.repository.builder.DocumentTypeApplicationTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DocumentTypeApplicationTypeMapper;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeApplicationTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DocumentTypeApplicationTypeQueryBuilder docTypeAppTypeQueryBuilder;

    @InjectMocks
    private DocumentTypeApplicationTypeRepository docTypeAppTypeRepository;

    @Mock
    private DocumentTypeApplicationTypeMapper docTypeAppliTypeRowMapper;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void test_Should_Create_ApplicationTypeDocType() {
        final DocumentTypeApplicationTypeReq docNameRequest = new DocumentTypeApplicationTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(2L);
        requestInfo.setUserInfo(user);
        docNameRequest.setRequestInfo(requestInfo);
        final List<DocumentTypeApplicationType> documentApplicationList = new ArrayList<>();
        final DocumentTypeApplicationType documentApplication = getDocumentTypeApplicationType();
        documentApplicationList.add(documentApplication);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(docNameRequest.equals(docTypeAppTypeRepository.create(docNameRequest)));
    }

    /*
     * @Test public void test_Should_Create_ApplicationTypeDocType_NotNullCheck() { final DocumentTypeApplicationType
     * applicationTypeDocReq = getApplicationTypeDoc(); final Object[] obj = new Object[] {
     * applicationTypeDocReq.getDocumentTypeApplicationType().getId(),
     * applicationTypeDocReq.getDocumentTypeApplicationType().getApplicationType(),
     * applicationTypeDocReq.getDocumentTypeApplicationType().getDocumentType(),
     * applicationTypeDocReq.getDocumentTypeApplicationType().getActive(),
     * applicationTypeDocReq.getDocumentTypeApplicationType().getTenantId(), new Date(new java.util.Date().getTime()),
     * applicationTypeDocReq.getRequestInfo().getUserInfo().getId() }; when(jdbcTemplate.update("query", obj)).thenReturn(1);
     * assertNotNull(docTypeAppTypeRepository.persistCreateDocTypeApplicationType(applicationTypeDocReq)); }
     */

    @Test
    public void test_Should_Find_ApplicationTypeDocType_Valid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final DocumentTypeApplicationTypeGetRequest docNameGetRequest = Mockito.mock(DocumentTypeApplicationTypeGetRequest.class);
        final String queryString = "MyQuery";
        when(docTypeAppTypeQueryBuilder.getQuery(docNameGetRequest, preparedStatementValues)).thenReturn(queryString);
        final List<DocumentTypeApplicationType> appTypeDocs = new ArrayList<>();
        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), docTypeAppliTypeRowMapper))
                .thenReturn(appTypeDocs);

        assertTrue(appTypeDocs.equals(docTypeAppTypeRepository.findForCriteria(docNameGetRequest)));
    }

    @Test
    public void test_Should_Find_ApplicationTypeDocType_Invalid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final DocumentTypeApplicationTypeGetRequest applicationTypeGetRequest = Mockito
                .mock(DocumentTypeApplicationTypeGetRequest.class);
        final String queryString = "MyQuery";
        when(docTypeAppTypeQueryBuilder.getQuery(applicationTypeGetRequest, preparedStatementValues)).thenReturn(null);
        final List<DocumentTypeApplicationType> applicationTypeDocs = new ArrayList<>();
        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), docTypeAppliTypeRowMapper))
                .thenReturn(applicationTypeDocs);

        assertTrue(applicationTypeDocs.equals(docTypeAppTypeRepository.findForCriteria(applicationTypeGetRequest)));
    }

    @Test
    public void test_Should_Update_ApplicationTypeDocumentType() {
        final DocumentTypeApplicationTypeReq documentTypeApplicationTypeReq = new DocumentTypeApplicationTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        documentTypeApplicationTypeReq.setRequestInfo(requestInfo);
        final List<DocumentTypeApplicationType> documentTypeApplicationTypeList = new ArrayList<>();
        final DocumentTypeApplicationType documentApplication = getDocumentTypeApplicationType();
        documentTypeApplicationTypeList.add(documentApplication);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(documentTypeApplicationTypeReq
                .equals(docTypeAppTypeRepository.update(documentTypeApplicationTypeReq)));
    }

    private DocumentTypeApplicationType getDocumentTypeApplicationType() {
        final DocumentTypeApplicationType documentApplication = new DocumentTypeApplicationType();
        documentApplication.setActive(true);
        documentApplication.setCode("2");
        documentApplication.setId(2L);
        documentApplication.setDocumentTypeId(1234);
        documentApplication.setTenantId("default");
        documentApplication.setApplicationType("New Connection");
        return documentApplication;
    }
}
