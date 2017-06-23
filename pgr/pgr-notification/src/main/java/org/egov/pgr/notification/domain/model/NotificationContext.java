package org.egov.pgr.notification.domain.model;

import lombok.*;
import org.egov.pgr.common.model.Designation;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.model.Location;
import org.egov.pgr.common.model.Position;

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
    private Position employeePosition;
    private Designation employeeDesignation;
    private Employee previousEmployee;
    private Position previousEmployeePosition;
    private Designation previousEmployeeDesignation;
    private Location location;
}
