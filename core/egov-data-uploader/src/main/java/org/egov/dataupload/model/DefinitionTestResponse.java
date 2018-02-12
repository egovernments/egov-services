package org.egov.dataupload.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.DocumentContext;
import groovy.transform.ToString;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefinitionTestResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("results")
    public List<String> results;

}