package org.egov.poc.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyRecord {

    private String tenantId;

    private String crn;

    private String serviceCode;

    private String status;

    private String url;

    public void processStatus(){
        if(status.equalsIgnoreCase("registered"))
            this.status = "PAYMENT_INITIATED";
        else if(status.equalsIgnoreCase("inprocess"))
            this.status = "PAYMENT_ACCEPTED";
        else
            this.status = "PAYMENT_RECEIVED";
    }
}
