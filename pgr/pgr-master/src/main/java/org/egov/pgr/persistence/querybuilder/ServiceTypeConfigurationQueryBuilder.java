package org.egov.pgr.persistence.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class ServiceTypeConfigurationQueryBuilder {

    public String getInsertQuery(){
        return "INSERT INTO eg_servicetypeconfiguration ( tenantid, servicecode, applicationfeesenabled, notificationenabled, slaenabled, glCode, online, source, url) "
                + " VALUES ( :tenantid, :servicecode, :applicationfeesenabled, :notificationenabled, :slaenabled, :glCode, :online, :source, :url)";
    }
    
    public String getUpdateQuery(){
        return "UPDATE  eg_servicetypeconfiguration SET tenantid= :tenantid, servicecode= :servicecode, applicationfeesenabled= :applicationfeesenabled, notificationenabled= :notificationenabled, slaenabled= :slaenabled, glCode= :glCode, online= :online, source= :source, url= :url where servicecode = :servicecode and tenantid = :tenantid ";
    }
    
    
}