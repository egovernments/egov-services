package org.egov.pgrrest.read.domain.service;

import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
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

    public void validate(String serviceRequestId, String tenantId, AuthenticatedUser user) {
        validateAnonymousUserNotAllowedToUpdate(user);
        validateCitizenUpdateEligibility(serviceRequestId, tenantId, user);
        validateEmployeeUpdateEligibility(serviceRequestId, tenantId, user);
    }

    private void validateCitizenUpdateEligibility(String serviceRequestId, String tenantId, AuthenticatedUser user) {
        if (!user.isCitizen()) {
            return;
        }
        final Long loggedInRequester = submissionRepository.getLoggedInRequester(serviceRequestId, tenantId);
        if (!user.getId().equals(loggedInRequester)) {
            throw new UpdateServiceRequestNotAllowedException();
        }
    }

    private void validateEmployeeUpdateEligibility(String serviceRequestId, String tenantId,
                                                   AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.isEmployee()) {
            return;
        }
        Long positionIdFromDB = getPositionId(serviceRequestId, tenantId);
        Employee employee = getEmployeeByAssignee(authenticatedUser.getId(), tenantId);
        if (null == employee || positionIdFromDB.equals(employee.getPrimaryPosition())) {
            return;
        }
        authenticatedUser.validateUpdateEligibility();
    }

    private void validateAnonymousUserNotAllowedToUpdate(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser.isAnonymousUser()) {
            throw new UpdateServiceRequestNotAllowedException();
        }
    }

    private Long getPositionId(String serviceRequestId, String tenantId) {
        return submissionRepository.getPosition(serviceRequestId, tenantId);
    }

    private Employee getEmployeeByAssignee(Long userId, String tenantId) {
        return employeeRepository.getEmployeeById(userId, tenantId);
    }
}
