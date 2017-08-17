package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ValueDefinitionQueryBuilder {

    public String findByTenantIdAndServiceCodeAndAttributeCode(){
        return "SELECT * FROM value_definition WHERE servicecode = :servicecode AND attributecode = :attributecode AND tenantid = :tenantid";
    }

    public String getInsertQuery(){
        return "INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, active, createddate, createdby)"+
                "VALUES (:servicecode, :attributecode, :key, :name, :tenantid, :active, :createddate, :createdby)";
    }
}