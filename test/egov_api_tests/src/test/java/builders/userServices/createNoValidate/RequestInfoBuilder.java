package builders.userServices.createNoValidate;

import entities.requests.userServices.createNoValidate.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("emp");
        requestInfo.setVer("1.0");
        requestInfo.setAction("create");
        requestInfo.setDid("1");
        requestInfo.setKey("abcdkey");
        requestInfo.setMsgId("20170310130900");
        requestInfo.setRequesterId("rajesh");
    }

    public RequestInfoBuilder(String getuser){
        requestInfo.setApiId("emp");
        requestInfo.setVer("1.0");
        requestInfo.setTs("16-03-2017 12:09:14");
        requestInfo.setAction(null);
        requestInfo.setDid("4354648646");
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setRequesterId("61");
    }

    public RequestInfoBuilder withAuthToken(String token){
        requestInfo.setAuthToken(token);
        return this;
    }

    public RequestInfo build(){
        return requestInfo;
    }
}
