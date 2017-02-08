package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.PropertiesManager;
import org.egov.pgr.employee.enrichment.repository.contract.AssigneeFilterInfo;
import org.egov.pgr.employee.enrichment.repository.contract.AssigneeRequestInfo;
import org.egov.pgr.employee.enrichment.repository.contract.AssigneeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssigneeRepositoryImpl implements AssigneeRepository {

    private RestTemplate restTemplate;
    private PropertiesManager propertiesManager;

    @Autowired
    public AssigneeRepositoryImpl(RestTemplate restTemplate, PropertiesManager propertiesManager) {
        this.restTemplate = restTemplate;
        this.propertiesManager = propertiesManager;
    }

    @Override
    public Long assigneeByBoundaryAndComplaintType(Long location, String complaintTypeCode) {
        String url = propertiesManager.getAssigneeUrl();
        AssigneeRequestInfo assigneeRequestInfo = buildAssigneeFilter(location, complaintTypeCode);
        return restTemplate.postForObject(url, assigneeRequestInfo, AssigneeResponse.class).getId();
    }

    private AssigneeRequestInfo buildAssigneeFilter(Long location, String complaintTypeCode) {
        AssigneeFilterInfo info = new AssigneeFilterInfo(location, complaintTypeCode);
        return new AssigneeRequestInfo(null, info);
    }

}
