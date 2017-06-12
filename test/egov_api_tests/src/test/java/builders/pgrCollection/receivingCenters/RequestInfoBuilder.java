package builders.pgrCollection.receivingCenters;

import entities.requests.pgrCollections.receivingCenters.RequestInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("org.egov.pgr");
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
