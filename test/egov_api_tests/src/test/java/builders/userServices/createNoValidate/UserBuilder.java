package builders.userServices.createNoValidate;

import entities.requests.userServices.createNoValidate.Roles;
import entities.requests.userServices.createNoValidate.User;

public class UserBuilder {

   User user = new User();
   Roles[] roles = new Roles[1];
   Roles role1 = new RolesBuilder().build();

   public UserBuilder(){
       user.setPassword("12345");
       user.setSalutation("Mrs");
       user.setName("Testing");
       user.setGender("MALE");
       user.setMobileNumber("9999999999");
       user.setEmailId("Tester@Testing.com");
       user.setDob("20/02/1990");
       user.setLocale("en_IN");
       user.setType("CITIZEN");
       user.setAccountLocked(false);
       user.setTenantId("default");
       user.setActive(true);
       roles[0] = role1;
       user.setRoles(roles);
   }

   public UserBuilder(String update){
       user.setTenantId("default");
       user.setName("UpdatedTester");
       user.setActive(true);
   }

   public UserBuilder withUserName(String userName){
       user.setUserName(userName);
       return this;
   }

   public UserBuilder withMobileNumber(String mobileNumber){
       user.setMobileNumber(mobileNumber);
       return this;
   }

   public User build(){
       return user;
   }
}

