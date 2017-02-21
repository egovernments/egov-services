package org.egov.workflow.domain.service;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.DepartmentRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private PropertiesManager propertiesManager;

    /*
     * @Override public Department getDepartmentForUser(Long userId, Date asOnDate) { String url =
     * propertiesManager.getDepartmentByUserUrl(); return getDepartmentForUser(url, userId, asOnDate); } private Department
     * getDepartmentForUser(final String url, Long userId, Date asOnDate) { RestTemplate restTemplate = new RestTemplate(); return
     * restTemplate.postForObject(url, null, DepartmentRes.class).getDepartment(); }
     */

    @Override
    public Department getDepartmentForUser() {
        String url = propertiesManager.getDepartmentByUserUrl();
        return getDepartmentForUser(url);
    }

    private Department getDepartmentForUser(final String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, null, DepartmentRes.class).getDepartment();
    }

}
