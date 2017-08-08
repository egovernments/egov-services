package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class AttributeDefinitionQueryBuilder {

    public String findByServiceCodeAndTenantId(){

        StringBuilder query = new StringBuilder("SELECT * FROM attribute_definition WHERE tenantid = :tenantid AND servicecode = :servicecode");

        return query.toString();
    }
}
