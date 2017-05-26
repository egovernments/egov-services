package org.egov.workflow.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.workflow.web.contract.DepartmentRes;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class DepartmentRestRepository {

    private final RestTemplate restTemplate;

    private final String departmentByIdUrl;

    @Autowired
    public DepartmentRestRepository(final RestTemplate restTemplate,
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
