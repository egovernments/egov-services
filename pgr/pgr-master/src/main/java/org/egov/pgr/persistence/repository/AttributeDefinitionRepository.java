package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.dto.AttributeDefinition;
import org.egov.pgr.persistence.querybuilder.AttributeDefinitionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class AttributeDefinitionRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private AttributeDefinitionQueryBuilder attributeDefinitionQueryBuilder;

    public AttributeDefinitionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                         AttributeDefinitionQueryBuilder attributeDefinitionQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.attributeDefinitionQueryBuilder = attributeDefinitionQueryBuilder;
    }

    public void save(AttributeDefinition attributeDefinition){
        namedParameterJdbcTemplate.update(attributeDefinitionQueryBuilder.getInsertQuery(),
                getInsertParamMap(attributeDefinition));
    }


    private HashMap getInsertParamMap(AttributeDefinition attributeDefinition){
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("servicecode", attributeDefinition.getServiceCode());
        parametersMap.put("tenantid", attributeDefinition.getTenantId());
        parametersMap.put("code", attributeDefinition.getCode());
        parametersMap.put("variable", attributeDefinition.getVariable());
        parametersMap.put("datatype", attributeDefinition.getDataType());
        parametersMap.put("required", attributeDefinition.getRequired());
        parametersMap.put("datatypedescription", attributeDefinition.getDataTypeDescription());
        parametersMap.put("ordernum", attributeDefinition.getOrdernum());
        parametersMap.put("description", attributeDefinition.getDescription());
        parametersMap.put("createddate", attributeDefinition.getCreatedDate());
        parametersMap.put("createdby", attributeDefinition.getCreatedBy());
        parametersMap.put("lastmodifiedby", attributeDefinition.getLastModifiedBy());
        parametersMap.put("lastmodifieddate", attributeDefinition.getLastModifiedDate());

        return parametersMap;
    }
}
