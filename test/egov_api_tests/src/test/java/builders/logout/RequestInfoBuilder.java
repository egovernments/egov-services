package builders.logout;

import entities.requests.logout.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();


    public RequestInfoBuilder() {
        requestInfo.setApiId("org.egov.pgr");
        requestInfo.setVer("1.0");
        requestInfo.setTs("13-04-2017 12:13:30");
        requestInfo.setAction("POST");
        requestInfo.setDid("4354648646");
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setRequesterId("61");
        requestInfo.setAuthToken("bed8d6d9-9477-48ca-94af-29ed3f7ab0f4");
    }

    public RequestInfoBuilder withApiId(String apiId) {
        requestInfo.setApiId(apiId);
        return this;
    }

    public RequestInfoBuilder withVer(String ver) {
        requestInfo.setVer(ver);
        return this;
    }

    public RequestInfoBuilder withTs(String ts) {
        requestInfo.setTs(ts);
        return this;
    }

    public RequestInfoBuilder withAction(String action) {
        requestInfo.setAction(action);
        return this;
    }

    public RequestInfoBuilder withDid(String did) {
        requestInfo.setDid(did);
        return this;
    }

    public RequestInfoBuilder withKey(String key) {
        requestInfo.setKey(key);
        return this;
    }

    public RequestInfoBuilder withMsgId(String msgId) {
        requestInfo.setMsgId(msgId);
        return this;
    }

    public RequestInfoBuilder withRequesterId(String requesterId) {
        requestInfo.setRequesterId(requesterId);
        return this;
    }

    public RequestInfoBuilder withAuthToken(String authToken) {
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }

}
