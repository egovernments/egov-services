package builders;

import entities.LoginDetails;

public class LoginDetailsBuilder {

    LoginDetails loginDetails = new LoginDetails();

    public LoginDetailsBuilder() {
        loginDetails.setLoginId("944177");
        loginDetails.setPassword("kurnool");
    }

    public LoginDetailsBuilder withLoginId(String loginId) {
        loginDetails.setLoginId(loginId);
        return this;
    }

    public LoginDetailsBuilder withPassword(String password) {
        loginDetails.setPassword(password);
        return this;
    }

    public LoginDetails build() {
        return loginDetails;
    }

    public LoginDetailsBuilder withHasZone(boolean hasZone) {
        loginDetails.setHasZone(hasZone);
        return this;
    }
}
