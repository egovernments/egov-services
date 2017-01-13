package org.egov.notification.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInfo {
	@JsonProperty("api_id")
	private String api_id = null;

	@JsonProperty("ver")
	private String ver = null;

	@JsonProperty("ts")
	private String ts = null;

	@JsonProperty("res_msg_id")
	private String res_msg_id = null;

	@JsonProperty("msg_id")
	private String msg_id = null;

	@JsonProperty("status")
	private String status = null;

	public ResponseInfo(String apiId, String ver, String ts, String resMsgId, String msgId, String status) {
		this.api_id = apiId;
		this.ver = ver;
		this.ts = ts;
		this.res_msg_id = resMsgId;
		this.msg_id = msgId;
		this.status = status;
	}

	public ResponseInfo api_id(String apiId) {
		this.api_id = apiId;
		return this;
	}

	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
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

	public ResponseInfo res_msg_id(String res_msg_id) {
		this.res_msg_id = res_msg_id;
		return this;
	}

	public String getRes_msg_id() {
		return res_msg_id;
	}

	public void setRes_msg_id(String res_msg_id) {
		this.res_msg_id = res_msg_id;
	}

	public ResponseInfo msg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
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
		return Objects.equals(this.api_id, responseInfo.api_id) && Objects.equals(this.ver, responseInfo.ver)
				&& Objects.equals(this.ts, responseInfo.ts) && Objects.equals(this.res_msg_id, responseInfo.res_msg_id)
				&& Objects.equals(this.msg_id, responseInfo.msg_id) && Objects.equals(this.status, responseInfo.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(api_id, ver, ts, res_msg_id, msg_id, status);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ResponseInfo {\n");

		sb.append("    apiId: ").append(toIndentedString(api_id)).append("\n");
		sb.append("    ver: ").append(toIndentedString(ver)).append("\n");
		sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
		sb.append("    resMsgId: ").append(toIndentedString(res_msg_id)).append("\n");
		sb.append("    msgId: ").append(toIndentedString(msg_id)).append("\n");
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