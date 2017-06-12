package builders.commonMaster.createHoliday;

import entities.requests.commonMasters.createHoliday.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder() {
        requestInfo.setApiId("1");
        requestInfo.setVer("1");
        requestInfo.setTs("08-06-2017 01:01:01");
        requestInfo.setAction("create");
        requestInfo.setDid("");
        requestInfo.setKey("");
        requestInfo.setMsgId("");
        requestInfo.setRequesterId("");
        requestInfo.setAuthToken("aeiou");
    }

    public RequestInfoBuilder withApi_id(String api_id) {
        requestInfo.setApiId(api_id);
        return this;
    }

    public RequestInfoBuilder withTs(String ts) {
        requestInfo.setTs(ts);
        return this;
    }

    public RequestInfoBuilder withMsg_id(String msg_id) {
        requestInfo.setMsgId(msg_id);
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
        requestInfo.setRequesterId(requester_id);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }
}
