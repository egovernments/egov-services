package builders.pgrCollection.createComplaint;

import entities.requests.pgrCollections.createComplaint.RequestInfo;

public final class RequestInfoBuilder {
    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder() {
        requestInfo.setApi_id("org.egov.pgr");
        requestInfo.setTs("16-03-2017 12:09:14");
        requestInfo.setMsg_id("654654");
        requestInfo.setAction(null);
        requestInfo.setDid("4354648646");
        requestInfo.setVer("1.0");
        requestInfo.setKey("xyz");
        requestInfo.setRequester_id("61");
    }

    public RequestInfoBuilder(String update) {
        requestInfo.setApi_id("org.egov.pgr");
        requestInfo.setMsg_id("c53d976c-5f7a-4a4c-b15f-b7e60fea0804");
        requestInfo.setAction("PUT");
        requestInfo.setAuthToken(null);
        requestInfo.setVer("1.0");
        requestInfo.setRequester_id("18");
        requestInfo.setTs("22-03-2017 18:13:03");
        requestInfo.setDid(null);
        requestInfo.setKey(null);
    }

    public RequestInfoBuilder withApi_id(String api_id) {
        requestInfo.setApi_id(api_id);
        return this;
    }

    public RequestInfoBuilder withTs(String ts) {
        requestInfo.setTs(ts);
        return this;
    }

    public RequestInfoBuilder withMsg_id(String msg_id) {
        requestInfo.setMsg_id(msg_id);
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

    public RequestInfoBuilder withAuth_token(String auth_token) {
        requestInfo.setAuthToken(auth_token);
        return this;
    }

    public RequestInfoBuilder withVer(String ver) {
        requestInfo.setVer(ver);
        return this;
    }

    public RequestInfoBuilder withKey(String key) {
        requestInfo.setKey(key);
        return this;
    }

    public RequestInfoBuilder withRequester_id(String requester_id) {
        requestInfo.setRequester_id(requester_id);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }
}
