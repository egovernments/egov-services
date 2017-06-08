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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
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

    @Test
    public void test_Should_Create_ApplicationTypeDocType() {
        final DocumentTypeApplicationTypeReq docNameRequest = new DocumentTypeApplicationTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(2L);
        requestInfo.setUserInfo(user);
        docNameRequest.setRequestInfo(requestInfo);
        final DocumentTypeApplicationType applicationTypDoce = Mockito.mock(DocumentTypeApplicationType.class);
        docNameRequest.setDocumentTypeApplicationType(applicationTypDoce);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(docNameRequest.equals(docTypeAppTypeRepository.persistCreateDocTypeApplicationType(docNameRequest)));
    }

    @Test
    public void test_Should_Create_ApplicationTypeDocType_NotNullCheck() {

        final DocumentTypeApplicationTypeReq applicationTypeDocReq = getApplicationTypeDoc();
        final Object[] obj = new Object[] { applicationTypeDocReq.getDocumentTypeApplicationType().getId(),
                applicationTypeDocReq.getDocumentTypeApplicationType().getApplicationType(),
                applicationTypeDocReq.getDocumentTypeApplicationType().getDocumentType(),
                applicationTypeDocReq.getDocumentTypeApplicationType().getActive(),
                applicationTypeDocReq.getDocumentTypeApplicationType().getTenantId(), new Date(new java.util.Date().getTime()),
                applicationTypeDocReq.getRequestInfo().getUserInfo().getId() };
        when(jdbcTemplate.update("query", obj)).thenReturn(1);
        assertNotNull(docTypeAppTypeRepository.persistCreateDocTypeApplicationType(applicationTypeDocReq));
    }

    private DocumentTypeApplicationTypeReq getApplicationTypeDoc() {
        final DocumentTypeApplicationTypeReq docAppliTypeRequest = new DocumentTypeApplicationTypeReq();
        final DocumentTypeApplicationType applicationDocType = new DocumentTypeApplicationType();
        applicationDocType.setActive(true);
        applicationDocType.setId(2L);
        applicationDocType.setDocumentTypeId(1234);
        applicationDocType.setTenantId("DEFAULT");
        applicationDocType.setApplicationType("New Connection");
        final User user = new User();
        user.setId(2L);
        final RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);
        docAppliTypeRequest.setDocumentTypeApplicationType(applicationDocType);
        docAppliTypeRequest.setRequestInfo(requestInfo);
        return docAppliTypeRequest;
    }

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

        final DocumentTypeApplicationTypeReq applicationDocumentRequest = new DocumentTypeApplicationTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        applicationDocumentRequest.setRequestInfo(requestInfo);
        final DocumentTypeApplicationType documentApplication = new DocumentTypeApplicationType();
        applicationDocumentRequest.setDocumentTypeApplicationType(documentApplication);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(applicationDocumentRequest
                .equals(docTypeAppTypeRepository.persistModifyDocTypeApplicationType(applicationDocumentRequest)));
    }

}
