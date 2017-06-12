package org.egov.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.common.model.Employee;

@Getter
@Builder
public class NotificationContext {
    private SevaRequest sevaRequest;
    private ServiceType serviceType;
    private Tenant tenant;
    private Employee employee;
}
