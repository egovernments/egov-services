package org.egov.pgrrest.read.persistence.repository;

import java.util.Set;

import org.egov.pgrrest.common.model.Role;
import org.egov.pgrrest.read.web.contract.EmployeeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeRepository {

    private final RestTemplate restTemplate;
    private final String url;

    public EmployeeRepository(final RestTemplate restTemplate, @Value("${egov.services.eis.host}") final String eisServiceHost,
            @Value("${egov.services.eis.roles_by_user_id}") final String url) {
        this.restTemplate = restTemplate;
        this.url = eisServiceHost + url;
    }

    public Set<Role> getRolesByUserId(final Long userId,final String tenantId) {
        return restTemplate.getForObject(url, EmployeeResponse.class, userId,tenantId).getEmployees().get(0).getRoles();
    }

}
