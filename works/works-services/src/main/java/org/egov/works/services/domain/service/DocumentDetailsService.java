package org.egov.works.services.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.model.AuditDetails;
import org.egov.works.services.web.model.DocumentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DocumentDetailsService {

    @Value("${kafka.topics.works.documentdetails.create.name}")
    private String documentDetailsCreateTopic;

    @Value("${kafka.topics.works.documentdetails.update.name}")
    private String documentDetailsUpdateTopic;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public List<DocumentDetail> createDocuments(final DocumentDetailRequest documentDetailRequest) {
        for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
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
        kafkaTemplate.send(documentDetailsCreateTopic, documentDetailRequest);
        return documentDetailRequest.getDocumentDetails();
    }


}
