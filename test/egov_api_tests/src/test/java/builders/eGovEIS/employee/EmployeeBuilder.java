package builders.eGovEIS.employee;

import entities.requests.eGovEIS.employee.*;

public class EmployeeBuilder {

    Employee employee = new Employee();
    int[] jurisdictions = new int[1];
    int jurisduction = 1;
    int[] languages = new int[3];
    int language1 = 1;
    int language2 = 3;
    int language3 = 5;

    Assignments[] assignmentses = new Assignments[2];
    Assignments assignments1 = new AssignmentsBuilder().build();
    Assignments assignments2 = new AssignmentsBuilder("second").build();

    ServiceHistory[] serviceHistories = new ServiceHistory[1];
    ServiceHistory serviceHistory = new ServiceHistoryBuilder().build();

    Probation[] probations = new Probation[1];
    Probation probation = new ProbationBuilder().build();

    Regularisation[] regularisations = new Regularisation[1];
    Regularisation regularisation = new RegularisationBuilder().build();

    Technical[] technicals = new Technical[1];
    Technical technical = new TechnicalBuilder().build();

    Education[] educations = new Education[1];
    Education education = new EducationBuilder().build();

    Test[] tests = new Test[1];
    Test test = new TestBuilder().build();

    public EmployeeBuilder() {
        employee.setDateOfAppointment("18/09/2016");
        employee.setDateOfJoining("18/09/2016");
        employee.setDateOfRetirement("18/09/2016");
        employee.setEmployeeStatus(1);
        employee.setRecruitmentMode(1);
        employee.setRecruitmentType(1);
        employee.setRecruitmentQuota(1);
        employee.setRetirementAge("45");
        employee.setDateOfResignation("18/09/2016");
        employee.setDateOfTermination("18/09/2016");
        employee.setEmployeeType(3);
        jurisdictions[0] = jurisduction;
        employee.setJurisdictions(jurisdictions);
        employee.setMotherTongue(5);
        employee.setReligion(1);
        employee.setCategory(3);
        employee.setCommunity(6);
        employee.setPhysicallyDisabled(false);
        employee.setMedicalReportProduced(false);
        languages[0] = language1;
        languages[1] = language2;
        languages[2] = language3;
        employee.setLanguagesKnown(languages);
        employee.setMaritalStatus("Unmarried");
        employee.setBank(21);
        employee.setBankBranch(30);
        employee.setBankAccount("987456");
        employee.setGroup(33);
        employee.setPlaceOfBirth("Bengaluru");
        employee.setTenantId("ap.public");
        assignmentses[0] = assignments1;
        assignmentses[1] = assignments2;
        employee.setAssignments(assignmentses);
        serviceHistories[0] = serviceHistory;
        employee.setServiceHistory(serviceHistories);
        probations[0] = probation;
        employee.setProbation(probations);
        regularisations[0] = regularisation;
        employee.setRegularisation(regularisations);
        technicals[0] = technical;
        employee.setTechnical(technicals);
        educations[0] = education;
        employee.setEducation(educations);
        tests[0] = test;
        employee.setTest(tests);
    }

    public EmployeeBuilder withPassportNo(String passportNo) {
        employee.setPassportNo(passportNo);
        return this;
    }

    public EmployeeBuilder withUser(User user) {
        employee.setUser(user);
        return this;
    }

    public EmployeeBuilder withGpfNo(String gpfNo) {
        employee.setGpfNo(gpfNo);
        return this;
    }

    public EmployeeBuilder withCode(String code) {
        employee.setCode(code);
        return this;
    }

    public Employee build() {
        return employee;
    }
}
