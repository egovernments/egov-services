package org.egov.citizen.model;

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
public class AttributeValueDefinition {
    private String key;
    private String name;
    @JsonProperty("isActive")
    private boolean active;
    private String groupCode;


}
