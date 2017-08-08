package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.springframework.stereotype.Component;


@Component
public class ServiceTypeKeywordQueryBuilder {

    public String keywordsSearchQuery(ServiceTypeSearchCriteria serviceTypeSearchCriteria) {

        StringBuilder query = new StringBuilder("SELECT * FROM servicetype_keyword WHERE tenantid = :tenantid AND servicecode = :code AND keyword in (:keywords)");

        return query.toString();
    }
}