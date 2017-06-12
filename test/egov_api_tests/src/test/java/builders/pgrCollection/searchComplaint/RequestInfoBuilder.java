package builders.pgrCollection.searchComplaint;

import entities.requests.pgrCollections.searchComplaint.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("org.egov.pgr");
        requestInfo.setAction("POST");
        requestInfo.setDid("4354648646");
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setRequesterId("61");
        requestInfo.setTs("21-04-2017 16:20:57");
        requestInfo.setVer("1.0");
    }

    public RequestInfoBuilder withAuthToken(String token){
        requestInfo.setAuthToken(token);
        return this;
    }

    public RequestInfo build(){
        return requestInfo;
    }
}
