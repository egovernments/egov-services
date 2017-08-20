package org.egov.pgr.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.persistence.dto.ServiceTypeConfiguration;
import org.egov.pgr.persistence.querybuilder.ServiceTypeConfigurationQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTypeConfigurationRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private ServiceTypeConfigurationQueryBuilder serviceTypeConfigurationQueryBuilder;

	public ServiceTypeConfigurationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			ServiceTypeConfigurationQueryBuilder serviceTypeConfigurationQueryBuilder) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.serviceTypeConfigurationQueryBuilder = serviceTypeConfigurationQueryBuilder;
	}

	public void save(ServiceTypeConfiguration serviceTypeConfiguration) {

		namedParameterJdbcTemplate.update(serviceTypeConfigurationQueryBuilder.getInsertQuery(),
				getNamedQuery(serviceTypeConfiguration));
	}

	public void update(ServiceTypeConfiguration serviceTypeConfiguration) {

		namedParameterJdbcTemplate.update(serviceTypeConfigurationQueryBuilder.getUpdateQuery(),
				getNamedQuery(serviceTypeConfiguration));
	}

	public List<org.egov.pgr.domain.model.ServiceTypeConfiguration> search(
			ServiceTypeConfigurationSearchCriteria serviceTypeSearchCriteria) {

		List<ServiceTypeConfiguration> serviceTypeList = namedParameterJdbcTemplate.query(
				serviceTypeConfigurationQueryBuilder.buildSearchQuery(serviceTypeSearchCriteria),
				getSearchNamedQuery(serviceTypeSearchCriteria),
				new BeanPropertyRowMapper(ServiceTypeConfiguration.class));

		return serviceTypeList.stream().map(ServiceTypeConfiguration::toDomain).collect(Collectors.toList());
	}

	private Map<String, Object> getSearchNamedQuery(ServiceTypeConfigurationSearchCriteria serviceTypeSearchCriteria) {
		Map<String, Object> parametersMap = new HashMap();
		parametersMap.put("serviceCode", serviceTypeSearchCriteria.getServiceCode());
		parametersMap.put("tenantid", serviceTypeSearchCriteria.getTenantId());

		return parametersMap;
	}

	private Map<String, Object> getNamedQuery(ServiceTypeConfiguration serviceTypeConfiguration) {
		Map<String, Object> parametersMap = new HashMap();
		parametersMap.put("tenantid", serviceTypeConfiguration.getTenantId());
		parametersMap.put("servicecode", serviceTypeConfiguration.getServiceCode());
		parametersMap.put("applicationfeesenabled", serviceTypeConfiguration.isApplicationFeesEnabled());
		parametersMap.put("notificationenabled", serviceTypeConfiguration.isNotificationEnabled());
		parametersMap.put("slaenabled", serviceTypeConfiguration.isSlaEnabled());
		parametersMap.put("online", serviceTypeConfiguration.isOnline());
		parametersMap.put("source", serviceTypeConfiguration.getSource());
		parametersMap.put("url", serviceTypeConfiguration.getUrl());
		parametersMap.put("glCode", serviceTypeConfiguration.getGlCode());

		return parametersMap;
	}
}