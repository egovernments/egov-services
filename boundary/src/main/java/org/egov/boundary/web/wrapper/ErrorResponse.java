package org.egov.boundary.web.wrapper;

public class ErrorResponse {

	private ResponseInfo responseInfo;

	private Error error;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
