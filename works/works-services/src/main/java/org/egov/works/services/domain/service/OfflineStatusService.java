package org.egov.works.services.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.services.config.Constants;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.repository.OfflineStatusRepository;
import org.egov.works.services.domain.validator.RequestValidator;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.OfflineStatus;
import org.egov.works.services.web.contract.OfflineStatusRequest;
import org.egov.works.services.web.contract.OfflineStatusResponse;
import org.egov.works.services.web.contract.OfflineStatusSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OfflineStatusService {

    @Autowired
    private ServiceUtils serviceUtils;

    @Autowired
    private OfflineStatusRepository offlineStatusRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RequestValidator requestValidator;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @SuppressWarnings("unchecked")
    public Collection<String> getStatusNameDetails(final String[] statusNames) {
        return CollectionUtils.select(Arrays.asList(statusNames), statusName -> (String) statusName != null);
    }

    public OfflineStatusResponse create(OfflineStatusRequest offlineStatusRequest) {
        requestValidator.validateOfflineStatus(offlineStatusRequest,Boolean.FALSE);
        OfflineStatusResponse response = new OfflineStatusResponse();
        List<OfflineStatus> loaOfflineStatusList = new ArrayList<>();
        List<OfflineStatus> workOrderOfflineStatusList = new ArrayList<>();
        for (OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            offlineStatus.setId(commonUtils.getUUID());
            offlineStatus.setAuditDetails(serviceUtils
                    .setAuditDetails(offlineStatusRequest.getRequestInfo(), false));
            if(offlineStatus.getObjectType().equals(Constants.LETTEROFACCEPTANCE) && offlineStatus.getStatus().getCode().equals(Constants.LOA_OFFLINESTATUS_AGREEMENT_ORDER_SIGNED))
                loaOfflineStatusList.add(offlineStatus);
            if(offlineStatus.getObjectType().equals(Constants.WORKORDER) && offlineStatus.getStatus().getCode().equals(Constants.WORKORDER_OFFLINESTATUS_WORK_COMMENCED))
                workOrderOfflineStatusList.add(offlineStatus);
        }
        kafkaTemplate.send(propertiesManager.getWorksServiceOfflineStatusUpdateValidatedTopic(), offlineStatusRequest);
        kafkaTemplate.send(propertiesManager.getWorksBackUpdateLOAWithOfflineStatusTopic(), offlineStatusRequest);
        kafkaTemplate.send(propertiesManager.getWorksBackUpdateWorkOrderWithOfflineStatusTopic(), offlineStatusRequest);
        if(loaOfflineStatusList != null && !loaOfflineStatusList.isEmpty()) {
            OfflineStatusRequest backUpdateRequest = new OfflineStatusRequest();
            backUpdateRequest.setOfflineStatuses(loaOfflineStatusList);
            kafkaTemplate.send(propertiesManager.getWorksBackUpdateLOAWithAllOfflineStatusTopic(), backUpdateRequest);
        }
        if(workOrderOfflineStatusList != null && !workOrderOfflineStatusList.isEmpty()) {
            OfflineStatusRequest backUpdateRequest = new OfflineStatusRequest();
            backUpdateRequest.setOfflineStatuses(workOrderOfflineStatusList);
            kafkaTemplate.send(propertiesManager.getWorksBackUpdateWorkOrderWithAllOfflineStatusTopic(), backUpdateRequest);
        }
        response.setOfflineStatuses(offlineStatusRequest.getOfflineStatuses());
        response.setResponseInfo(
                serviceUtils.createResponseInfoFromRequestInfo(offlineStatusRequest.getRequestInfo(), true));

        return response;
    }

    public ResponseEntity<?> update(OfflineStatusRequest offlineStatusRequest) {
        requestValidator.validateOfflineStatus(offlineStatusRequest, Boolean.TRUE);

        OfflineStatusResponse response = new OfflineStatusResponse();
        List<OfflineStatus> loaOfflineStatusList = new ArrayList<>();
        List<OfflineStatus> workOrderOfflineStatusList = new ArrayList<>();
        for (OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            if (offlineStatus.getId() == null)
                offlineStatus.setId(commonUtils.getUUID());
            offlineStatus.setAuditDetails(serviceUtils
                    .setAuditDetails(offlineStatusRequest.getRequestInfo(), true));
            if(offlineStatus.getObjectType().equals(Constants.LETTEROFACCEPTANCE) && offlineStatus.getStatus().equals(Constants.LOA_OFFLINESTATUS_AGREEMENT_ORDER_SIGNED))
                loaOfflineStatusList.add(offlineStatus);
            if(offlineStatus.getObjectType().equals(Constants.WORKORDER) && offlineStatus.getStatus().getCode().equals(Constants.WORKORDER_OFFLINESTATUS_WORK_COMMENCED))
                workOrderOfflineStatusList.add(offlineStatus);
        }
        kafkaTemplate.send(propertiesManager.getWorksServiceOfflineStatusUpdateValidatedTopic(), offlineStatusRequest);
        if(loaOfflineStatusList != null && !loaOfflineStatusList.isEmpty()) {
            OfflineStatusRequest backUpdateRequest = new OfflineStatusRequest();
            backUpdateRequest.setOfflineStatuses(loaOfflineStatusList);
            kafkaTemplate.send(propertiesManager.getWorksBackUpdateLOAWithAllOfflineStatusTopic(), backUpdateRequest);
        }
        if(workOrderOfflineStatusList != null && !workOrderOfflineStatusList.isEmpty()) {
            OfflineStatusRequest backUpdateRequest = new OfflineStatusRequest();
            backUpdateRequest.setOfflineStatuses(workOrderOfflineStatusList);
            kafkaTemplate.send(propertiesManager.getWorksBackUpdateWorkOrderWithAllOfflineStatusTopic(), backUpdateRequest);
        }
        response.setOfflineStatuses(offlineStatusRequest.getOfflineStatuses());
        response.setResponseInfo(
                serviceUtils.createResponseInfoFromRequestInfo(offlineStatusRequest.getRequestInfo(), true));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public OfflineStatusResponse search(OfflineStatusSearchContract offlineStatusSearchContract, final RequestInfo requestInfo) {
        OfflineStatusResponse offlineStatusResponse = new OfflineStatusResponse();
        offlineStatusResponse.setOfflineStatuses(offlineStatusRepository.search(offlineStatusSearchContract));
        offlineStatusResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
        return offlineStatusResponse;
    }

}
