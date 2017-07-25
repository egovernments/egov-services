package org.egov.web.indexer.enricher;

import lombok.extern.slf4j.Slf4j;
import org.egov.web.indexer.contract.*;
import org.egov.web.indexer.repository.DepartmentRepository;
import org.egov.web.indexer.repository.EmployeeRepository;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class EmployeeDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String POSITION_ID = "systemPositionId";
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    public EmployeeDocumentEnricher(DepartmentRepository departmentRepository,
                                    EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return true;
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        final String positionId = getPosition(serviceRequest);
        if (positionId == null) {
            return;
        }
        document.setAssigneeId(Long.valueOf(positionId));
        setEmployee(document, serviceRequest, Long.valueOf(positionId));
    }

    private String getPosition(ServiceRequest serviceRequest) {
        return serviceRequest.getDynamicSingleValue(POSITION_ID);
    }

    private void setEmployee(ServiceRequestDocument document, ServiceRequest serviceRequest, Long positionId) {
        Employee employee = employeeRepository
            .fetchEmployeeByPositionId(positionId, new LocalDate(), serviceRequest.getTenantId());
        if (employee == null) {
            return;
        }
        document.setAssigneeName(employee.getName());
        setDepartment(document, serviceRequest, employee);
    }

    private void setDepartment(ServiceRequestDocument document, ServiceRequest serviceRequest, Employee employee) {
        if (employee.getAssignments().isEmpty()) {
            return;
        }
        Assignment assignment = employee.getAssignments().get(0);
        DepartmentRes department = departmentRepository
            .getDepartmentById(assignment.getDepartment(), serviceRequest.getTenantId());
        if (department == null || CollectionUtils.isEmpty(department.getDepartment())) {
            return;
        }
        document.setDepartmentName(department.getDepartment().get(0).getName());
        document.setDepartmentCode(department.getDepartment().get(0).getCode());
    }

}
