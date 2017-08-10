package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.dto.ServiceDefinition;
import org.egov.pgr.persistence.querybuilder.ServiceDefinitionQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ServiceDefinitionRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceDefinitionQueryBuilder serviceDefinitionQueryBuilder;

    public ServiceDefinitionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                       ServiceDefinitionQueryBuilder serviceDefinitionQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceDefinitionQueryBuilder = serviceDefinitionQueryBuilder;
    }

    public void save(ServiceDefinition serviceDefinition){
        namedParameterJdbcTemplate.update(serviceDefinitionQueryBuilder.getInsertQuery(),
                getInsertMap(serviceDefinition));
    }


    private HashMap getInsertMap(ServiceDefinition serviceDefinition){
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("code", serviceDefinition.getCode());
        parametersMap.put("tenantid", serviceDefinition.getTenantId());
        parametersMap.put("createddate", serviceDefinition.getCreatedDate());
        parametersMap.put("createdby", serviceDefinition.getCreatedBy());

        return parametersMap;
    }

}
