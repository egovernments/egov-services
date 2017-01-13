package org.egov.pgr.rest.web.model;

import java.util.Date;
import java.util.Objects;

import org.egov.infra.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestInfo   {
  @JsonProperty("api_id")
  private String apiId = null;

  @JsonProperty("ver")
  private String ver = null;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
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

  public RequestInfo apiId(String apiId) {
    this.apiId = apiId;
    return this;
  }

  public String getApiId() {
    return apiId;
  }

  public void setApiId(String apiId) {
    this.apiId = apiId;
  }

  public RequestInfo ver(String ver) {
    this.ver = ver;
    return this;
  }

  public String getVer() {
    return ver;
  }

  public void setVer(String ver) {
    this.ver = ver;
  }

  public RequestInfo ts(Date ts) {
    this.ts = ts;
    return this;
  }

  public Date getTs() {
    return ts;
  }

  public void setTs(Date ts) {
    this.ts = ts;
  }

  public RequestInfo action(String action) {
    this.action = action;
    return this;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public RequestInfo did(String did) {
    this.did = did;
    return this;
  }

  public String getDid() {
    return did;
  }

  public void setDid(String did) {
    this.did = did;
  }

  public RequestInfo key(String key) {
    this.key = key;
    return this;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public RequestInfo msgId(String msgId) {
    this.msgId = msgId;
    return this;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public RequestInfo requesterId(String requesterId) {
    this.requesterId = requesterId;
    return this;
  }

  public String getRequesterId() {
    return requesterId;
  }

  public void setRequesterId(String requesterId) {
    this.requesterId = requesterId;
  }

  public RequestInfo authToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public boolean validate(RequestInfo requestInfo){
	  return (StringUtils.isNotBlank(requestInfo.getApiId()) &&
			  StringUtils.isNotBlank(requestInfo.getVer()) &&
			  Objects.nonNull(requestInfo.getTs()));
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestInfo requestInfo = (RequestInfo) o;
    return Objects.equals(this.apiId, requestInfo.apiId) &&
        Objects.equals(this.ver, requestInfo.ver) &&
        Objects.equals(this.ts, requestInfo.ts) &&
        Objects.equals(this.action, requestInfo.action) &&
        Objects.equals(this.did, requestInfo.did) &&
        Objects.equals(this.key, requestInfo.key) &&
        Objects.equals(this.msgId, requestInfo.msgId) &&
        Objects.equals(this.requesterId, requestInfo.requesterId) &&
        Objects.equals(this.authToken, requestInfo.authToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiId, ver, ts, action, did, key, msgId, requesterId, authToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestInfo {\n");
    
    sb.append("    apiId: ").append(toIndentedString(apiId)).append("\n");
    sb.append("    ver: ").append(toIndentedString(ver)).append("\n");
    sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    did: ").append(toIndentedString(did)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    msgId: ").append(toIndentedString(msgId)).append("\n");
    sb.append("    requesterId: ").append(toIndentedString(requesterId)).append("\n");
    sb.append("    authToken: ").append(toIndentedString(authToken)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}