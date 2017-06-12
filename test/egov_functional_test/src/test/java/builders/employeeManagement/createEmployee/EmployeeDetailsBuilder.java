package builders.employeeManagement.createEmployee;

import entities.employeeManagement.createEmployee.EmployeeDetails;

public final class EmployeeDetailsBuilder {

    EmployeeDetails employeeDetails = new EmployeeDetails();

    public EmployeeDetailsBuilder() {
    }

    public EmployeeDetailsBuilder withEmployeeType(String employeeType) {
        employeeDetails.setEmployeeType(employeeType);
        return this;
    }

    public EmployeeDetailsBuilder withStatus(String status) {
        employeeDetails.setStatus(status);
        return this;
    }

    public EmployeeDetailsBuilder withDateOfBirth(String dateOfBirth) {
        employeeDetails.setDateOfBirth(dateOfBirth);
        return this;
    }

    public EmployeeDetailsBuilder withGender(String gender) {
        employeeDetails.setGender(gender);
        return this;
    }

    public EmployeeDetailsBuilder withMaritalStatus(String maritalStatus) {
        employeeDetails.setMaritalStatus(maritalStatus);
        return this;
    }

    public EmployeeDetailsBuilder withUserName(String userName) {
        employeeDetails.setUserName(userName);
        return this;
    }

    public EmployeeDetailsBuilder withIsUserActive(String isUserActive) {
        employeeDetails.setIsUserActive(isUserActive);
        return this;
    }

    public EmployeeDetailsBuilder withMobileNumber(String mobileNumber) {
        employeeDetails.setMobileNumber(mobileNumber);
        return this;
    }

    public EmployeeDetailsBuilder withPermanentAddress(String permanentAddress) {
        employeeDetails.setPermanentAddress(permanentAddress);
        return this;
    }

    public EmployeeDetailsBuilder withPermanentCity(String permanentCity) {
        employeeDetails.setPermanentCity(permanentCity);
        return this;
    }

    public EmployeeDetailsBuilder withPermanentPincode(String permanentPincode) {
        employeeDetails.setPermanentPincode(permanentPincode);
        return this;
    }

    public EmployeeDetailsBuilder withDateOfAppointment(String dateOfAppointment) {
        employeeDetails.setDateOfAppointment(dateOfAppointment);
        return this;
    }

    public EmployeeDetails build() {
        return employeeDetails;
    }
}
