package org.egov.wf.web.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.common.contract.request.User;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BusinessServiceSearchCriteria {


    @NotNull
    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("businessServices")
    private List<String> businessServices;

    @JsonIgnore
    private List<String> stateUuids;

    @JsonIgnore
    private List<String> actionUuids;



}
