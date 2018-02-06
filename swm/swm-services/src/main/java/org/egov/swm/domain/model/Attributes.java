package org.egov.swm.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Attributes {
    @JsonProperty("key")
    private String key = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("value")
    private Object value = null;

}