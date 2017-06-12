package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Test;

public class TestBuilder {

    Test test = new Test();

    public TestBuilder() {
        test.setTest("A01");
        test.setYearOfPassing(2014);
        test.setRemarks("None");
        test.setCreatedBy(1);
        test.setCreatedDate("18/09/2016");
        test.setLastModifiedBy(1);
        test.setLastModifiedDate("18/09/2016");
    }

    public Test build() {
        return test;
    }
}
