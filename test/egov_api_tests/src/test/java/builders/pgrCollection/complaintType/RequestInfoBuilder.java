package builders.pgrCollection.complaintType;

import entities.requests.pgrCollections.complaintType.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("org.egov.pgr");
        requestInfo.setVer("1.0");
        requestInfo.setTs("21-04-2017 16:20:57");
        requestInfo.setAction("POST");
        requestInfo.setDid("4354648646");
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setRequesterId("61");
    }

    public RequestInfoBuilder withAuthToken(String authToken){
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfo build(){
        return requestInfo;
    }
}
