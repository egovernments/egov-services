package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Probation;

public class ProbationBuilder {

    Probation probation = new Probation();

    public ProbationBuilder() {
        probation.setDesignation(1);
        probation.setDeclaredOn("18/09/2016");
        probation.setOrderNo("A1");
        probation.setOrderDate("18/09/2016");
        probation.setRemarks("None");
        probation.setCreatedBy(1);
        probation.setCreatedDate("18/09/2016");
        probation.setLastModifiedBy(1);
        probation.setLastModifiedDate("18/09/2016");
    }

    public Probation build() {
        return probation;
    }

}
