package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.dto.ServiceTypeKeyword;
import org.egov.pgr.persistence.querybuilder.ServiceTypeKeywordQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ServiceTypeKeywordRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceTypeKeywordQueryBuilder serviceTypeKeywordQueryBuilder;

    public ServiceTypeKeywordRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                        ServiceTypeKeywordQueryBuilder serviceTypeKeywordQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceTypeKeywordQueryBuilder = serviceTypeKeywordQueryBuilder;
    }

    public void save(ServiceTypeKeyword serviceTypeKeyword){
        namedParameterJdbcTemplate.update(serviceTypeKeywordQueryBuilder.getInsertQuery(),
                getMap(serviceTypeKeyword));
    }

    public void update(ServiceTypeKeyword serviceTypeKeyword){
        namedParameterJdbcTemplate.update(serviceTypeKeywordQueryBuilder.getUpdateQuery(),
                getMap(serviceTypeKeyword));
    }


    private HashMap getMap(ServiceTypeKeyword serviceTypeKeyword){
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("servicecode", serviceTypeKeyword.getServicecode());
        parametersMap.put("tenantid", serviceTypeKeyword.getTenantId());
        parametersMap.put("keyword", serviceTypeKeyword.getKeyword());
        parametersMap.put("createddate", serviceTypeKeyword.getCreatedDate());
        parametersMap.put("createdby", serviceTypeKeyword.getCreatedBy());
        parametersMap.put("lastmodifiedby", serviceTypeKeyword.getLastModifiedBy());
        parametersMap.put("lastmodifieddate", serviceTypeKeyword.getLastModifiedDate());

        return parametersMap;
    }
}
