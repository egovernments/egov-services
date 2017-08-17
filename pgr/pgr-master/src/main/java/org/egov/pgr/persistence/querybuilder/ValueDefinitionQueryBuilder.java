package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.ValueDefinition;
import org.jsoup.select.Evaluator.IsEmpty;
import org.springframework.stereotype.Component;

@Component
public class ValueDefinitionQueryBuilder {

    public String findByTenantIdAndServiceCodeAndAttributeCode(){
        return "SELECT * FROM value_definition WHERE servicecode = :servicecode AND attributecode = :attributecode AND tenantid = :tenantid";
    }

    public String getInsertQuery(){
        return "INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, active, required, createddate, createdby)"+
                "VALUES (:servicecode, :attributecode, :key, :name, :tenantid, :active, :required, :createddate, :createdby)";
    }
    
    public String getCodeTenantQuery(ValueDefinition valueDefinition){

        StringBuilder query = new StringBuilder("SELECT * FROM value_definition WHERE tenantid = :tenantid" );


        if(!valueDefinition.getServiceCode().isEmpty())
        	addWhereClauseWithAnd(query, "upper(servicecode)", "servicecode");
        
        if(! valueDefinition.getAttributeCode().isEmpty())
        	addWhereClauseWithAnd(query, "upper(attributecode)", "attributecode");
        
        if(!valueDefinition.isKeyAbsent())
        	addWhereClauseWithAnd(query, "key", "key");

        return query.toString();
    }
    
    
    
    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
}