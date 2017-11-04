package org.egov.works.services.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.exception.ErrorCode;
import org.egov.works.services.domain.exception.InvalidDataException;
import org.egov.works.services.domain.repository.DocumentDetailRepository;
import org.egov.works.services.domain.repository.FileStoreRepository;
import org.egov.works.services.web.contract.AuditDetails;
import org.egov.works.services.web.contract.DocumentDetail;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.egov.works.services.web.contract.DocumentDetailSearchRequest;
import org.egov.works.services.web.contract.FileStoreResponse;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentDetailsService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private DocumentDetailRepository documentDetailRepository;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    public List<DocumentDetail> createDocuments(final DocumentDetailRequest documentDetailRequest) {
        for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
            document.setId(UUID.randomUUID().toString().replace("-", ""));
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedTime(new Date().getTime());
            auditDetails.setLastModifiedTime(new Date().getTime());
            RequestInfo requestInfo = documentDetailRequest.getRequestInfo();
            if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
                auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            document.setAuditDetails(auditDetails);
        }
        kafkaTemplate.send(propertiesManager.getDocumentDetailsCreateTopic(), documentDetailRequest);
        return documentDetailRequest.getDocumentDetails();
    }

    public List<DocumentDetail> updateDocuments(final DocumentDetailRequest documentDetailRequest) {
        for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
            AuditDetails auditDetails = document.getAuditDetails();
            if (auditDetails == null) {
                auditDetails = new AuditDetails();
            }
            RequestInfo requestInfo = documentDetailRequest.getRequestInfo();
            auditDetails.setLastModifiedTime(new Date().getTime());
            if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            document.setAuditDetails(auditDetails);
        }
        kafkaTemplate.send(propertiesManager.getDocumentDetailsUpdateTopic(), documentDetailRequest);
        return documentDetailRequest.getDocumentDetails();
    }

    public List<DocumentDetail> searchDocuments(final DocumentDetailSearchCriteria documentDetailSearchCriteria) {
          return documentDetailRepository.findForCriteria(documentDetailSearchCriteria);
    }

    public void validateDocuments(final DocumentDetailRequest documentDetailRequest) {

        for(DocumentDetail documentDetail : documentDetailRequest.getDocumentDetails()) {
            if(StringUtils.isNotBlank(documentDetail.getFileStore())) {
                boolean fileExists = fileStoreRepository.searchFileStore(documentDetail.getTenantId(), documentDetail.getFileStore(), documentDetailRequest.getRequestInfo());
                if(fileExists)
                    throw new InvalidDataException("fileStore", ErrorCode.INVALID_REF_VALUE.getCode(), null);
            }
        }
    }

    public void validateSearchDocuments(final DocumentDetailSearchRequest documentDetailSearchRequest) {

            if(StringUtils.isBlank(documentDetailSearchRequest.getTenantId())) {
                throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), null);
            }

    }


}
