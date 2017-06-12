package entities.responses.userServices.createUser;

public class UserResponse {
    private ResponseInfo responseInfo;
    private User[] user;

    public ResponseInfo getResponseInfo() {
        return this.responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public User[] getUser() {
        return this.user;
    }

    public void setUser(User[] user) {
        this.user = user;
    }
}
