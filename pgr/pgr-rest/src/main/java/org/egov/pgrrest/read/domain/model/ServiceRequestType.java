package org.egov.pgrrest.read.domain.model;

import lombok.*;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Getter
@Builder
public class ServiceRequestType {
    private String name;
    private String code;
    private String tenantId;
    private ServiceType serviceType;
    
    public boolean isAbsent() {
        return isEmpty(code);
    }

    public boolean isComplaintType() {
        return ServiceType.COMPLAINT.equals(serviceType);
    }

    public void setServiceType(boolean isComplaintType) {
        serviceType = isComplaintType ? ServiceType.COMPLAINT : ServiceType.CITIZEN_SERVICE;
    }

    public ServiceDefinitionSearchCriteria getSearchCriteria() {
        return new ServiceDefinitionSearchCriteria(code, tenantId);
    }
}

