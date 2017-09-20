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

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.repository.DocumentTypeApplicationTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentTypeApplicationTypeService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private DocumentTypeApplicationTypeRepository docTypeApplTypeRepository;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public DocumentTypeApplicationTypeReq create(final DocumentTypeApplicationTypeReq docNameRequest) {
        return docTypeApplTypeRepository.create(docNameRequest);
    }

    public DocumentTypeApplicationTypeReq update(final DocumentTypeApplicationTypeReq documentTypeRequest) {
        return docTypeApplTypeRepository.update(documentTypeRequest);
    }

    public List<DocumentTypeApplicationType> pushCreateToQueue(final String topic, final String key,
            final DocumentTypeApplicationTypeReq docmentApplicationRequest) {
        for (final DocumentTypeApplicationType documentApplication : docmentApplicationRequest.getDocumentTypeApplicationTypes())
            documentApplication
                    .setCode(codeGeneratorService.generate(DocumentTypeApplicationType.SEQ_DOCUMENT_TYPE_APPLICATION_TYPE));

        try {
            kafkaTemplate.send(topic, key, docmentApplicationRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return docmentApplicationRequest.getDocumentTypeApplicationTypes();
    }

    public List<DocumentTypeApplicationType> pushUpdateToQueue(final String topic, final String key,
            final DocumentTypeApplicationTypeReq docmentApplicationRequest) {
        try {
            kafkaTemplate.send(topic, key, docmentApplicationRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return docmentApplicationRequest.getDocumentTypeApplicationTypes();
    }

    public List<DocumentTypeApplicationType> getDocumentAndApplicationTypes(
            final DocumentTypeApplicationTypeGetRequest docNameGetRequest) {
        return docTypeApplTypeRepository.findForCriteria(docNameGetRequest);

    }

    public boolean checkDocumentTypeApplicationTypeExist(final String code, final String applicationType,
            final String documentType, final String tenantid) {
        return docTypeApplTypeRepository.checkDocumentTypeApplicationTypeExist(code, applicationType, documentType,
                tenantid);
    }

    public boolean checkDocumentTypeExists(final String documentName, final String tenantId) {
        return docTypeApplTypeRepository.checkDocumentTypeExists(documentName, tenantId);
    }

}