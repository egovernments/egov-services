package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.AssigneeFilterInfo;
import org.egov.pgr.employee.enrichment.model.AssigneeRequestInfo;
import org.egov.pgr.employee.enrichment.transform.EmployeePopulateServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeePopulateServiceImpl implements EmployeePopulateService {

    @Value("${egov.services.employee_populate_service.host}")
    private String employeePopulateServiceHost;

    @Override
    public Long fetchEmployeeAssignmentByLocation(Long location, String complaintTypeCode) {
        String url = employeePopulateServiceHost
                + "workflow/getAssignee";
        AssigneeRequestInfo request = new AssigneeRequestInfo();
        AssigneeFilterInfo info = new AssigneeFilterInfo(); 
        info.setBoundaryId(location);
        info.setComplaintTypeCode(complaintTypeCode);
        request.setAssigneeFilterInfo(info);
        return getEmployeePopulateServiceResponse(url, request).getId();
    }

    private EmployeePopulateServiceResponse getEmployeePopulateServiceResponse(final String url, AssigneeRequestInfo request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, request, EmployeePopulateServiceResponse.class);
    }

}
