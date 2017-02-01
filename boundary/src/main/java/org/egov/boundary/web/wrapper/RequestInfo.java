package org.egov.boundary.web.wrapper;

public class RequestInfo extends Info {

	private String action = null;

	private String did = null;

	private String msg_id = null;

	private String requester_id = null;

	private String auth_token = null;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(String requester_id) {
		this.requester_id = requester_id;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

}
