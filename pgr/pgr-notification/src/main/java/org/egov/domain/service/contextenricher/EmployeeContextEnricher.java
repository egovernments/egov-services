package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SevaRequest;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeContextEnricher implements NotificationContextEnricher {

    private EmployeeRepository employeeRepository;

    public EmployeeContextEnricher(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        final Employee employee = this.employeeRepository
            .getEmployeeByPosition(sevaRequest.getAssigneeId(), sevaRequest.getTenantId());
        context.setEmployee(employee);
    }
}
