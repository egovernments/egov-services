package org.egov.domain.model;

import lombok.*;
import org.egov.pgr.common.model.Employee;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContext {
    private SevaRequest sevaRequest;
    private ServiceType serviceType;
    private Tenant tenant;
    private Employee employee;
    private Employee previousEmployee;
}
