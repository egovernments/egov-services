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

import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.DocumentTypeApplicationTypeRepository;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DocumentTypeApplicationTypeService {

    public static final Logger logger = LoggerFactory.getLogger(DocumentTypeApplicationTypeService.class);

    @Autowired
    private DocumentTypeApplicationTypeRepository docTypeApplTypeRepository;
    
    @Autowired
    private WaterMasterProducer mastertProducer;

   public DocumentTypeApplicationTypeReq create(final DocumentTypeApplicationTypeReq docNameRequest) {
       return docTypeApplTypeRepository.persistCreateDocTypeApplicationType(docNameRequest);
   }

    public DocumentTypeApplicationType sendMessage(final String topic,final String key,final DocumentTypeApplicationTypeReq docNameRequest) {
    	//docNameRequest.getDocumentName().setCode(codeGeneratorService.generate(usageTypeRequest.getUsageType().SEQ_USAGETYPE));
        final ObjectMapper mapper = new ObjectMapper();
        String docTypeApplTypeValue = null;
        try {
            logger.info("createDocumentTypeApplicationType service::" + docNameRequest);
            docTypeApplTypeValue = mapper.writeValueAsString(docNameRequest);
            logger.info("DocumentTypeApplicationTypeValue::" + docTypeApplTypeValue);
        } catch (final JsonProcessingException e) {
        	logger.error("Exception Encountered : " + e);
        }
        try {
        	mastertProducer.sendMessage(topic,key,docTypeApplTypeValue);
        } catch (final Exception ex) {
        	logger.error("Exception Encountered : " + ex);
        }
        return docNameRequest.getDocumentTypeApplicationType();
    }
    
  
    public DocumentTypeApplicationTypeReq update(final DocumentTypeApplicationTypeReq documentTypeRequest) {
        return docTypeApplTypeRepository.persistModifyDocTypeApplicationType(documentTypeRequest);
    }
    
    
    public List<DocumentTypeApplicationType> getDocumentAndApplicationTypes(DocumentTypeApplicationTypeGetRequest docNameGetRequest) {
        return docTypeApplTypeRepository.findForCriteria(docNameGetRequest);

    }
    
    public boolean checkDocumentTypeApplicationTypeExist(String applicationType,long documentType,String tenantid){
    	
    	 return docTypeApplTypeRepository.checkDocumentTypeApplicationTypeExist(documentType,applicationType,tenantid);
    }
    
    
    
    
}