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

    @Autowired
    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                 SevaNumberGeneratorService sevaNumberGeneratorService,
                                 UserRepository userRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
        this.userRepository = userRepository;
    }

    public List<ServiceRequest> findAll(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        return serviceRequestRepository.findAll(serviceRequestSearchCriteria);
    }

    public Long getCount(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        return serviceRequestRepository.getCount(serviceRequestSearchCriteria);
    }

    public void save(ServiceRequest complaint, SevaRequest sevaRequest) {
		complaint.validate();
		final String crn = sevaNumberGeneratorService.generate();
		complaint.setCrn(crn);
		sevaRequest.update(complaint);
		setUserIdForAnonymousUser(sevaRequest);
		serviceRequestRepository.save(sevaRequest);
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
        return userRepository.getUserByUserName(ANONYMOUS_USER_NAME,tenantId);
    }

    public void update(ServiceRequest complaint, SevaRequest sevaRequest) {
		complaint.validate();
		sevaRequest.update(complaint);
		setUserIdForAnonymousUser(sevaRequest);
		serviceRequestRepository.update(sevaRequest);
	}

}
