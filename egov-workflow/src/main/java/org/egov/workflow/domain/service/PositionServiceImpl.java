package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.DepartmentRes;
import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.model.PositionServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Override
    public PositionResponse getById(Long id) {
        String url = propertiesManager.getPositionsByIdUrl() + id;
        return getPositionServiceResponseById(url, id).getPosition().get(0);
    }

    @Override
    public List<PositionResponse> getByEmployeeCode(String code) {
        String url = propertiesManager.getPositionByEmployeeCodeUrl();
        return getPositionServiceResponseByCode(url, code).getPosition();
    }

    @Override
    public Department getDepartmentByPosition(Long id) {
        String url = propertiesManager.getDepartmentByPositionUrl();
        return getDepartmentByPosition(url, id).getDepartment();
    }

    private PositionServiceResponse getPositionServiceResponseById(final String url, Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, PositionServiceResponse.class, id);
    }

    private PositionServiceResponse getPositionServiceResponseByCode(final String url, String code) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, PositionServiceResponse.class, code);
    }

    private DepartmentRes getDepartmentByPosition(final String url, Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, null, DepartmentRes.class);
    }

}
