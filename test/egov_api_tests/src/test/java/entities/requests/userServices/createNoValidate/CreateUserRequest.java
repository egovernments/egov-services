package entities.requests.userServices.createNoValidate;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateUserRequest {

    @JsonProperty("User")
    private User User;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public User getUser() {
        return this.User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
