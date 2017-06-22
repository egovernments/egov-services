package org.egov.wcms.transanction.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponseInfo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("code")
    private String code;
        
}
