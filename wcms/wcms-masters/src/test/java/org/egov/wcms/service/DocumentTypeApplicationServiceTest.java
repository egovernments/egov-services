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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.repository.DocumentTypeApplicationTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeApplicationServiceTest {

    @Mock
    private DocumentTypeApplicationTypeRepository documentApplicationTypeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private DocumentTypeApplicationTypeService docTypeAppTypeService;

    @Test
    public void testSearch() {
        final List<DocumentTypeApplicationType> applicationTypeDocs = new ArrayList<>();
        applicationTypeDocs.add(getApplicationTypeDoc());
        when(documentApplicationTypeRepository.findForCriteria(any(DocumentTypeApplicationTypeGetRequest.class)))
                .thenReturn(applicationTypeDocs);
        assertTrue(applicationTypeDocs
                .equals(docTypeAppTypeService.getDocumentAndApplicationTypes(any(DocumentTypeApplicationTypeGetRequest.class))));
    }

    @Test
    public void testCreate() {
        final DocumentTypeApplicationTypeReq documentAppRequest = new DocumentTypeApplicationTypeReq();
        final List<DocumentTypeApplicationType> applicationTypeDocList = new ArrayList<>();

        applicationTypeDocList.add(getApplicationTypeDoc());
        documentAppRequest.setDocumentTypeApplicationTypes(applicationTypeDocList);
        assertTrue(applicationTypeDocList.equals(docTypeAppTypeService.pushCreateToQueue("", "", documentAppRequest)));
    }

    @Test
    public void testPersist() {
        final List<DocumentTypeApplicationType> applicationTypeDocList = new ArrayList<>();
        applicationTypeDocList.add(getApplicationTypeDoc());
        final DocumentTypeApplicationTypeReq documentAppRequest = new DocumentTypeApplicationTypeReq();
        documentAppRequest.setDocumentTypeApplicationTypes(applicationTypeDocList);
        when(documentApplicationTypeRepository.create(any(DocumentTypeApplicationTypeReq.class)))
                .thenReturn(documentAppRequest);
        assertTrue(documentAppRequest.equals(docTypeAppTypeService.create(documentAppRequest)));
    }

    private DocumentTypeApplicationType getApplicationTypeDoc() {

        final DocumentTypeApplicationType appTypeDoc = new DocumentTypeApplicationType();
        appTypeDoc.setTenantId("default");
        appTypeDoc.setCode("2");
        appTypeDoc.setApplicationType("property type");
        appTypeDoc.setDocumentTypeId(123);
        appTypeDoc.setActive(true);
        return appTypeDoc;
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_ApplicationTypeDocumentType() throws Exception {

        final DocumentTypeApplicationTypeReq applicationDocumentTypeRequest = Mockito.mock(DocumentTypeApplicationTypeReq.class);
        when(documentApplicationTypeRepository.update(applicationDocumentTypeRequest))
                .thenThrow(Exception.class);

        assertTrue(applicationDocumentTypeRequest.equals(docTypeAppTypeService.update(applicationDocumentTypeRequest)));
    }

}