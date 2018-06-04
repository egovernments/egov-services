package org.egov.pt.util;

import org.egov.pt.web.models.AuditDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class PropertyUtil {




    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = new Date().getTime();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }
    
    public Object getMdmsRequest(Map<String, Map<String, String>> masterDetailMap) {
    	
    	
    	return null;
    }


}
