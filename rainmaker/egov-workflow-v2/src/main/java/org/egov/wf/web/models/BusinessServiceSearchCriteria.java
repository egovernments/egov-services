package org.egov.wf.web.models;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

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
