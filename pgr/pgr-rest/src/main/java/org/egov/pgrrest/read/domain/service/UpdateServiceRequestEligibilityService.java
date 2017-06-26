package org.egov.pgrrest.read.domain.service;

import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.exception.UpdateServiceRequestNotAllowedException;
import org.egov.pgrrest.read.persistence.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceRequestEligibilityService {

    private SubmissionRepository submissionRepository;
    private EmployeeRepository employeeRepository;

    public UpdateServiceRequestEligibilityService(SubmissionRepository submissionRepository,
                                                  EmployeeRepository employeeRepository) {
        this.submissionRepository = submissionRepository;
        this.employeeRepository = employeeRepository;
    }

    public void validate(String serviceRequestId, String tenantId, AuthenticatedUser authenticatedUser) {
        validateAnonymousUserNotAllowedToUpdate(authenticatedUser);
        validateEmployeeUpdateEligibility(serviceRequestId, tenantId, authenticatedUser);
    }

    private void validateEmployeeUpdateEligibility(String serviceRequestId, String tenantId,
                                                   AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.isEmployee()) {
            return;
        }
        Long assignmentIdDB = getAssignmentId(serviceRequestId, tenantId);
        Employee employee = getEmployeeByAssignee(authenticatedUser.getId(), tenantId);
        if (assignmentIdDB.equals(employee.getPrimaryPosition())) {
            return;
        }
        authenticatedUser.validateUpdateEligibility();
    }

    private void validateAnonymousUserNotAllowedToUpdate(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser.isAnonymousUser()) {
            throw new UpdateServiceRequestNotAllowedException();
        }
    }

    private Long getAssignmentId(String serviceRequestId, String tenantId) {
        return submissionRepository.getAssignmentByCrnAndTenantId(serviceRequestId, tenantId);

    }

    private Employee getEmployeeByAssignee(Long userId, String tenantId) {
        return employeeRepository.getEmployeeById(userId, tenantId);
    }
}
