package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.Roles;

public class RolesBuilder {

    Roles roles = new Roles();

    public RolesBuilder(){
        roles.setName("assistantEngineer");
    }

    public Roles build(){
        return roles;
    }
}
