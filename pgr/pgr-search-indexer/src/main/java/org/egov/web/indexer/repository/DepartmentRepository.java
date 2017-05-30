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
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(RequestInfo.builder().apiId("apiID").ver("ver").ts(new Date()).msgId("msgID").build()).build();
       return restTemplate.postForObject(departmentByIdUrl,wrapper,DepartmentRes.class, departmentId, tenantId);
    }
}
