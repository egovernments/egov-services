package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@NoArgsConstructor
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
    private List<Value> values = new ArrayList<Value>();

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDatatype_description() {
        return datatypeDescription;
    }

    public void setDatatype_description(String datatypeDescription) {
        this.datatypeDescription = datatypeDescription;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}