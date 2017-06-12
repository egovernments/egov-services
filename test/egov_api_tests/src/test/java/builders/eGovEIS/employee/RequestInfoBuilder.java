package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder() {
        requestInfo.setApiId("emp");
        requestInfo.setVer("1.0");
        requestInfo.setTs("01-01-2017 01:01:01");
        requestInfo.setAction("create");
        requestInfo.setDid("1");
        requestInfo.setKey("abcdkey");
        requestInfo.setMsgId("20170310130900");
        requestInfo.setRequesterId("1");
    }

    public RequestInfoBuilder withAuthToken(String authToken) {
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }
}
