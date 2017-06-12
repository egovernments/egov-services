package builders.login;

import entities.requests.login.LoginRequestForPilotService;

public class LoginRequestForPilotServiceBuilder {

    LoginRequestForPilotService loginRequestForPilotService = new LoginRequestForPilotService();

    public LoginRequestForPilotServiceBuilder() {
        loginRequestForPilotService.setJ_username("egovernments");
        loginRequestForPilotService.setJ_password("kurnool_eGov@123");
    }

    public LoginRequestForPilotServiceBuilder withJ_username(String j_username) {
        loginRequestForPilotService.setJ_username(j_username);
        return this;
    }

    public LoginRequestForPilotServiceBuilder withJ_password(String j_password) {
        loginRequestForPilotService.setJ_password(j_password);
        return this;
    }

    public LoginRequestForPilotService build() {
        return loginRequestForPilotService;
    }
}
