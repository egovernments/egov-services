package builders.userServices.userDetails;

import entities.requests.userServices.userDetails.RequestInfo;
import entities.requests.userServices.userDetails.UserDetailsRequest;

public class UserDetailsRequestBuilder {

    UserDetailsRequest request = new UserDetailsRequest();

    public UserDetailsRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public UserDetailsRequest build(){
        return request;
    }
}
