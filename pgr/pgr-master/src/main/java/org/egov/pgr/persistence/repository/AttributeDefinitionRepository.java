package org.egov.pgr.persistence.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.persistence.dto.AttributeDefinition;
import org.egov.pgr.persistence.dto.ValueDefinition;
import org.egov.pgr.persistence.querybuilder.AttributeDefinitionQueryBuilder;
import org.egov.pgr.persistence.querybuilder.ValueDefinitionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeDefinitionRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private AttributeDefinitionQueryBuilder attributeDefinitionQueryBuilder;

    private ValueDefinitionRepository valueDefinitionRepository;

    private ValueDefinitionQueryBuilder valueDefinitionQueryBuilder;

    public AttributeDefinitionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                         AttributeDefinitionQueryBuilder attributeDefinitionQueryBuilder,
                                         ValueDefinitionRepository valueDefinitionRepository,
                                         ValueDefinitionQueryBuilder valueDefinitionQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.attributeDefinitionQueryBuilder = attributeDefinitionQueryBuilder;
        this.valueDefinitionRepository = valueDefinitionRepository;
        this.valueDefinitionQueryBuilder = valueDefinitionQueryBuilder;
    }

    public void save(AttributeDefinition attributeDefinition){
        namedParameterJdbcTemplate.update(attributeDefinitionQueryBuilder.getInsertQuery(),
                getInsertParamMap(attributeDefinition));
    }

    public List<org.egov.pgr.domain.model.AttributeDefinition> searchByCodeAndTenant(String code, String tenantId){
        List<AttributeDefinition> attributeDefinitions =  namedParameterJdbcTemplate.query(attributeDefinitionQueryBuilder.findByServiceCodeAndTenantId(),
                                                searchByCodeAndTenantIdMap(code, tenantId),
                                                new BeanPropertyRowMapper<>(AttributeDefinition.class));

        attributeDefinitions.forEach(this::setValueDefinitions);

        return attributeDefinitions.stream()
                .map(AttributeDefinition::toDomain)
                .collect(Collectors.toList());
    }

    private void setValueDefinitions(AttributeDefinition attributeDefinition){

        List<ValueDefinition> valueDefinitions = valueDefinitionRepository.fetchValueDefinition(
                            attributeDefinition.getServiceCode(), attributeDefinition.getTenantId(),
                            attributeDefinition.getCode());

        attributeDefinition.setValueDefinitions(valueDefinitions);
    }
    
    public List<org.egov.pgr.domain.model.AttributeDefinition> getServicecodeCodeTenantData(org.egov.pgr.domain.model.AttributeDefinition attributeDefinition) {
        org.egov.pgr.domain.model.AttributeDefinition attributeCodeuniqueCheck = org.egov.pgr.domain.model.AttributeDefinition
        		.builder()
        		.serviceCode(attributeDefinition.getServiceCode())
                .tenantId(attributeDefinition.getTenantId())
                .code(attributeDefinition.getCode())
                .build();
        List<AttributeDefinition> attributeDefinitionList = getAttributeList(attributeDefinitionQueryBuilder.getCodeTenantQuery(attributeCodeuniqueCheck),
                getDetailQuery(attributeCodeuniqueCheck), new BeanPropertyRowMapper<>(AttributeDefinition.class));
        System.out.println(attributeDefinitionQueryBuilder.getCodeTenantQuery(attributeCodeuniqueCheck));
        
        if (!attributeDefinitionList.isEmpty()) {
            return attributeDefinitionList.stream()
                    .map(AttributeDefinition::toDomain)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }
    
    private List<AttributeDefinition> getAttributeList(String sql, HashMap<String, Object> searchNamedQuery, BeanPropertyRowMapper<AttributeDefinition> rowMapper) {
        return namedParameterJdbcTemplate.query(sql,searchNamedQuery, rowMapper);
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
        parametersMap.put("active", attributeDefinition.getActive());
        parametersMap.put("createddate", attributeDefinition.getCreatedDate());
        parametersMap.put("createdby", attributeDefinition.getCreatedBy());
        parametersMap.put("lastmodifiedby", attributeDefinition.getLastModifiedBy());
        parametersMap.put("lastmodifieddate", attributeDefinition.getLastModifiedDate());

        return parametersMap;
    }
    
    private HashMap<String, Object> getDetailQuery(org.egov.pgr.domain.model.AttributeDefinition attributeDefinition) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("code", attributeDefinition.getCode().toUpperCase());
        parametersMap.put("tenantid", attributeDefinition.getTenantId());
        parametersMap.put("servicecode", attributeDefinition.getServiceCode().toUpperCase());
        return parametersMap;
    }

    private HashMap searchByCodeAndTenantIdMap(String code, String tenantId){
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("servicecode", code);
        parametersMap.put("tenantid", tenantId);

        return parametersMap;
    }
}
