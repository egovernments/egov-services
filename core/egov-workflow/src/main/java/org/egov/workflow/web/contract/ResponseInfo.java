package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseInfo {
    @JsonProperty("api_id")
    private String apiId;

    @JsonProperty("ver")
    private String ver;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("res_msg_id")
    private String resMsgId;

    @JsonProperty("msg_id")
    private String msgId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("jurisdiction_id")
    private String jurisdictionId;
}