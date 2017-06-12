package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Roles;

public class RolesBuilder {

    Roles roles = new Roles();

    public RolesBuilder() {
        roles.setName("EE");
        roles.setDescription("Executive Engineer");
        roles.setCreatedBy(1);
        roles.setCreatedDate("01/01/2017");
        roles.setLastModifiedBy(1);
        roles.setLastModifiedDate("01/01/2017");
    }

    public RolesBuilder(String msg) {
        roles.setName("Manager");
        roles.setDescription("Manager for Section");
        roles.setCreatedBy(1);
        roles.setCreatedDate("01/01/2017");
        roles.setLastModifiedBy(1);
        roles.setLastModifiedDate("01/01/2017");
    }

    public Roles build() {
        return roles;
    }
}
