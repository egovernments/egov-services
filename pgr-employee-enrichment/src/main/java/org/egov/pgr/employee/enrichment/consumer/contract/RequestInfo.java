package org.egov.pgr.employee.enrichment.consumer.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class RequestInfo {

    public static final String API_ID = "api_id";
    public static final String VER = "ver";
    public static final String TS = "ts";
    public static final String ACTION = "action";
    public static final String DID = "did";
    public static final String KEY = "key";
    public static final String MSG_ID = "msg_id";
    public static final String REQUESTER_ID = "requester_id";
    public static final String AUTH_TOKEN = "auth_token";

    @JsonProperty(API_ID)
    private String apiId = null;

    @JsonProperty(VER)
    private String ver = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty(TS)
    private Date ts = null;

    @JsonProperty(ACTION)
    private String action = null;

    @JsonProperty(DID)
    private String did = null;

    @JsonProperty(KEY)
    private String key = null;

    @JsonProperty(MSG_ID)
    private String msgId = null;

    @JsonProperty(REQUESTER_ID)
    private String requesterId = null;

    @JsonProperty(AUTH_TOKEN)
    private String authToken = null;

}