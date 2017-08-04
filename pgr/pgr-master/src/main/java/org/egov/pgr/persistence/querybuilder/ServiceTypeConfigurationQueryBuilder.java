package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ServiceTypeConfigurationQueryBuilder {

    public String getInsertQuery(){
        return "INSERT INTO eg_servicetypeconfiguration ( tenantid, servicecode, isapplicationfeesenabled, isnotificationenabled, isslaenabled) "
                + " VALUES ( :tenantid, :servicecode, :isapplicationfeesenabled, :isnotificationenabled, :isslaenabled)";
    }
}