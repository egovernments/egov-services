package builders.eGovEIS.attendances;

import entities.requests.eGovEIS.attendances.RequestInfo;

public class RequestInfoBuilder {
    RequestInfo requestInfo = new RequestInfo();
    entities.requests.eGovEIS.RequestInfo requestInfo1 = new entities.requests.eGovEIS.RequestInfo();

    public RequestInfoBuilder() {
        requestInfo.setApiId("123456");
        requestInfo.setVer("1");
        requestInfo.setTs("");
        requestInfo.setMsgId("1");
        requestInfo.setStatus("200");
        requestInfo.setResMsgId("uief87324");
        requestInfo.setAuthToken("");
    }

    public RequestInfoBuilder(String msg) {
        requestInfo1.setApiId("emp");
        requestInfo1.setVer("1.0");
        requestInfo1.setTs("10/03/2017");
        requestInfo1.setAction("create");
        requestInfo1.setAuthToken("");
        requestInfo1.setRequesterId("rajesh");
        requestInfo1.setDid("1");
        requestInfo1.setKey("abcdkey");
        requestInfo1.setMsgId("20170310130900");
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

    public RequestInfoBuilder withMsgId(String msgId) {
        requestInfo.setMsgId(msgId);
        return this;
    }

    public RequestInfoBuilder withStatus(String status) {
        requestInfo.setStatus(status);
        return this;
    }

    public RequestInfoBuilder withResMsgId(String resMsgId) {
        requestInfo.setResMsgId(resMsgId);
        return this;
    }

    public RequestInfoBuilder withAuthToken1(String authToken) {
        requestInfo1.setAuthToken(authToken);
        return this;
    }

    public RequestInfoBuilder withAuthToken(String authToken) {
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }

    public entities.requests.eGovEIS.RequestInfo build1() {
        return requestInfo1;
    }
}