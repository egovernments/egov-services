package org.egov.works.masters.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.RemarksRepository;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.egov.works.masters.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RemarksService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MasterUtils masterUtils;

    @Autowired
    private RemarksRepository remarksRepository;

    public RemarksResponse create(RemarksRequest remarksRequest) {
        validateRemarks(remarksRequest);
        AuditDetails auditDetails = masterUtils.getAuditDetails(remarksRequest.getRequestInfo(),false);
        CommonUtils commonUtils = new CommonUtils();
        for (Remarks remarks : remarksRequest.getRemarks()) {
            remarks.setId(commonUtils.getUUID());
            remarks.setAuditDetails(auditDetails);
            for (RemarksDetail remarksDetail : remarks.getRemarksDetails()) {
                remarksDetail.setId(commonUtils.getUUID());
                remarksDetail.setAuditDetails(auditDetails);
                remarksDetail.setRemarks(remarks.getId());
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMasterRemarksCreateAndUpdateTopic(), remarksRequest);
        final RemarksResponse response = new RemarksResponse();
        response.setRemarks(remarksRequest.getRemarks());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(remarksRequest.getRequestInfo(), true));
        return response;
    }

    public RemarksResponse update(RemarksRequest remarksRequest) {
        AuditDetails auditDetails = masterUtils.getAuditDetails(remarksRequest.getRequestInfo(),false);
        CommonUtils commonUtils = new CommonUtils();
        for (Remarks remarks : remarksRequest.getRemarks()) {
            if(StringUtils.isBlank(remarks.getId()))
              remarks.setId(commonUtils.getUUID());
            remarks.setAuditDetails(auditDetails);
            for (RemarksDetail remarksDetail : remarks.getRemarksDetails()) {
                if(StringUtils.isBlank(remarksDetail.getId()))
                  remarksDetail.setId(commonUtils.getUUID());
                remarksDetail.setAuditDetails(auditDetails);
                remarksDetail.setRemarks(remarks.getId());
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMasterRemarksCreateAndUpdateTopic(), remarksRequest);
        final RemarksResponse response = new RemarksResponse();
        response.setRemarks(remarksRequest.getRemarks());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(remarksRequest.getRequestInfo(), true));
        return response;
    }

    public RemarksResponse search(RemarksSearchContract remarksSearchContract, final RequestInfo requestInfo ) {
        RemarksResponse remarksResponse = new RemarksResponse();
        remarksResponse.setRemarks(remarksRepository.search(remarksSearchContract));
        remarksResponse.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(requestInfo, true));
        return remarksResponse;
    }

    private void validateRemarks(RemarksRequest remarksRequest) {
        HashMap<String, String> messages = new HashMap<>();

        for(Remarks remarks : remarksRequest.getRemarks()) {
            if(remarks.getTypeOfDocument().equalsIgnoreCase(CommonConstants.WORKORDER) && !(remarks.getRemarksType().equalsIgnoreCase(Constants.REMARKS_TYPE_APPROVAL) || remarks.getRemarksType().equalsIgnoreCase(Constants.REMARKS_TYPE_REJECTION)))
                 messages.put(Constants.KEY_REMARKS_TYPE_INVALID,  Constants.MESSAGE_REMARKS_TYPE_INVALID);

            if(remarks.getTypeOfDocument().equalsIgnoreCase(CommonConstants.NOTICE) && !remarks.getRemarksType().equalsIgnoreCase(Constants.REMARKS_TYPE_SHOWCAUSE))
                messages.put(Constants.KEY_REMARKS_TYPE_INVALID,  Constants.MESSAGE_REMARKS_TYPE_INVALID);

        }

        if(!messages.isEmpty())
            throw new CustomException(messages);
    }

}
