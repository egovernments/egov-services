package org.egov.pgrrest.read.domain.service;

import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.exception.UpdateComplaintNotAllowedException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceRequestRepository;
import org.egov.pgrrest.read.persistence.repository.SubmissionRepository;
import org.egov.pgrrest.read.web.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    private static final String SYSTEM_USER = "SYSTEM";
    private static final String ANONYMOUS_USER_NAME = "anonymous";
    private ServiceRequestRepository serviceRequestRepository;
    private UserRepository userRepository;
    private SevaNumberGeneratorService sevaNumberGeneratorService;
    private OtpService otpService;
    private ServiceRequestTypeService serviceRequestTypeService;
    private SubmissionRepository submissionRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                 SevaNumberGeneratorService sevaNumberGeneratorService,
                                 UserRepository userRepository,
                                 OtpService otpService,
                                 ServiceRequestTypeService serviceRequestTypeService,
                                 SubmissionRepository submissionRepository,
                                 EmployeeRepository employeeRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.serviceRequestTypeService = serviceRequestTypeService;
        this.submissionRepository = submissionRepository;
        this.employeeRepository = employeeRepository;
    }


    public List<ServiceRequest> findAll(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.find(serviceRequestSearchCriteria);
        maskCitizenDetailsForAnonymousRequest(serviceRequestSearchCriteria, serviceRequestList);
        return serviceRequestList;
    }

    private void maskCitizenDetailsForAnonymousRequest(ServiceRequestSearchCriteria serviceRequestSearchCriteria,
                                                       List<ServiceRequest> serviceRequestList) {
        if (serviceRequestSearchCriteria.isAnonymous())
            serviceRequestList.forEach(ServiceRequest::maskUserDetails);
    }

    public Long getCount(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        return serviceRequestRepository.getCount(serviceRequestSearchCriteria);
    }

    public void save(ServiceRequest serviceRequest, SevaRequest sevaRequest) {
        serviceRequest.validate();
        enrichWithServiceType(serviceRequest);
        otpService.validateOtp(serviceRequest);
        enrichWithCRN(serviceRequest);
        sevaRequest.update(serviceRequest);
        setUserIdForAnonymousUser(sevaRequest);
        serviceRequestRepository.save(sevaRequest);
    }

    private void enrichWithCRN(ServiceRequest serviceRequest) {
        final String crn = sevaNumberGeneratorService.generate();
        serviceRequest.setCrn(crn);
    }

    private void enrichWithServiceType(ServiceRequest serviceRequest) {
        serviceRequestTypeService.enrich(serviceRequest);
    }

    private void setUserIdForAnonymousUser(SevaRequest sevaRequest) {
        if (sevaRequest.getUserId() != null) {
            return;
        }

        final User anonymousUser = getAnonymousUser(sevaRequest.getServiceRequest().getTenantId());
        sevaRequest.getRequestInfo()
            .setUserInfo(org.egov.common.contract.request.User.builder()
                .id(anonymousUser.getId())
                .type(SYSTEM_USER)
                .build());
    }

    private User getAnonymousUser(String tenantId) {
        return userRepository.getUserByUserName(ANONYMOUS_USER_NAME, tenantId);
    }

    public void update(ServiceRequest complaint, SevaRequest sevaRequest) {
        complaint.validate();
        validateUpdateEligibility(complaint);
        sevaRequest.update(complaint);
        setUserIdForAnonymousUser(sevaRequest);
        serviceRequestRepository.update(sevaRequest);
    }

    public Long getAssignmentId(String crn, String tenantId) {
        return submissionRepository.getAssignmentByCrnAndTenantId(crn, tenantId);

    }

    private Employee getEmployeeByAssignee(Long assigneeId, String tenantId) {
        return employeeRepository.getEmployeeById(assigneeId, tenantId);
    }

    private void validateUpdateEligibility(ServiceRequest complaint) {
        if (complaint.getAuthenticatedUser().isCitizen()) {
            //TODO: Temporary fix - discuss and fix this
            return;
        }
        Long assignmentIdDB = getAssignmentId(complaint.getCrn(), complaint.getTenantId());
        Employee employee = getEmployeeByAssignee(complaint.getAuthenticatedUser().getId(), complaint.getTenantId());
        if (assignmentIdDB.equals(employee.getPrimaryPosition())) {
            return;
        }
        complaint.getAuthenticatedUser().validateUpdateEligibility();
    }


    public boolean validateUpdateEligibilityUI(String crn, String tenantId, AuthenticatedUser authenticatedUser) {
        Long assignmentIdDB = getAssignmentId(crn, tenantId);
        Employee employee = getEmployeeByAssignee(authenticatedUser.getId(), tenantId);
        if (assignmentIdDB.equals(employee.getPrimaryPosition())) {
            return true;
        }
        try {
            authenticatedUser.validateUpdateEligibility();
            return true;
        } catch (UpdateComplaintNotAllowedException ue) {
            return false;
        }
    }
}
