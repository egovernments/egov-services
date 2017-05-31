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

import org.egov.wcms.model.DocumentType;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.producers.DocumentTypeProducer;
import org.egov.wcms.repository.DocumentTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.egov.wcms.web.contract.UsageTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DocumentTypeService {
	public static final Logger logger = LoggerFactory.getLogger(UsageTypeService.class);
	
	@Autowired
    private DocumentTypeRepository documentTypeRepository;
    
    @Autowired
    private DocumentTypeProducer documentTypeProducer;
    
    public void create(final DocumentTypeReq documentTypeRequest) {
        documentTypeRepository.persistCreateDocumentType(documentTypeRequest);
    }
    
    public DocumentType createDocumentType(final String topic,final String key,final DocumentTypeReq documentTypeRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String documentTypeValue = null;
        try {
            logger.info("documentUsageType service::" + documentTypeRequest);
            documentTypeValue = mapper.writeValueAsString(documentTypeRequest);
            logger.info("documentTypeValue::" + documentTypeValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            documentTypeProducer.sendMessage(topic,key,documentTypeValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return documentTypeRequest.getDocumentType();
    }
    
    public List<DocumentType> getDocumentTypes(DocumentTypeGetReq documentTypeGetRequest) {
        return documentTypeRepository.findForCriteria(documentTypeGetRequest);
    }
    
    public boolean checkCodeAndTenantExists(DocumentTypeReq documentTypeRequest) {
    	return documentTypeRepository.checkCodeAndTenantExists(documentTypeRequest);
    }
}
