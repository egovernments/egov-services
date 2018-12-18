package org.egov.wf.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.common.contract.request.User;

import java.util.List;

@Data
public class BusinessServiceSearchCriteria {



    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("stateUuid")
    private String stateUuid;

    @JsonProperty("action")
    private String action;


}
