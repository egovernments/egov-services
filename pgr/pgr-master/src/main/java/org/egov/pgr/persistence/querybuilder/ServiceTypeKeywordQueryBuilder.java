package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.springframework.stereotype.Component;


@Component
public class ServiceTypeKeywordQueryBuilder {

    public String keywordsSearchQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM servicetype_keyword WHERE tenantid = :tenantid AND servicecode = :code AND keyword in (:keywords)");

        return query.toString();
    }

    public String getInsertQuery(){
        return "INSERT INTO servicetype_keyword (id, servicecode, keyword, tenantid, createddate, createdby, lastmodifieddate, lastmodifiedby)"
                + " VALUES (NEXTVAL('seq_servicetype_keyword'), :servicecode, :keyword, :tenantid, :createddate, :createdby, :lastmodifieddate, :lastmodifiedby)";
    }

    public String getUpdateQuery(){
        return "UPDATE servicetype_keyword SET servicecode = :servicecode, keyword = :keyword, tenantid = :tenantid, " +
                "lastmodifieddate = :lastmodifieddate, lastmodifiedby = :lastmodifiedby WHERE servicecode = :servicecode AND " +
                "tenantid = :tenantid";
    }
}