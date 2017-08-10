package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ServiceDefinitionQueryBuilder {

    public String getInsertQuery(){
        return "INSERT INTO service_definition (code, tenantid, createddate, createdby)"+
                "VALUES (:code, :tenantid, :createddate, :createdby)";
    }
}
