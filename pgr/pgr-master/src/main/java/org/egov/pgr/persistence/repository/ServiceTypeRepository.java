package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.persistence.dto.ServiceType;
import org.egov.pgr.persistence.querybuilder.ServiceTypeQueryBuilder;
import org.egov.pgr.persistence.rowmapper.ServiceTypeRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ServiceTypeRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceTypeQueryBuilder serviceTypeQueryBuilder;

    private ServiceTypeRowMapper serviceTypeRowMapper;

    public ServiceTypeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 ServiceTypeQueryBuilder serviceTypeQueryBuilder,
                                 ServiceTypeRowMapper serviceTypeRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceTypeQueryBuilder = serviceTypeQueryBuilder;
        this.serviceTypeRowMapper = serviceTypeRowMapper;
    }


    public void save(ServiceType serviceType){
        namedParameterJdbcTemplate.update(serviceTypeQueryBuilder.getInsertQuery(),
                getNamedQuery(serviceType));
    }

    public List<org.egov.pgr.domain.model.ServiceType> search(ServiceTypeSearchCriteria serviceTypeSearchCriteria){

        List<ServiceType> serviceTypeList =  namedParameterJdbcTemplate.query(serviceTypeQueryBuilder.buildSearchQuery(serviceTypeSearchCriteria),
                getSearchNamedQuery(serviceTypeSearchCriteria), new BeanPropertyRowMapper(ServiceType.class));

        return serviceTypeList.stream()
                .map(ServiceType::toDomain)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getNamedQuery(ServiceType serviceType){
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("code", serviceType.getCode());
        parametersMap.put("name", serviceType.getName());
        parametersMap.put("category", serviceType.getCategory());
        parametersMap.put("description", serviceType.getDescription());
        parametersMap.put("isactive", serviceType.getIsactive());
        parametersMap.put("isday", serviceType.getIsday());
        parametersMap.put("metadata", serviceType.getMetadata());
        parametersMap.put("slahours", serviceType.getSlahours());
        parametersMap.put("type", serviceType.getType());
        parametersMap.put("tenantid", serviceType.getTenantid());
        parametersMap.put("hasfinancialimpact", serviceType.isHasfinancialimpact());
        parametersMap.put("createdby", serviceType.getCreatedBy());
        parametersMap.put("createddate", serviceType.getCreatedDate());
        parametersMap.put("lastmodifiedby", serviceType.getLastModifiedBy());
        parametersMap.put("lastmodifieddate", serviceType.getLastModifiedDate());
        parametersMap.put("department", serviceType.getDepartment());

        return parametersMap;
    }

    private Map<String, Object> getSearchNamedQuery(ServiceTypeSearchCriteria serviceTypeSearchCriteria){
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("code", serviceTypeSearchCriteria.getServiceCode());
        parametersMap.put("tenantid", serviceTypeSearchCriteria.getTenantId());

        return parametersMap;
    }
}