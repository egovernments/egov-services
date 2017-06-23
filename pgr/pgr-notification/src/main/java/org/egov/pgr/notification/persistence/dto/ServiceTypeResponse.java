package org.egov.pgr.notification.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceTypeResponse {
    private static final String SERVICE_NAME_NOT_AVAILABLE_MESSAGE = "SERVICE NAME NOT AVAILABLE";
    private ResponseInfo responseInfo;
    private List<ServiceType> complaintTypes;

    public org.egov.pgr.notification.domain.model.ServiceType toDomain() {
        return new org.egov.pgr.notification.domain.model.ServiceType(getServiceName(), getKeywords());
    }

    private List<String> getKeywords() {
        if (CollectionUtils.isEmpty(complaintTypes)) {
            return Collections.emptyList();
        } else {
            return complaintTypes.get(0).getKeywords();
        }
    }

    private String getServiceName() {
        if (CollectionUtils.isEmpty(complaintTypes)) {
            return SERVICE_NAME_NOT_AVAILABLE_MESSAGE;
        } else {
            return complaintTypes.get(0).getServiceName();
        }
    }
}