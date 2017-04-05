package org.egov.pgr.read.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.read.domain.model.Complaint;
import org.egov.pgr.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.common.contract.SevaRequest;
import org.egov.pgr.read.persistence.repository.ComplaintJpaRepository;
import org.egov.pgr.read.persistence.repository.ComplaintRepository;
import org.egov.pgr.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

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
		populateRequesterId(sevaRequest, complaint);
		complaintRepository.save(sevaRequest);
	}

	private void populateRequesterId(SevaRequest sevaRequest, Complaint complaint) {

		if (sevaRequest.getRequestInfo() != null && StringUtils.isNotEmpty(sevaRequest.getRequestInfo().getAuthToken()))
			sevaRequest.getRequestInfo().setRequesterId(complaint.getAuthenticatedUser().getId().toString());
		else if (sevaRequest.getRequestInfo() != null) {
			sevaRequest.getRequestInfo()
					.setRequesterId(userRepository.getUserByUserName("anonymous").getId().toString());
		} else {
			sevaRequest.setRequestInfo(new RequestInfo("", "", new Date(), "", "", "", "",
					userRepository.getUserByUserName("anonymous").getId().toString(), "", "", null));
		}
	}

	public void update(Complaint complaint, SevaRequest sevaRequest) {
		complaint.validate();
		sevaRequest.update(complaint);
		populateRequesterId(sevaRequest, complaint);
		complaintRepository.update(sevaRequest);
	}

	public void updateLastAccessedTime(String crn) {
		complaintJpaRepository.updateLastAccessedTime(new Date(), crn);
	}

	public List<Complaint> getAllModifiedCitizenComplaints(Long userId) {
		if (userId != null) {
			return complaintRepository.getAllModifiedComplaintsForCitizen(userId);
		}
		return new ArrayList<>();
	}

}
