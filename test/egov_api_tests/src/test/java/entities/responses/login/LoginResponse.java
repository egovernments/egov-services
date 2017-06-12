package entities.responses.login;

import org.codehaus.jackson.annotate.JsonProperty;

public class LoginResponse {

    private String scope;

    private ResponseInfo ResponseInfo;

    private String expires_in;

    private String token_type;

    private String refresh_token;

    private String access_token;

    @JsonProperty("UserRequest")
    private UserRequest userRequest;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public ResponseInfo getResponseInfo() {
        return ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest UserRequest) {
        this.userRequest = UserRequest;
    }
}
