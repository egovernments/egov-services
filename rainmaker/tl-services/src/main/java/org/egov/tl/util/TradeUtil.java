package org.egov.tl.util;

import org.egov.tl.config.TLConfiguration;
import org.egov.tl.web.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeUtil {

    @Autowired
    private TLConfiguration config;


    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }

    public StringBuilder getCalculationURI(){
        StringBuilder uri = new StringBuilder(config.getCalculatorHost());
        uri.append(config.getCalculateEndpoint());
        return uri;
    }



}
