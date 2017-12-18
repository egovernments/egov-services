package org.egov.works.qualitycontrol.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.qualitycontrol.config.PropertiesManager;
import org.egov.works.qualitycontrol.persistence.repository.QualityTestingRepository;
import org.egov.works.qualitycontrol.utils.QualityTestingUtils;
import org.egov.works.qualitycontrol.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QualityTestingService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private QualityTestingUtils qualityTestingUtils;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private QualityTestingRepository qualityTestingRepository;

    public QualityTestingResponse create(final QualityTestingRequest qualityTestingRequest) {
        AuditDetails auditDetails = qualityTestingUtils.setAuditDetails(qualityTestingRequest.getRequestInfo());
        for (QualityTesting qualityTesting : qualityTestingRequest.getQualityTestings()) {
            qualityTesting.setId(UUID.randomUUID().toString().replace("-", ""));
            qualityTesting.setAuditDetails(auditDetails);

            if(qualityTesting.getQualityTestingDetails() != null) {
                for(QualityTestingDetail qualityTestingDetail : qualityTesting.getQualityTestingDetails()){
                    qualityTestingDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                    qualityTestingDetail.setAuditDetails(auditDetails);
                    qualityTestingDetail.setQualityTesting(qualityTesting.getId());
                }
            }

            if(qualityTesting.getTestReports() != null) {
                for(DocumentDetail documentDetail : qualityTesting.getTestReports()) {
                    documentDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                    documentDetail.setAuditDetails(auditDetails);
                }
            }
        }

        kafkaTemplate.send(propertiesManager.getQualityTestingCreateAndUpdateTopic(), qualityTestingRequest);
        final QualityTestingResponse response = new QualityTestingResponse();
        response.setQualityTestings(qualityTestingRequest.getQualityTestings());
        response.setResponseInfo(qualityTestingUtils.getResponseInfo(qualityTestingRequest.getRequestInfo()));
        return response;
    }

    public QualityTestingResponse update(final QualityTestingRequest qualityTestingRequest) {
        AuditDetails auditDetails = qualityTestingUtils.setAuditDetails(qualityTestingRequest.getRequestInfo());
        for (QualityTesting qualityTesting : qualityTestingRequest.getQualityTestings()) {
            qualityTesting.setAuditDetails(auditDetails);

            if(qualityTesting.getQualityTestingDetails() != null) {
                for(QualityTestingDetail qualityTestingDetail : qualityTesting.getQualityTestingDetails()) {
                    if(qualityTestingDetail.getId() == null)
                        qualityTestingDetail.setId(UUID.randomUUID().toString().replace("-", ""));

                    qualityTestingDetail.setAuditDetails(auditDetails);
                    qualityTestingDetail.setQualityTesting(qualityTesting.getId());
                }
            }

            if(qualityTesting.getTestReports() != null) {
                for(DocumentDetail documentDetail : qualityTesting.getTestReports()) {
                    if(documentDetail.getId() == null)
                        documentDetail.setId(UUID.randomUUID().toString().replace("-", ""));

                    documentDetail.setAuditDetails(auditDetails);
                }
            }
        }

        kafkaTemplate.send(propertiesManager.getQualityTestingCreateAndUpdateTopic(), qualityTestingRequest);
        final QualityTestingResponse response = new QualityTestingResponse();
        response.setQualityTestings(qualityTestingRequest.getQualityTestings());
        response.setResponseInfo(qualityTestingUtils.getResponseInfo(qualityTestingRequest.getRequestInfo()));
        return response;
    }

    public QualityTestingResponse search(final QualityTestingSearchContract qualityTestingSearchContract, final RequestInfo requestInfo) {
        QualityTestingResponse qualityTestingResponse = new QualityTestingResponse();
        qualityTestingResponse.setQualityTestings(qualityTestingRepository.searchQualityTesting(qualityTestingSearchContract, requestInfo));
        qualityTestingResponse.setResponseInfo(qualityTestingUtils.getResponseInfo(requestInfo));
        return qualityTestingResponse;
    }

}
