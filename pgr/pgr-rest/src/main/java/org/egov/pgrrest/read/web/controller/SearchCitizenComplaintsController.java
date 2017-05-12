package org.egov.pgrrest.read.web.controller;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.common.contract.GetUserByIdResponse;
import org.egov.pgrrest.common.contract.ServiceRequest;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.model.Complaint;
import org.egov.pgrrest.read.domain.service.ServiceRequestService;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.egov.pgrrest.read.web.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = { "/searchcitizencomplaints" })
public class SearchCitizenComplaintsController {

    private UserRepository userRepository;
    private ServiceRequestService serviceRequestService;

    @Autowired
    public SearchCitizenComplaintsController(UserRepository userRepository, ServiceRequestService serviceRequestService) {
        this.userRepository = userRepository;
        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    @ResponseBody
    public ServiceResponse getComplaints(@RequestParam(value = "tenantId", required = true) final String tenantId,
            @RequestParam(value = "userId", required = true) Long userId, @RequestBody RequestInfoBody requestInfo) {
        List<Complaint> complaints = new ArrayList<>();
        if (tenantId != null && !tenantId.isEmpty()) {
            GetUserByIdResponse user = userRepository.findUserByIdAndTenantId(userId,tenantId);
            if (!user.getUser().isEmpty() && user.getUser().get(0).getType().equalsIgnoreCase("CITIZEN")) {
                complaints = serviceRequestService.getAllModifiedCitizenComplaints(userId,tenantId);
            }
        }
        return createResponse(complaints);
    }

    private ServiceResponse createResponse(List<Complaint> complaints) {
        final List<ServiceRequest> serviceRequests = complaints.stream().map(ServiceRequest::new)
                .collect(Collectors.toList());
        return new ServiceResponse(new ResponseInfo("", "", "", "", "", "Successful response"), serviceRequests);
    }
}
