package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ValueDefinitionQueryBuilder {

    public String findByTenantIdAndServiceCodeAndAttributeCode(){
        return "SELECT * FROM value_definition WHERE servicecode = :servicecode AND attributecode = :attributecode AND tenantid = :tenantid";
    }
}