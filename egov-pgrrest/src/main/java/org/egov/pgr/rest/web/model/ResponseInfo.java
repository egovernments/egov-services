package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInfo   {
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
  
  public ResponseInfo(String apiId,String ver,String ts,String resMsgId,String msgId,String status){
	  this.apiId = apiId;
	  this.ver = ver;
	  this.ts = ts;
	  this.resMsgId = resMsgId;
	  this.msgId = msgId;
	  this.status = status;
  }

  public ResponseInfo apiId(String apiId) {
    this.apiId = apiId;
    return this;
  }

  public String getApiId() {
    return apiId;
  }

  public void setApiId(String apiId) {
    this.apiId = apiId;
  }

  public ResponseInfo ver(String ver) {
    this.ver = ver;
    return this;
  }

  public String getVer() {
    return ver;
  }

  public void setVer(String ver) {
    this.ver = ver;
  }

  public ResponseInfo ts(String ts) {
    this.ts = ts;
    return this;
  }

  public String getTs() {
    return ts;
  }

  public void setTs(String ts) {
    this.ts = ts;
  }

  public ResponseInfo resMsgId(String resMsgId) {
    this.resMsgId = resMsgId;
    return this;
  }

  public String getResMsgId() {
    return resMsgId;
  }

  public void setResMsgId(String resMsgId) {
    this.resMsgId = resMsgId;
  }

  public ResponseInfo msgId(String msgId) {
    this.msgId = msgId;
    return this;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public ResponseInfo status(String status) {
    this.status = status;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseInfo responseInfo = (ResponseInfo) o;
    return Objects.equals(this.apiId, responseInfo.apiId) &&
        Objects.equals(this.ver, responseInfo.ver) &&
        Objects.equals(this.ts, responseInfo.ts) &&
        Objects.equals(this.resMsgId, responseInfo.resMsgId) &&
        Objects.equals(this.msgId, responseInfo.msgId) &&
        Objects.equals(this.status, responseInfo.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiId, ver, ts, resMsgId, msgId, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseInfo {\n");
    
    sb.append("    apiId: ").append(toIndentedString(apiId)).append("\n");
    sb.append("    ver: ").append(toIndentedString(ver)).append("\n");
    sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
    sb.append("    resMsgId: ").append(toIndentedString(resMsgId)).append("\n");
    sb.append("    msgId: ").append(toIndentedString(msgId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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