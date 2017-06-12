package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.Roles;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.UserInfo;

public class UserInfoBuilder {

    UserInfo userInfo = new UserInfo();
    Roles[] roles = new Roles[1];
    Roles role1 = new RolesBuilder().build();

    public UserInfoBuilder(){
        userInfo.setName("Tester");
        userInfo.setId(82);
        userInfo.setTenantId("ap.kurnool");
        roles[0] = role1;
        userInfo.setRoles(roles);
    }

    public UserInfo build(){
        return userInfo;
    }
}
