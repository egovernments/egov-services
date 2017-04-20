package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Attribute {

    @JsonProperty("variable")
    private Boolean variable;

    @JsonProperty("code")
    private String code;

    @JsonProperty("datatype")
    private String datatype;

    @JsonProperty("required")
    private Boolean required;

    @JsonProperty("datatypeDescription")
    private String datatypeDescription;

    @JsonProperty("values")
    private List<Value> values;
    
    @JsonProperty("tenantId")
    private String tenantId;
}