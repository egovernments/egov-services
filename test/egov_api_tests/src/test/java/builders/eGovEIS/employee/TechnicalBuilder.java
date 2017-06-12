package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Technical;

public class TechnicalBuilder {

    Technical technical = new Technical();

    public TechnicalBuilder() {
        technical.setSkill("C");
        technical.setGrade("A");
        technical.setYearOfPassing(2014);
        technical.setCreatedBy(1);
        technical.setRemarks("None");
        technical.setCreatedDate("18/09/2016");
        technical.setLastModifiedBy(1);
        technical.setLastModifiedDate("18/09/2016");
    }

    public Technical build() {
        return technical;
    }
}
