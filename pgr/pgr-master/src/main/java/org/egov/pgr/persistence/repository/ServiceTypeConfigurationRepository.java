package org.egov.pgr.persistence.repository;


import org.egov.pgr.persistence.dto.ServiceTypeConfiguration;
import org.egov.pgr.persistence.querybuilder.ServiceTypeConfigurationQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ServiceTypeConfigurationRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceTypeConfigurationQueryBuilder serviceTypeConfigurationQueryBuilder;

    public ServiceTypeConfigurationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                              ServiceTypeConfigurationQueryBuilder serviceTypeConfigurationQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceTypeConfigurationQueryBuilder = serviceTypeConfigurationQueryBuilder;
    }

    public void save(ServiceTypeConfiguration serviceTypeConfiguration){

        namedParameterJdbcTemplate.update(serviceTypeConfigurationQueryBuilder.getInsertQuery(),
                getNamedQuery(serviceTypeConfiguration));
    }

    private Map<String, Object> getNamedQuery(ServiceTypeConfiguration serviceTypeConfiguration){
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("tenantid", serviceTypeConfiguration.getTenantId());
        parametersMap.put("servicecode", serviceTypeConfiguration.getServiceCode());
        parametersMap.put("isapplicationfeesenabled", serviceTypeConfiguration.isApplicationFeesEnabled());
        parametersMap.put("isnotificationenabled", serviceTypeConfiguration.isNotificationEnabled());
        parametersMap.put("isslaenabled", serviceTypeConfiguration.isSlaEnabled());

        return parametersMap;
    }
}