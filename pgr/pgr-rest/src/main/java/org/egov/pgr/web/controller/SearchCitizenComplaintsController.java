package org.egov.pgr.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.service.ComplaintService;
import org.egov.pgr.persistence.queue.contract.ServiceRequest;
import org.egov.pgr.persistence.repository.UserRepository;
import org.egov.pgr.web.contract.GetUserByIdResponse;
import org.egov.pgr.web.contract.ResponseInfo;
import org.egov.pgr.web.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/searchcitizencomplaints" })
public class SearchCitizenComplaintsController {

	private UserRepository userRepository;
	private ComplaintService complaintService;

	@Autowired
	public SearchCitizenComplaintsController(UserRepository userRepository, ComplaintService complaintService) {
		this.userRepository = userRepository;
		this.complaintService = complaintService;
	}

	@PostMapping
	@ResponseBody
	public ServiceResponse getComplaints(@RequestParam(value = "userId", required = true) Long userId) {
		List<Complaint> complaints = new ArrayList<>();
		GetUserByIdResponse user = userRepository.findUserById(userId);
		if (!user.getUser().isEmpty()) {
			if (user.getUser().get(0).getType().equalsIgnoreCase("CITIZEN")) {
				complaints = complaintService.getAllModifiedCitizenComplaints(userId);
			}
		}
		return createResponse(complaints);
	}

	private ServiceResponse createResponse(List<Complaint> complaints) {
		final List<ServiceRequest> serviceRequests = complaints.stream().map(ServiceRequest::new)
				.collect(Collectors.toList());
		return new ServiceResponse(new ResponseInfo("", "", "", "", "", "Successful response"),
				serviceRequests);
	}
}
