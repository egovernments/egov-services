package org.egov.citizen.model;

import java.util.List;

import org.egov.citizen.model.enums.DataType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@Setter
@ToString
@NoArgsConstructor
public class Attributes {
	
	private String name;
    private boolean variable;
    private String code;
    @JsonProperty(value = "datatype")
    private DataType dataType;
    private boolean required;
    private String description;
    @JsonProperty(value = "datatype_description")
    private String dataTypeDescription;
    private String url;
    private String groupCode;
    private Boolean isActive;
    private Integer order;
   // private List<String> roles;
   // private List<String> actions;
   // private List<ConstraintDefinition> constraints;
    private List<AttributeValueDefinition> attribValues;

 }

