package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceRequestRepository;
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
    private List<ServiceRequestValidator> validators;

    @Autowired
    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                 SevaNumberGeneratorService sevaNumberGeneratorService,
                                 UserRepository userRepository,
                                 OtpService otpService,
                                 ServiceRequestTypeService serviceRequestTypeService,
                                 List<ServiceRequestValidator> validators) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.serviceRequestTypeService = serviceRequestTypeService;
        this.validators = validators;
    }

    public List<ServiceRequest> findAll(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.find(serviceRequestSearchCriteria);
        maskCitizenDetailsForAnonymousRequest(serviceRequestSearchCriteria, serviceRequestList);
        return serviceRequestList;
    }

    public Long getCount(ServiceRequestSearchCriteria searchCriteria) {
        return serviceRequestRepository.getCount(searchCriteria);
    }

    public void save(ServiceRequest serviceRequest, SevaRequest contractSevaRequest) {
        validate(serviceRequest);
        enrichWithServiceType(serviceRequest);
        otpService.validateOtp(serviceRequest);
        enrichWithCRN(serviceRequest);
        contractSevaRequest.update(serviceRequest);
        setUserIdForAnonymousUser(contractSevaRequest);
        serviceRequestRepository.save(contractSevaRequest);
    }

    public void update(ServiceRequest serviceRequest, SevaRequest contractSevaRequest) {
        validate(serviceRequest);
        setUserIdForAnonymousUser(contractSevaRequest);
        serviceRequestRepository.update(contractSevaRequest);
    }

    private void maskCitizenDetailsForAnonymousRequest(ServiceRequestSearchCriteria searchCriteria,
                                                       List<ServiceRequest> serviceRequestList) {
        if (searchCriteria.isAnonymous())
            serviceRequestList.forEach(ServiceRequest::maskUserDetails);
    }

    private void validate(ServiceRequest serviceRequest) {
        validators.stream()
            .filter(v -> v.canValidate(serviceRequest))
            .forEach(validator -> validator.validate(serviceRequest));
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

}
