package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.Roles;
import entities.requests.userServices.createWithValidate.User;

public class UserBuilder {

    User user = new User();
    Roles[] roles = new Roles[1];
    Roles role1 = new RolesBuilder().build();

    public UserBuilder(){
        user.setPassword("12345");
        user.setSalutation("mrs");
        user.setName("Tester");
        user.setGender("MALE");
        user.setEmailId("Tester@testing.com");
        user.setDob("01/01/1990");
        user.setLocale("en_IN");
        user.setType("CITIZEN");
        user.setAccountLocked(false);
        user.setActive(true);
        user.setTenantId("default");
        roles[0] = role1;
        user.setRoles(roles);
    }

    public UserBuilder withUserName(String name){
        user.setUserName(name);
        return this;
    }

    public UserBuilder withMobileNumber(String number){
        user.setMobileNumber(number);
        return this;
    }

    public UserBuilder withOtpReference(String otpReference){
        user.setOtpReference(otpReference);
        return this;
    }

    public User build() {
        return user;
    }
}
