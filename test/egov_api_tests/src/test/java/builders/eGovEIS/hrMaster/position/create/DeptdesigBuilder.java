package builders.eGovEIS.hrMaster.position.create;

import entities.requests.eGovEIS.hrMaster.position.create.Deptdesig;
import entities.requests.eGovEIS.hrMaster.position.create.Designation;

public class DeptdesigBuilder {

    Deptdesig deptdesig = new Deptdesig();
    Designation designation = new DesignationBuilder().build();

    public DeptdesigBuilder() {
        deptdesig.setDesignation(designation);
        deptdesig.setDepartment(1);
    }

    public DeptdesigBuilder withDesignation(Designation designation) {
        deptdesig.setDesignation(designation);
        return this;
    }

    public DeptdesigBuilder withDepartment(int department) {
        deptdesig.setDepartment(department);
        return this;
    }

    public Deptdesig build() {
        return deptdesig;
    }
}
