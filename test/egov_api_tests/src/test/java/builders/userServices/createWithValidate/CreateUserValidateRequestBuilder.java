package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.CreateUserValidateRequest;
import entities.requests.userServices.createWithValidate.RequestInfo;
import entities.requests.userServices.createWithValidate.User;

public class CreateUserValidateRequestBuilder {

    CreateUserValidateRequest request = new CreateUserValidateRequest();

    public CreateUserValidateRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public CreateUserValidateRequestBuilder withUser(User user){
        request.setUser(user);
        return this;
    }

    public CreateUserValidateRequest build(){
        return request;
    }
}
