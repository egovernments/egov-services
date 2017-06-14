package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SevaRequest;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class PreviousEmployeeContextEnricher implements NotificationContextEnricher {

    private EmployeeRepository employeeRepository;

    public PreviousEmployeeContextEnricher(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        if (sevaRequest.getPreviousAssignee() == null) {
            return;
        }
        setEmployee(sevaRequest, context);
    }

    private void setEmployee(SevaRequest sevaRequest, NotificationContext notificationContext) {
        final Employee employee = this.employeeRepository
            .getEmployeeByPosition(sevaRequest.getPreviousAssignee(), sevaRequest.getTenantId());
        notificationContext.setPreviousEmployee(employee);
    }
}
