package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Value
public class ServiceRequestType {
    private String name;
    private String code;
    private String tenantId;
    
    public boolean isAbsent() {
        return isEmpty(code);
    }
}
