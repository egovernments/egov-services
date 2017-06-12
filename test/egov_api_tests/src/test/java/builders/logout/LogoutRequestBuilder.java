package builders.logout;

import entities.requests.logout.LogoutRequest;
import entities.requests.logout.RequestInfo;

public final class LogoutRequestBuilder {

    LogoutRequest logoutRequest = new LogoutRequest();
    RequestInfo requestInfo = new RequestInfo();

    public LogoutRequestBuilder() {
        logoutRequest.setRequestInfo(requestInfo);
    }

    public LogoutRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        logoutRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public LogoutRequest build() {
        return logoutRequest;
    }
}
