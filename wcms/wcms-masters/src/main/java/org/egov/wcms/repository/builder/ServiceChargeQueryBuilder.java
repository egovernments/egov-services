package org.egov.wcms.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class ServiceChargeQueryBuilder {

    public String insertServiceChargeData() {
        return "Insert into egwtr_servicecharge (id,code,servicetype,servicechargeapplicable,servicechargetype"
                + ",description,effectivefrom,effectiveto,outsideulb,tenantid,createdby,createddate,lastmodifiedby,"
                + "lastmodifieddate) values (:id,:code,:servicetype,:servicechargeapplicable,:servicechargetype"
                + ",:description,:effectivefrom,:effectiveto,:outsideulb,:tenantid,:createdby,:createddate,:lastmodifiedby,"
                + ":lastmodifieddate)";
    }

    public String insertServiceChargeDetailsData() {
        return "Insert into egwtr_servicecharge_details (id,code,uomfrom,uomto,amountorpercentage,servicecharge,tenantid)"
                + " values(:id,:code,:uomfrom,:uomto,:amountorpercentage,:servicecharge,:tenantid)";
    }

    public String updateServiceChargeData() {
        return "Update egwtr_servicecharge set servicetype =:servicetype,servicechargeapplicable =:servicechargeapplicable,"
                + "servicechargetype =:servicechargetype,description =:description,effectivefrom =:effectivefrom,"
                + "effectiveto =:effectiveto,outsideulb =:outsideulb,lastmodifiedby =:lastmodifiedby,lastmodifieddate"
                + " =:lastmodifieddate where code =:code and tenantid =:tenantid";
    }

    public String deleteServiceChargeDetailsData() {
        return "Delete from egwtr_servicecharge_details where servicecharge =:servicecharge and tenantid =:tenantid";
    }

}
