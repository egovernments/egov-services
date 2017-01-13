package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken = null;

    @JsonProperty("token_type")
    private String tokenType = null;

    @JsonProperty("refresh_token")
    private String refreshToken = null;

    @JsonProperty("expires_in")
    private String expiresIn = null;

    @JsonProperty("scope")
    private String scope = null;

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("User")
    private User user = null;

    public LoginResponse accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponse tokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public LoginResponse refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LoginResponse expiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public LoginResponse scope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public LoginResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public LoginResponse user(User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginResponse loginResponse = (LoginResponse) o;
        return Objects.equals(this.accessToken, loginResponse.accessToken) &&
                Objects.equals(this.tokenType, loginResponse.tokenType) &&
                Objects.equals(this.refreshToken, loginResponse.refreshToken) &&
                Objects.equals(this.expiresIn, loginResponse.expiresIn) &&
                Objects.equals(this.scope, loginResponse.scope) &&
                Objects.equals(this.responseInfo, loginResponse.responseInfo) &&
                Objects.equals(this.user, loginResponse.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, refreshToken, expiresIn, scope, responseInfo, user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LoginResponse {\n");

        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    user: ").append(toIndentedString(user)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
