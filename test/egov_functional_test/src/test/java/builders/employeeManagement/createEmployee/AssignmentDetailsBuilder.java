package builders.employeeManagement.createEmployee;

import entities.employeeManagement.createEmployee.AssignmentDetails;

public class AssignmentDetailsBuilder {

    AssignmentDetails assignmentDetails = new AssignmentDetails();

    public AssignmentDetailsBuilder() {
    }

    public AssignmentDetailsBuilder withIsPrimary(String isPrimary) {
        assignmentDetails.setIsPrimary(isPrimary);
        return this;
    }

    public AssignmentDetailsBuilder withFromDate(String fromDate) {
        assignmentDetails.setFromDate(fromDate);
        return this;
    }

    public AssignmentDetailsBuilder withToDate(String toDate) {
        assignmentDetails.setToDate(toDate);
        return this;
    }

    public AssignmentDetailsBuilder withDepartment(String department) {
        assignmentDetails.setDepartment(department);
        return this;
    }

    public AssignmentDetailsBuilder withDesignation(String designation) {
        assignmentDetails.setDesignation(designation);
        return this;
    }

    public AssignmentDetailsBuilder withPosition(String position) {
        assignmentDetails.setPosition(position);
        return this;
    }

    public AssignmentDetailsBuilder withGrade(String grade) {
        assignmentDetails.setGrade(grade);
        return this;
    }

    public AssignmentDetailsBuilder withFunction(String function) {
        assignmentDetails.setFunction(function);
        return this;
    }

    public AssignmentDetailsBuilder withFunctionary(String functionary) {
        assignmentDetails.setFunctionary(functionary);
        return this;
    }

    public AssignmentDetailsBuilder withFund(String fund) {
        assignmentDetails.setFund(fund);
        return this;
    }

    public AssignmentDetailsBuilder withIsHODTrueButton(boolean isHOD) {
        assignmentDetails.setIsHOD(isHOD);
        return this;
    }

    public AssignmentDetailsBuilder withGovtOrderNumber(String govtOrderNumber) {
        assignmentDetails.setGovtOrderNumber(govtOrderNumber);
        return this;
    }

    public AssignmentDetails build() {
        return assignmentDetails;
    }
}
