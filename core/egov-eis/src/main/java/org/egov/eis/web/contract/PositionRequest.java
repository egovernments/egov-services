package org.egov.eis.web.contract;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionRequest {

    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;
    @Valid
    @JsonProperty("Position")
    private Position position = null;

    @JsonProperty("UserId")
    private Long userId;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(final RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
