package org.egov.pgr.persistence.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.persistence.dto.ValueDefinition;
import org.egov.pgr.persistence.querybuilder.ValueDefinitionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ValueDefinitionRepository {

    private ValueDefinitionQueryBuilder valueDefinitionQueryBuilder;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ValueDefinitionRepository(ValueDefinitionQueryBuilder valueDefinitionQueryBuilder,
                                     NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.valueDefinitionQueryBuilder = valueDefinitionQueryBuilder;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(ValueDefinition valueDefinition){
        namedParameterJdbcTemplate.update(valueDefinitionQueryBuilder.getInsertQuery(),
                getInsertMap(valueDefinition));
    }

    public List<ValueDefinition> fetchValueDefinition(String code, String tenantId, String attributeCode){

      return  namedParameterJdbcTemplate.query(valueDefinitionQueryBuilder.findByTenantIdAndServiceCodeAndAttributeCode(),
                getValueDefinitionMap(code, tenantId, attributeCode),
                new BeanPropertyRowMapper<>(ValueDefinition.class));
    }
    
    public List<org.egov.pgr.domain.model.ValueDefinition> getValueDefinitionTenantData(org.egov.pgr.domain.model.ValueDefinition valueDefinition,
    		String serviceCode,String attributeCode,String tenantid) {
        org.egov.pgr.domain.model.ValueDefinition valueCodeuniqueCheck = org.egov.pgr.domain.model.ValueDefinition
        		.builder()
        		.serviceCode(serviceCode)
                .key(valueDefinition.getKey())
                .attributeCode(attributeCode)
                .tenantid(tenantid)
                .build();
        
        List<ValueDefinition> valueDefinitionList = getValueList(valueDefinitionQueryBuilder.getCodeTenantQuery(valueCodeuniqueCheck),
                getDetailQuery(valueCodeuniqueCheck), new BeanPropertyRowMapper<>(ValueDefinition.class));
        System.out.println(valueDefinitionQueryBuilder.getCodeTenantQuery(valueCodeuniqueCheck));
        
        if (!valueDefinitionList.isEmpty()) {
            return valueDefinitionList.stream()
                    .map(ValueDefinition::toDomain)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }
    
    private List<ValueDefinition> getValueList(String sql, HashMap<String, Object> searchNamedQuery, BeanPropertyRowMapper<ValueDefinition> rowMapper) {
        return namedParameterJdbcTemplate.query(sql,searchNamedQuery, rowMapper);
    }
    
    private HashMap<String, Object> getDetailQuery(org.egov.pgr.domain.model.ValueDefinition valueDefinition) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("servicecode", valueDefinition.getServiceCode().toUpperCase());
        parametersMap.put("tenantid", valueDefinition.getTenantid());
        parametersMap.put("attributecode", valueDefinition.getAttributeCode());
        parametersMap.put("key", valueDefinition.getKey());
        return parametersMap;
    }

    private HashMap getInsertMap(ValueDefinition valueDefinition){
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("servicecode", valueDefinition.getServiceCode());
        parametersMap.put("tenantid", valueDefinition.getTenantId());
        parametersMap.put("active", valueDefinition.getActive());
        parametersMap.put("required", valueDefinition.getRequired());
        parametersMap.put("key", valueDefinition.getKey());
        parametersMap.put("name", valueDefinition.getName());
        parametersMap.put("attributecode", valueDefinition.getAttributeCode());
        parametersMap.put("createddate", valueDefinition.getCreatedDate());
        parametersMap.put("createdby", valueDefinition.getCreatedBy());

        return parametersMap;
    }

    private HashMap<String, Object> getValueDefinitionMap(String code, String tenantId, String attributeCode){
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("servicecode", code);
        parametersMap.put("tenantid", tenantId);
        parametersMap.put("attributecode", attributeCode);

        return parametersMap;
    }
}
