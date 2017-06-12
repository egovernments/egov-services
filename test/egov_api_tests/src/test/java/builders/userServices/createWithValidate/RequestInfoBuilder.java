package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("emp");
        requestInfo.setVer("1.0");
        requestInfo.setTs("4-11-2017 3:18:55 PM");
        requestInfo.setRequesterId(null);
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setDid("4354648646");
    }

    public RequestInfoBuilder withAuthToken(String authToken){
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfo build(){
        return requestInfo;
    }
}
