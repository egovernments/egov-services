package builders.employeeManagement.createAttendance;

import entities.employeeManagement.createAttendance.AllEmployeeCode;

public class AllEmployeeCodeBuilder {

    AllEmployeeCode allEmployeeCode = new AllEmployeeCode();

    public AllEmployeeCodeBuilder withEmployeeCode(String employeeCode1) {
        allEmployeeCode.setEmployeeCode(employeeCode1);
        return this;
    }

    public AllEmployeeCode build() {
        return allEmployeeCode;
    }
}
