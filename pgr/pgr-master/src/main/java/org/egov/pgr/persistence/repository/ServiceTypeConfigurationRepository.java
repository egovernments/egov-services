package org.egov.pgr.persistence.repository;


import java.util.HashMap;
import java.util.Map;

import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.persistence.dto.ServiceTypeConfiguration;
import org.egov.pgr.persistence.querybuilder.ServiceTypeConfigurationQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTypeConfigurationRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    
    public void update(ServiceTypeConfiguration serviceTypeConfiguration){

        namedParameterJdbcTemplate.update(serviceTypeConfigurationQueryBuilder.getUpdateQuery(),
                getNamedQuery(serviceTypeConfiguration));
    }
    
    
    private Map<String, Object> getNamedQueryforSearch(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria){
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("tenantid", serviceTypeConfigurationSearchCriteria.getTenantId());
        parametersMap.put("servicecode", serviceTypeConfigurationSearchCriteria.getServiceCode());

        return parametersMap;
    }
    
    private Map<String, Object> getNamedQuery(ServiceTypeConfiguration serviceTypeConfiguration){
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