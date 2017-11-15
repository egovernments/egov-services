package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.persistence.dto.ServiceType;
import org.egov.pgr.persistence.dto.ServiceTypeKeyword;
import org.egov.pgr.persistence.querybuilder.ServiceTypeKeywordQueryBuilder;
import org.egov.pgr.persistence.querybuilder.ServiceTypeQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ServiceTypeRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceTypeQueryBuilder serviceTypeQueryBuilder;

    private ServiceTypeKeywordQueryBuilder serviceTypeKeywordQueryBuilder;

    public ServiceTypeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 ServiceTypeQueryBuilder serviceTypeQueryBuilder,
                                 ServiceTypeKeywordQueryBuilder serviceTypeKeywordQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceTypeQueryBuilder = serviceTypeQueryBuilder;
        this.serviceTypeKeywordQueryBuilder = serviceTypeKeywordQueryBuilder;
    }

    public void save(ServiceType serviceType) {
        namedParameterJdbcTemplate.update(serviceTypeQueryBuilder.getInsertQuery(),
                getNamedQuery(serviceType));
    }

    public void update(ServiceType serviceType){
        namedParameterJdbcTemplate.update(serviceTypeQueryBuilder.getUpdateQuery(),
                getNamedQuery(serviceType));
    }

    public List<org.egov.pgr.domain.model.ServiceType> search(ServiceTypeSearchCriteria serviceTypeSearchCriteria) {

        List<ServiceType> serviceTypeList = getServiceList(serviceTypeQueryBuilder.buildSearchQuery(serviceTypeSearchCriteria),
                getSearchNamedQuery(serviceTypeSearchCriteria), new BeanPropertyRowMapper<>(ServiceType.class));
        filterKeywords(serviceTypeSearchCriteria, serviceTypeList);

        return getServiceTypes(serviceTypeSearchCriteria, serviceTypeList);
    }
    
    public List getData(org.egov.pgr.domain.model.ServiceType serviceType) {

        List<ServiceType> serviceTypeList = getServiceList(serviceTypeQueryBuilder.getQuery(serviceType),
                getDetailNamedQuery(serviceType), new BeanPropertyRowMapper<>(ServiceType.class));
        System.out.println(serviceTypeQueryBuilder.getQuery(serviceType));
        if (!serviceTypeList.isEmpty()) {
            return serviceTypeList.stream()
                    .map(ServiceType::toDomain)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }
    
    public List<org.egov.pgr.domain.model.ServiceType> getCodeTenantData(org.egov.pgr.domain.model.ServiceType serviceType) {
        org.egov.pgr.domain.model.ServiceType serviceTypeCodeTenant = org.egov.pgr.domain.model.ServiceType.builder().serviceCode(serviceType.getServiceCode())
                .tenantId(serviceType.getTenantId())
                .build();
        List<ServiceType> serviceTypeList = getServiceList(serviceTypeQueryBuilder.getQuery(serviceTypeCodeTenant),
                getDetailQuery(serviceTypeCodeTenant), new BeanPropertyRowMapper<>(ServiceType.class));
        System.out.println(serviceTypeQueryBuilder.getQuery(serviceType));
        if (!serviceTypeList.isEmpty()) {
            return serviceTypeList.stream()
                    .map(ServiceType::toDomain)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }
    
    public List<org.egov.pgr.domain.model.ServiceType> getCodeTenantDataFromCategory(org.egov.pgr.domain.model.ServiceType serviceType) {
        org.egov.pgr.domain.model.ServiceType service = org.egov.pgr.domain.model.ServiceType.builder()
                .category(serviceType.getCategory())
                .tenantId(serviceType.getTenantId())
                .build();
        List<ServiceType> serviceTypeList = getServiceList(serviceTypeQueryBuilder.getCategoryData(service),
                getcategory(serviceType), new BeanPropertyRowMapper<>(ServiceType.class));
        System.out.println(serviceTypeQueryBuilder.getCategoryData(serviceType));
        if (!serviceTypeList.isEmpty()) {
            return serviceTypeList.stream()
                    .map(ServiceType::toDomain)
                    .collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }

    private List<org.egov.pgr.domain.model.ServiceType> getServiceTypes(ServiceTypeSearchCriteria serviceTypeSearchCriteria, List<ServiceType> serviceTypeList) {
    	if(serviceTypeSearchCriteria.getKeywords()!=null && !serviceTypeSearchCriteria.getKeywords().isEmpty())
	        return serviceTypeList.stream()
	                .filter(serviceType -> serviceType.isKeywordPresent(serviceTypeSearchCriteria.getKeywords()))
	                .map(ServiceType::toDomain)
	                .collect(Collectors.toList());
    	else
    		return serviceTypeList.stream()
	                .map(ServiceType::toDomain)
	                .collect(Collectors.toList());
    }

    private void filterKeywords(ServiceTypeSearchCriteria serviceTypeSearchCriteria, List<ServiceType> serviceTypeList) {
        serviceTypeList.
                forEach(serviceType -> serviceType.setKeywords(
                        fetchKeywords(serviceType, serviceTypeSearchCriteria.getKeywords())));
    }

    private List<ServiceType> getServiceList(String sql, HashMap<String, Object> searchNamedQuery, BeanPropertyRowMapper<ServiceType> rowMapper) {
        return namedParameterJdbcTemplate.query(sql,
                searchNamedQuery, rowMapper);
    }

    private List<String> fetchKeywords(ServiceType serviceType, List<String> keywords) {
        List<ServiceTypeKeyword> keywordList = namedParameterJdbcTemplate.query(serviceTypeKeywordQueryBuilder.keywordsSearchQuery(),
                getKeywordsSearchMap(serviceType, keywords), new BeanPropertyRowMapper<>(ServiceTypeKeyword.class));

        return keywordList.stream()
                .map(ServiceTypeKeyword::getKeyword)
                .collect(Collectors.toList());
    }

    //used for update unique validation
    public ServiceType findByCodeAndTenantId(ServiceType serviceType){
        return namedParameterJdbcTemplate.queryForObject(serviceTypeQueryBuilder.getSearchQueryForUpdateValidation(),
                    getUpdateValidationMap(serviceType), new BeanPropertyRowMapper<>(ServiceType.class));
    }

    private HashMap getUpdateValidationMap(ServiceType serviceType){
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();

        parametersMap.put("code", serviceType.getCode());
        parametersMap.put("tenantid", serviceType.getTenantId());

        return parametersMap;
    }

    private HashMap<String, Object> getNamedQuery(ServiceType serviceType) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("code", serviceType.getCode());
        parametersMap.put("name", serviceType.getName());
        parametersMap.put("category", serviceType.getCategory());
        parametersMap.put("description", serviceType.getDescription());
        parametersMap.put("isactive", serviceType.getIsactive());
        parametersMap.put("isday", serviceType.getIsday());
        parametersMap.put("metadata", serviceType.getMetadata());
        parametersMap.put("slahours", serviceType.getSlaHours());
        parametersMap.put("type", serviceType.getType());
        parametersMap.put("tenantid", serviceType.getTenantId());
        parametersMap.put("hasfinancialimpact", serviceType.isHasfinancialimpact());
        parametersMap.put("createdby", serviceType.getCreatedBy());
        parametersMap.put("createddate", serviceType.getCreatedDate());
        parametersMap.put("lastmodifiedby", serviceType.getLastModifiedBy());
        parametersMap.put("lastmodifieddate", serviceType.getLastModifiedDate());
        parametersMap.put("department", serviceType.getDepartment());
        parametersMap.put("localname", serviceType.getLocalname());

        return parametersMap;
    }

    private HashMap<String, Object> getSearchNamedQuery(ServiceTypeSearchCriteria serviceTypeSearchCriteria) {
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("code", serviceTypeSearchCriteria.getServiceCode());
        parametersMap.put("tenantid", serviceTypeSearchCriteria.getTenantId());
        parametersMap.put("category", serviceTypeSearchCriteria.getCategory());

        return parametersMap;
    }

    private HashMap<String, Object> getDetailNamedQuery(org.egov.pgr.domain.model.ServiceType serviceType) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("code", serviceType.getServiceCode().toUpperCase());
        parametersMap.put("tenantid", serviceType.getTenantId());
        parametersMap.put("name",serviceType.getServiceName().toUpperCase());
        parametersMap.put("category", serviceType.getCategory());
        return parametersMap;
    }

    private HashMap<String, Object> getDetailQuery(org.egov.pgr.domain.model.ServiceType serviceType) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("code", serviceType.getServiceCode().toUpperCase());
        parametersMap.put("tenantid", serviceType.getTenantId());
        return parametersMap;
    }

    private HashMap<String, Object> getcategory(org.egov.pgr.domain.model.ServiceType serviceType) {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("categoryId", serviceType.getCategory());
        parametersMap.put("tenantId", serviceType.getTenantId());
        return parametersMap;
    }

    private HashMap<String, Object> getKeywordsSearchMap(ServiceType serviceType, List<String> keywords) {
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("code", serviceType.getCode());
        parametersMap.put("tenantid", serviceType.getTenantId());
        parametersMap.put("keywords", keywords);

        return parametersMap;
    }
}