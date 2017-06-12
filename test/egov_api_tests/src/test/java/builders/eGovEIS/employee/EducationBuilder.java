package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Education;

public class EducationBuilder {

    Education education = new Education();

    public EducationBuilder() {
        education.setQualification("B.E");
        education.setMajorSubject("Electricals");
        education.setYearOfPassing(2012);
        education.setUniversity("VTU");
        education.setCreatedBy(1);
        education.setCreatedDate("18/09/2016");
        education.setLastModifiedBy(1);
        education.setLastModifiedDate("18/09/2016");
    }

    public Education build() {
        return education;
    }
}
