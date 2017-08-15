package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ServiceCategoryQueryBuilder {

    public String getCategoryByTenantIdAndId(){
        return "SELECT * FROM egpgr_complainttype_category WHERE tenantid = :tenantid AND id = :id";
    }
}
