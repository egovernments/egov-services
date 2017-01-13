package org.egov.notification.web.model;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestInfo {
	@JsonProperty("api_id")
	private String api_id = null;

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
	private String msg_id = null;

	@JsonProperty("requester_id")
	private String requester_id = null;

	@JsonProperty("auth_token")
	private String auth_token = null;

	public RequestInfo api_id(String api_id) {
		this.api_id = api_id;
		return this;
	}

	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
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

	public RequestInfo msg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public RequestInfo requester_id(String requester_id) {
		this.requester_id = requester_id;
		return this;
	}

	public String getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(String requester_id) {
		this.requester_id = requester_id;
	}

	public RequestInfo auth_token(String auth_token) {
		this.auth_token = auth_token;
		return this;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	public boolean validate(RequestInfo requestInfo) {
		return (requestInfo.getApi_id() != null && !requestInfo.getApi_id().isEmpty() && requestInfo.getVer() != null
				&& !requestInfo.getVer().isEmpty()) && Objects.nonNull(requestInfo.getTs());
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
		return Objects.equals(this.api_id, requestInfo.api_id) && Objects.equals(this.ver, requestInfo.ver)
				&& Objects.equals(this.ts, requestInfo.ts) && Objects.equals(this.action, requestInfo.action)
				&& Objects.equals(this.did, requestInfo.did) && Objects.equals(this.key, requestInfo.key)
				&& Objects.equals(this.msg_id, requestInfo.msg_id)
				&& Objects.equals(this.requester_id, requestInfo.requester_id)
				&& Objects.equals(this.auth_token, requestInfo.auth_token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(api_id, ver, ts, action, did, key, msg_id, requester_id, auth_token);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RequestInfo {\n");

		sb.append("    apiId: ").append(toIndentedString(api_id)).append("\n");
		sb.append("    ver: ").append(toIndentedString(ver)).append("\n");
		sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
		sb.append("    action: ").append(toIndentedString(action)).append("\n");
		sb.append("    did: ").append(toIndentedString(did)).append("\n");
		sb.append("    key: ").append(toIndentedString(key)).append("\n");
		sb.append("    msgId: ").append(toIndentedString(msg_id)).append("\n");
		sb.append("    requesterId: ").append(toIndentedString(requester_id)).append("\n");
		sb.append("    authToken: ").append(toIndentedString(auth_token)).append("\n");
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