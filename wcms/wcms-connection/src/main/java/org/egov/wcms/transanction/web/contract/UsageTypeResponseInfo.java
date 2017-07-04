package org.egov.wcms.transanction.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageTypeResponseInfo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("usageTypeId")
    private String usageTypeId;

}
