package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Assignments;
import entities.requests.eGovEIS.employee.Hod;

public class AssignmentsBuilder {

    Assignments assignments = new Assignments();
    Hod[] hods1 = new Hod[2];
    Hod[] hods2 = new Hod[1];
    Hod hod1 = new HodBuilder().withDepartment(5).build();
    Hod hod2 = new HodBuilder().withDepartment(18).build();
    Hod hod3 = new HodBuilder().withDepartment(2).build();


    public AssignmentsBuilder() {
        assignments.setEmployee(2);
        assignments.setPosition(1);
        assignments.setIsPrimary(true);
        assignments.setFund(1);
        assignments.setFunctionary(1);
        assignments.setFunction(1);
        assignments.setDepartment(5);
        assignments.setDesignation(5);
        assignments.setFromDate("01/01/2016");
        assignments.setToDate("31/12/2016");
        assignments.setGrade(1);
        assignments.setGovtOrderNumber("sadda");
        assignments.setCreatedBy(1);
        assignments.setCreatedDate("18/09/2016");
        assignments.setLastModifiedBy(3);
        assignments.setLastModifiedDate("18/09/2016");
        hods1[0] = hod1;
        hods1[1] = hod2;
        assignments.setHod(hods1);
    }

    public AssignmentsBuilder(String msg) {
        assignments.setEmployee(3);
        assignments.setPosition(2);
        assignments.setIsPrimary(false);
        assignments.setFund(1);
        assignments.setFunctionary(1);
        assignments.setFunction(1);
        assignments.setDepartment(2);
        assignments.setDesignation(2);
        assignments.setFromDate("01/01/2017");
        assignments.setToDate("31/12/2017");
        assignments.setGrade(1);
        assignments.setGovtOrderNumber("sadda");
        assignments.setCreatedBy(1);
        assignments.setCreatedDate("18/09/2016");
        assignments.setLastModifiedBy(3);
        assignments.setLastModifiedDate("18/09/2016");
        hods2[0] = hod3;
        assignments.setHod(hods2);
    }

    public Assignments build() {
        return assignments;
    }
}
