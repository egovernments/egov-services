package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDCBRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @Valid
    private PropertyDCB propertyDCB;
}
