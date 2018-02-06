package org.egov.pgr.notification.domain.service.contextenricher;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.common.model.Designation;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.model.Position;
import org.egov.pgr.common.repository.DesignationRepository;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgr.common.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmployeeContextEnricher implements NotificationContextEnricher {

    private EmployeeRepository employeeRepository;
    private PositionRepository positionRepository;
    private DesignationRepository designationRepository;
    private Boolean escalationSmsEnabled;

    public EmployeeContextEnricher(EmployeeRepository employeeRepository,
                                   PositionRepository positionRepository,
                                   DesignationRepository designationRepository,
                                   @Value("${sms.escalation.enabled}") final Boolean escalationSmsEnabled) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.designationRepository = designationRepository;
        this.escalationSmsEnabled = escalationSmsEnabled;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        if(escalationSmsEnabled){
            final Employee employee = this.employeeRepository
                .getEmployeeByPosition(sevaRequest.getPositionId(), sevaRequest.getTenantId());
            context.setEmployee(employee);
            setDesignation(sevaRequest, context);
            setPosition(sevaRequest, context);
        }
    }

    private void setPosition(SevaRequest sevaRequest, NotificationContext context) {
        final Long positionId = context.getEmployee().getPrimaryPosition();
        final Position position = this.positionRepository
            .getPositionById(positionId, sevaRequest.getTenantId());
        context.setPreviousEmployeePosition(position);
    }

    private void setDesignation(SevaRequest sevaRequest, NotificationContext context) {
        final Long designationId = context.getEmployee().getPrimaryDesignation();
        final Designation designation = this.designationRepository
            .getDesignationById(designationId, sevaRequest.getTenantId());
        context.setEmployeeDesignation(designation);
    }
}
