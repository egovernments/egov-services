package org.egov.pgr.persistence.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgr.persistence.dto.ServiceDefinition;
import org.egov.pgr.persistence.querybuilder.ServiceDefinitionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceDefinitionRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private ServiceDefinitionQueryBuilder serviceDefinitionQueryBuilder;

	public ServiceDefinitionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			ServiceDefinitionQueryBuilder serviceDefinitionQueryBuilder) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.serviceDefinitionQueryBuilder = serviceDefinitionQueryBuilder;
	}

	public void save(ServiceDefinition serviceDefinition) {
		namedParameterJdbcTemplate.update(serviceDefinitionQueryBuilder.getInsertQuery(),
				getInsertMap(serviceDefinition));
	}

	public void update(ServiceDefinition serviceDefinition){
		namedParameterJdbcTemplate.update(serviceDefinitionQueryBuilder.updateQuery(),
                getUpdateMap(serviceDefinition));
	}

	public List<org.egov.pgr.domain.model.ServiceDefinition> search(ServiceDefinitionSearchCriteria searchCriteria) {

		List<ServiceDefinition> serviceDefinitions = namedParameterJdbcTemplate.query(
				serviceDefinitionQueryBuilder.getSearchQuery(),
				getSearchMap(searchCriteria.getServiceCode(), searchCriteria.getTenantId()),
				new BeanPropertyRowMapper<>(ServiceDefinition.class));

		return serviceDefinitions.stream().map(ServiceDefinition::toDomain).collect(Collectors.toList());
	}

	private HashMap getInsertMap(ServiceDefinition serviceDefinition) {
		HashMap<String, Object> parametersMap = new HashMap<>();

		parametersMap.put("code", serviceDefinition.getCode());
		parametersMap.put("tenantid", serviceDefinition.getTenantId());
		parametersMap.put("createddate", serviceDefinition.getCreatedDate());
		parametersMap.put("createdby", serviceDefinition.getCreatedBy());

		return parametersMap;
	}

	private HashMap getUpdateMap(ServiceDefinition serviceDefinition) {
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("code", serviceDefinition.getCode());
        parametersMap.put("tenantid", serviceDefinition.getTenantId());
        parametersMap.put("lastModifiedBy", serviceDefinition.getLastModifiedBy());
        parametersMap.put("lastModifiedDate", serviceDefinition.getLastModifiedDate());

        return parametersMap;
    }

	public List<org.egov.pgr.domain.model.ServiceDefinition> getData(
			org.egov.pgr.domain.model.ServiceDefinition serviceDefinition) {

		List<ServiceDefinition> serviceTypeList = getServiceList(
				serviceDefinitionQueryBuilder.buildSearchQuery(serviceDefinition),
				getDetailNamedQuery(serviceDefinition), new BeanPropertyRowMapper<>(ServiceDefinition.class));

		System.out.println(serviceDefinitionQueryBuilder.buildSearchQuery(serviceDefinition));
		if (!serviceTypeList.isEmpty()) {
			return serviceTypeList.stream().map(ServiceDefinition::toDomain).collect(Collectors.toList());
		}

		return Collections.EMPTY_LIST;
	}

	public List<org.egov.pgr.domain.model.ServiceDefinition> getDefinitionCode(
			org.egov.pgr.domain.model.ServiceDefinition serviceDefinition) {

		List<ServiceDefinition> definitionList = getServiceList(
				serviceDefinitionQueryBuilder.getSubmissionData(serviceDefinition),
				getDetailNamedQuery(serviceDefinition), new BeanPropertyRowMapper<>(ServiceDefinition.class));

		System.out.println(serviceDefinitionQueryBuilder.buildSearchQuery(serviceDefinition));
		if (!definitionList.isEmpty()) {
			return definitionList.stream().map(ServiceDefinition::toDomain).collect(Collectors.toList());
		}

		return Collections.EMPTY_LIST;
	}

	private HashMap<String, String> getDetailNamedQuery(org.egov.pgr.domain.model.ServiceDefinition serviceDefinition) {
		HashMap<String, String> parametersMap = new HashMap<String, String>();

		parametersMap.put("code", serviceDefinition.getCode().toUpperCase());
		parametersMap.put("tenantid", serviceDefinition.getTenantId());

		return parametersMap;
	}

	private List<ServiceDefinition> getServiceList(String sql, HashMap<String, String> searchNamedQuery,
			BeanPropertyRowMapper<ServiceDefinition> rowMapper) {
		return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
	}

	private HashMap getSearchMap(String serviceCode, String tenantId) {
		HashMap<String, Object> parametersMap = new HashMap<>();

		parametersMap.put("serviceCode", serviceCode);
		parametersMap.put("tenantid", tenantId);

		return parametersMap;
	}
}