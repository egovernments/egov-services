package org.egov.workflow.repository.consumer.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class RequestInfo {

    @JsonProperty("api_id")
    private String apiId = null;

    @JsonProperty("ver")
    private String ver = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("ts")
    private Date ts = null;

    @JsonProperty("action")
    private String action = null;

    @JsonProperty("did")
    private String did = null;

    @JsonProperty("key")
    private String key = null;

    @JsonProperty("msg_id")
    private String msgId = null;

    @JsonProperty("requester_id")
    private String requesterId = null;

    @JsonProperty("auth_token")
    private String authToken = null;

    @JsonProperty("tenant_id")
    private String tenantId;

}