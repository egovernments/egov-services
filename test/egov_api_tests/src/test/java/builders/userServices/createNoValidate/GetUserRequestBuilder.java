package builders.userServices.createNoValidate;

import entities.requests.userServices.createNoValidate.GetUserRequest;
import entities.requests.userServices.createNoValidate.RequestInfo;

public class GetUserRequestBuilder {

    GetUserRequest request = new GetUserRequest();

    public GetUserRequestBuilder() {request.setTenantId("default");}

    public GetUserRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        request.setActive(true);
        return this;
    }

    public GetUserRequestBuilder withId(int[] id){
        request.setId(id);
        return this;
    }

    public GetUserRequestBuilder withUserName(String name){
        request.setUserName(name);
        return this;
    }

    public GetUserRequestBuilder withUserType(String type){
        request.setUserType(type);
        return this;
    }

    public GetUserRequestBuilder withActive(Boolean active){
        request.setActive(active);
        return this;
    }

    public GetUserRequest build(){
        return request;
    }
}
