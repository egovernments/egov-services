package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeQueryBuilder {

    public String getInsertQuery(){
        return "INSERT INTO egpgr_complainttype (id, name, department, code, isactive, description, metadata, type, category, isday, createddate, lastmodifieddate, createdby, lastmodifiedby,slahours,hasfinancialimpact,tenantid, localname)"
                +"VALUES (NEXTVAL('seq_egpgr_complainttype'), :name, :department, :code, :isactive, :description, :metadata, :type, :category, :isday, :createddate, :lastmodifieddate, :createdby, :lastmodifiedby, :slahours, :hasfinancialimpact, :tenantid, :localname)";
    }

    public String getUpdateQuery(){
        return "UPDATE egpgr_complainttype SET name = :name, department = :department, code = :code, isactive = :isactive," +
                " description = :description, metadata = :metadata, type = :type, category = :category, isday = :isday, " +
                "lastmodifieddate = :lastmodifieddate, lastmodifiedby = :lastmodifiedby, slahours = :slahours," +
                "hasfinancialimpact = :hasfinancialimpact, tenantid = :tenantid, localname = :localname WHERE code = :code AND tenantid = :tenantid";
    }

    public String getSearchQueryForUpdateValidation(){
        return "SELECT * FROM egpgr_complainttype WHERE code = :code AND tenantid = :tenantid";
    }

    public String buildSearchQuery(ServiceTypeSearchCriteria serviceTypeSearchCriteria){

        StringBuilder query = new StringBuilder("SELECT * FROM egpgr_complainttype WHERE tenantid = :tenantid");

        if(!serviceTypeSearchCriteria.isServiceCodeEmpty())
            addWhereClauseWithAnd(query,"code","code");

        if(!serviceTypeSearchCriteria.isCategoryEmpty())
            addWhereClauseWithAnd(query,"category","category");

        return query.toString();
    }

    public String getQuery(ServiceType serviceType){

        StringBuilder query = new StringBuilder("SELECT * FROM egpgr_complainttype WHERE tenantid = :tenantid" );

        if(!serviceType.isServiceCodeAbsent())
            addWhereClauseWithAnd(query,"upper(code)","code");

        if(!serviceType.isServiceNameAbsent())
        	addWhereClauseWithAnd(query, "upper(name)", "name");

        if(!serviceType.isCategoryAbsent())
        	addWhereClauseWithAnd(query, "category", "category");

        return query.toString();
    }

    public String getCategoryData(ServiceType serviceType) {

        StringBuilder query = new StringBuilder("SELECT * FROM egpgr_complainttype_category");

        if (!serviceType.isCategoryAbsent())
            addWhereClause(query, "id", "categoryId");

        if (!serviceType.isTenantIdAbsent())
            addWhereClauseWithAnd(query, "tenantId", "tenantId");

        return query.toString();
    }

    private StringBuilder addWhereClause(StringBuilder query, String fieldName, String paramName){
        return query.append(" WHERE ").append(fieldName).append("= :").append(paramName);
    }

    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
}