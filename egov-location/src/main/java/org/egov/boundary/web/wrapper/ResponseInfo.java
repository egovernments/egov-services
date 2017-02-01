package org.egov.boundary.web.wrapper;

public class ResponseInfo extends Info {

	private String resMsgId = null;
	private String status = null;

	public String getResMsgId() {
		return resMsgId;
	}

	public void setResMsgId(String resMsgId) {
		this.resMsgId = resMsgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
