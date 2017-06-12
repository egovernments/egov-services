package builders.eGovEIS.leaveManagement.create;

import entities.requests.eGovEIS.leaveManagement.create.RequestInfo;
import entities.requests.eGovEIS.leaveManagement.create.UserInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();
    UserInfo userInfo = new UserInfoBuilder().build();

    public RequestInfoBuilder() {
        requestInfo.setUserInfo(userInfo);
        requestInfo.setApiId("1");
        requestInfo.setTs("10-01-2017 01:01:01");
        requestInfo.setMsgId("");
        requestInfo.setAction("create");
        requestInfo.setDid("");
        requestInfo.setAuthToken("4f77f679-d609-45e8-beb7-26f449ca209f");
        requestInfo.setVer("1");
        requestInfo.setKey("");
        requestInfo.setRequesterId("");
    }

    public RequestInfoBuilder withUserInfo(UserInfo userInfo) {
        requestInfo.setUserInfo(userInfo);
        return this;
    }

    public RequestInfoBuilder withApiId(String api_id) {
        requestInfo.setApiId(api_id);
        return this;
    }

    public RequestInfoBuilder withTs(String ts) {
        requestInfo.setTs(ts);
        return this;
    }

    public RequestInfoBuilder withMsgId(String msg_id) {
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

    public RequestInfoBuilder withAuthToken(String auth_token) {
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

    public RequestInfoBuilder withRequesterId(String requester_id) {
        requestInfo.setRequesterId(requester_id);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }
}
