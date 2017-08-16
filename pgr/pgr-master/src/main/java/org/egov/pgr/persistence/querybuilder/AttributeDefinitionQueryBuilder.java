package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.AttributeDefinition;
import org.springframework.stereotype.Component;

@Component
public class AttributeDefinitionQueryBuilder {

    public String findByServiceCodeAndTenantId(){

        StringBuilder query = new StringBuilder("SELECT * FROM attribute_definition WHERE tenantid = :tenantid AND servicecode = :servicecode");

        return query.toString();
    }

    public String getInsertQuery(){
        return "INSERT INTO attribute_definition (code, datatype, variable, required, datatypedescription, ordernum, description, servicecode, active, tenantid, createddate, createdby, lastmodifieddate, lastmodifiedby)"
                + " VALUES (:code, :datatype, :variable, :required, :datatypedescription, :ordernum, :description, :servicecode, :active, :tenantid, :createddate, :createdby, :lastmodifieddate, :lastmodifiedby)";
    }
    
    public String getCodeTenantQuery(AttributeDefinition attributeDefinition){

        StringBuilder query = new StringBuilder("SELECT * FROM attribute_definition WHERE tenantid = :tenantid" );

        if(!attributeDefinition.isAttributCodeAbsent())
            addWhereClauseWithAnd(query,"upper(code)","code");


        if(!attributeDefinition.isServiceCodeAbsent())
        	addWhereClauseWithAnd(query, "upper(servicecode)", "servicecode");

        return query.toString();
    }
    
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
}
