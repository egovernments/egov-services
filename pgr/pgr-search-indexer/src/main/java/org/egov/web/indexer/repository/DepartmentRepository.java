package org.egov.web.indexer.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.contract.DepartmentRes;
import org.egov.web.indexer.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class DepartmentRepository {

    private final RestTemplate restTemplate;

    private final String departmentByIdUrl;

    @Autowired
    public DepartmentRepository(final RestTemplate restTemplate,
                                @Value("${egov.services.commonmasters.host}") final String departmentServiceHostname,
                                @Value("${egov.services.common_masters.department}") final String departmentByIdUrl) {

        this.restTemplate = restTemplate;
        this.departmentByIdUrl = departmentServiceHostname + departmentByIdUrl;
    }

    public DepartmentRes getDepartmentById(final Long departmentId, final String tenantId) {
        final RequestInfo requestInfo = RequestInfo.builder()
            .ts(new Date())
            .build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(requestInfo).build();
        return restTemplate.postForObject(departmentByIdUrl, wrapper, DepartmentRes.class, departmentId, tenantId);
    }
}
