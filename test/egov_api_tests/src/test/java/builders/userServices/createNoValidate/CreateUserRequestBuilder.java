package builders.userServices.createNoValidate;

import entities.requests.userServices.createNoValidate.CreateUserRequest;
import entities.requests.userServices.createNoValidate.RequestInfo;
import entities.requests.userServices.createNoValidate.User;

public class CreateUserRequestBuilder {

  CreateUserRequest request = new CreateUserRequest();

  public CreateUserRequestBuilder withRequestInfo(RequestInfo requestInfo){
      request.setRequestInfo(requestInfo);
      return this;
  }

  public CreateUserRequestBuilder withUser(User user){
      request.setUser(user);
      return this;
  }

  public CreateUserRequest build(){
      return request;
  }
}
