package org.egov.user.web.contract.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Action {
    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("orderNumber")
    private Integer orderNumber;

    @JsonProperty("queryParams")
    private String queryParams;

    @JsonProperty("parentModule")
    private String parentModule;

    @JsonProperty("serviceCode")
    private String serviceCode;
}

