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

import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.model.DocumentType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.DocumentTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DocumentTypeService {

    public static final Logger logger = LoggerFactory.getLogger(DocumentTypeService.class);

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private WaterMasterProducer waterMasterProducer;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public DocumentTypeReq create(final DocumentTypeReq documentTypeReq) {
        return documentTypeRepository.persistCreateDocumentType(documentTypeReq);
    }

    public DocumentTypeReq update(final DocumentTypeReq documentTypeReq) {
        return documentTypeRepository.persistModifyDocumentType(documentTypeReq);
    }

    public DocumentType sendMessage(final String topic, final String key, final DocumentTypeReq documentTypeReq) {

        if (key.equalsIgnoreCase("documenttype-create")) {
            documentTypeReq.getDocumentType();
            documentTypeReq.getDocumentType()
                    .setCode(codeGeneratorService.generate(DocumentType.SEQ_DOCUMENTTYPE));
        }

        final ObjectMapper mapper = new ObjectMapper();
        String documentTypeValue = null;
        try {
            logger.info("createDocumentType service::" + documentTypeReq);
            documentTypeValue = mapper.writeValueAsString(documentTypeReq);
            logger.info("documentTypeValue::" + documentTypeValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, documentTypeValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return documentTypeReq.getDocumentType();
    }

    public boolean getDocumentTypeByNameAndCode(final String code, final String name, final String tenantId) {
        return documentTypeRepository.checkDocumentTypeByNameAndCode(code, name, tenantId);
    }

    public List<DocumentType> getDocumentTypes(final DocumentTypeGetReq documentTypeGetRequest) {
        return documentTypeRepository.findForCriteria(documentTypeGetRequest);

    }

    public List<Long> getAllMandatoryDocs(final String applicationType) {
        List<Long> mandatoryDocs = new ArrayList<>();
        try {
            mandatoryDocs = documentTypeRepository.getMandatoryocs(applicationType);
        } catch (final Exception e) {
            logger.error("There are no mandatory docs for this application type", e.getMessage());
            return mandatoryDocs;
        }
        return mandatoryDocs;
    }

}
