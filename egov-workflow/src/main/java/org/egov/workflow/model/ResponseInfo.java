package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseInfo {
    @JsonProperty("api_id")
    private String apiId = null;

    @JsonProperty("ver")
    private String ver = null;

    @JsonProperty("ts")
    private String ts = null;

    @JsonProperty("res_msg_id")
    private String resMsgId = null;

    @JsonProperty("msg_id")
    private String msgId = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("jurisdiction_id")
    private String jurisdictionId;
}