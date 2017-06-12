package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.CreateEmployeeRequest;
import entities.requests.eGovEIS.employee.Employee;
import entities.requests.eGovEIS.employee.RequestInfo;

public class CreateEmployeeRequestBuilder {

    CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();

    public CreateEmployeeRequestBuilder() {
    }

    public CreateEmployeeRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        createEmployeeRequest.setRequestInfo(requestInfo);
        return this;
    }

    public CreateEmployeeRequestBuilder withEmployee(Employee employee) {
        createEmployeeRequest.setEmployee(employee);
        return this;
    }

    public CreateEmployeeRequest build() {
        return createEmployeeRequest;
    }
}
