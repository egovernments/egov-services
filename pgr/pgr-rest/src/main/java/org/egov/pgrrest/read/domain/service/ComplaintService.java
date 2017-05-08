package org.egov.pgrrest.read.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.pgrrest.common.repository.ComplaintJpaRepository;
import org.egov.pgrrest.read.domain.model.Complaint;
import org.egov.pgrrest.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.read.persistence.repository.ComplaintRepository;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.web.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

    private static final String SYSTEM_USER = "SYSTEM";
    private ComplaintRepository complaintRepository;
	private ComplaintJpaRepository complaintJpaRepository;
	private UserRepository userRepository;
	private SevaNumberGeneratorService sevaNumberGeneratorService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository,
            SevaNumberGeneratorService sevaNumberGeneratorService, UserRepository userRepository,
            ComplaintJpaRepository complaintJpaRepository) {
        this.complaintRepository = complaintRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
        this.userRepository = userRepository;
        this.complaintJpaRepository = complaintJpaRepository;
    }

    public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
        return complaintRepository.findAll(complaintSearchCriteria);
    }

	public void save(Complaint complaint, SevaRequest sevaRequest) {
		complaint.validate();
		final String crn = sevaNumberGeneratorService.generate();
		complaint.setCrn(crn);
		sevaRequest.update(complaint);
		setUserIdForAnonymousUser(sevaRequest);
		complaintRepository.save(sevaRequest);
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
        return userRepository.getUserByUserName("anonymous",tenantId); 
    }

    public void update(Complaint complaint, SevaRequest sevaRequest) {
		complaint.validate();
		sevaRequest.update(complaint);
		setUserIdForAnonymousUser(sevaRequest);
		complaintRepository.update(sevaRequest);
	}

    public void updateLastAccessedTime(String crn, String tenantId) {
        complaintJpaRepository.updateLastAccessedTime(new Date(), crn, tenantId);
    }

    public List<Complaint> getAllModifiedCitizenComplaints(Long userId, String tenantId) {
        if (userId != null) {
            return complaintRepository.getAllModifiedComplaintsForCitizen(userId, tenantId);
        }
        return new ArrayList<>();
    }

}
