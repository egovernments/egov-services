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

package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.DocumentType;
import org.egov.wcms.repository.DocumentTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.DocumentTypeRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeServiceTest {

    @Mock
    private DocumentTypeRepository documentRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private DocumentTypeService docTypeService;

    @Test
    public void test_Search_For_DocumentTypeServices() {
        final List<DocumentType> documentTypeList = new ArrayList<>();
        final DocumentType docType = Mockito.mock(DocumentType.class);
        documentTypeList.add(docType);

        when(documentRepository.findForCriteria(any(DocumentTypeGetReq.class))).thenReturn(documentTypeList);
        assertTrue(documentTypeList.equals(docTypeService.getDocumentTypes(any(DocumentTypeGetReq.class))));
    }

    @Test
    public void test_Should_DocumentType_Create() {
        final List<DocumentType> docTypes = new ArrayList<>();
        docTypes.add(getDocumentType());
        final DocumentTypeReq docTypeReq = new DocumentTypeReq();
        docTypeReq.setDocumentTypes(docTypes);
        final DocumentTypeRes propUsageTypeResponse = new DocumentTypeRes();
        propUsageTypeResponse.setResponseInfo(null);
        propUsageTypeResponse.setDocumentTypes(docTypes);

        when(docTypeService.create(any(DocumentTypeReq.class))).thenReturn(docTypeReq);
        assertTrue(docTypeReq.equals(docTypeService.create(docTypeReq)));

    }

    private DocumentType getDocumentType() {
        final DocumentType docType = new DocumentType();
        docType.setCode("23");
        docType.setName("New Document Name");
        docType.setDescription("Document Name Description");
        docType.setActive(true);
        return docType;
    }

    @Test
    public void test_Search_For_DocumentType_Notnull() {
        final List<DocumentType> docTypeList = new ArrayList<>();
        final DocumentType docType = Mockito.mock(DocumentType.class);
        docTypeList.add(docType);

        when(documentRepository.findForCriteria(any(DocumentTypeGetReq.class))).thenReturn(docTypeList);
        assertNotNull(docTypeService.getDocumentTypes(any(DocumentTypeGetReq.class)));
    }

    @Test
    public void test_Search_For_Document_Types_Null() {
        final List<DocumentType> docTypeList = new ArrayList<>();
        final DocumentType docType = Mockito.mock(DocumentType.class);
        docTypeList.add(docType);

        when(documentRepository.findForCriteria(any(DocumentTypeGetReq.class))).thenReturn(null);
        assertNull(docTypeService.getDocumentTypes(any(DocumentTypeGetReq.class)));
    }

    public List<DocumentType> getDocumentTypes(final DocumentTypeGetReq docTypeGetRequest) {
        return documentRepository.findForCriteria(docTypeGetRequest);
    }

    @Test
    public void test_Should_Update_DocumentType_NotNull() throws Exception {
        final DocumentTypeReq docTypeRequest = new DocumentTypeReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final List<DocumentType> docTypeList = new ArrayList<>();
        docTypeList.add(getDocumentType());

        docTypeRequest.setRequestInfo(requestInfo);
        docTypeRequest.setDocumentTypes(docTypeList);

        final List<DocumentType> docTypeResult = docTypeService.pushCreateToQueue("topic", "key", docTypeRequest);

        assertNotNull(docTypeResult);
    }

    @Test
    public void test_Should_Update_DocumentType() {
        final List<DocumentType> docTypesList = new ArrayList<>();
        docTypesList.add(getDocumentType());
        final DocumentTypeReq docTypeRequest = new DocumentTypeReq();
        docTypeRequest.setDocumentTypes(docTypesList);
        final DocumentTypeRes docTypeResponse = new DocumentTypeRes();
        docTypeResponse.setResponseInfo(null);
        docTypeResponse.setDocumentTypes(docTypesList);

        when(docTypeService.update(any(DocumentTypeReq.class))).thenReturn(docTypeRequest);
        assertTrue(docTypeRequest.equals(docTypeService.update(docTypeRequest)));

    }

}